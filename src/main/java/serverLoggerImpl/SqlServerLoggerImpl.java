package serverLoggerImpl;

import api.*;
import api.columnExpr.ColumnExpression;
import api.columnExpr.ColumnRef;
import api.exceptions.*;
import api.metadata.ColumnMetadata;
import api.metadata.TableMetadata;
import api.metadata.VarcharColumnMetadata;
import api.predicates.*;
import api.queries.*;
import clientImpl.predicates.ColumnInPredicateImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SqlServerLoggerImpl implements SqlServer {


    @Override
    public void executeStatement(SqlStatement stmt) throws SqlException {
        switch (stmt.getType()) {
            case CREATE_TABLE:
                createTable((CreateTableStatement) stmt);
                return;
            case INSERT:
                insert((InsertStatement) stmt);
                return;
            case DELETE:
                delete((DeleteStatement) stmt);
                return;
            case UPDATE:
                update((UpdateStatement) stmt);
                return;
        }
    }

    @Override
    public void createDatabase(String dbName) throws DatabaseAlreadyExistsException {
        System.out.println("CREATE DATABASE " + dbName);
    }

    @Override
    public void openDatabaseWithTables(String dbName, List<TableMetadata> tables) {
    }

    @Override
    public void persistDatabase(String dbName) {
    }


    @Override
    public @NotNull ResultSet select(SelectExpression selectExpression) {
        StringBuilder sb = new StringBuilder("SELECT ");
        String from = selectExpression.getSelectedItems()
                .stream().map(SelectedItem::toString).collect(Collectors.joining(", "));
        sb.append(from);
        sb.append(" FROM ");
        String tables = selectExpression.getTableReferences().stream().
                map(table -> getTableReferencesString(table))
                .collect(Collectors.joining(", "));
        sb.append(tables);
        if (!selectExpression.getPredicate().isTrue()) {
            sb.append(" WHERE ");
            sb.append(getPredicateString(selectExpression.getPredicate()));
        }
        sb.append(";");
        System.out.println(sb);
        return this.getEmptyResultSet();
    }

    private static String getColumnMetadataString(ColumnMetadata columnMetadata) {
        StringBuilder sb = new StringBuilder(columnMetadata.getName());
        sb.append(" ");
        sb.append(columnMetadata.getSqlTypeName());
        if (columnMetadata instanceof VarcharColumnMetadata) {
            sb.append("(");
            sb.append(((VarcharColumnMetadata) columnMetadata).getMaxLength());
            sb.append(")");
        }
        if (columnMetadata.isNotNull()) {
            sb.append(" NOT NULL");
        }
        if (columnMetadata.isPrimaryKey()) {
            sb.append(" PRIMARY KEY");
        }
        return sb.toString();
    }

    private static void createTable(CreateTableStatement stmt) {
        StringBuilder sb = new StringBuilder("CREATE TABLE ");
        sb.append(stmt.getDatabaseName());
        sb.append(".");
        sb.append(stmt.getTableName());
        sb.append("(");
        sb.append(stmt.getColumns().stream().map(c -> getColumnMetadataString(c))
                .collect(Collectors.joining(", ")));
        sb.append(");");
        System.out.println(sb);
    }

    private static String getStringFromValue(Object value) {
        if (value instanceof String) {
            return '\'' + (String) value + '\'';
        } else if (value == null) {
            return "NULL";
        }
        return String.valueOf(value);
    }

    private static void insert(InsertStatement stmt) {

        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(stmt.getDatabaseName());
        sb.append(".");
        sb.append(stmt.getTableName());
        if (stmt.getColumns() != null) {
            String columns = stmt.getColumns().stream()
                    .collect(Collectors.joining(", ", "(", ")"));
            sb.append(columns);
        }
        sb.append(" VALUES (");
        String valuesString =
                stmt.getValues().stream().map(o -> getStringFromValue(o.getValue()))
                        .collect(Collectors.joining(", "));
        sb.append(valuesString);
        sb.append(");");
        System.out.println(sb);
    }

    private static void delete(DeleteStatement stmt) {
        StringBuilder sb = new StringBuilder("DELETE FROM ");
        sb.append(stmt.getDatabaseName());
        sb.append(".");
        sb.append(stmt.getTableName());
        if (!stmt.getPredicate().isTrue()) {
            sb.append(" WHERE ");
            sb.append(getPredicateString(stmt.getPredicate()));
        }
        sb.append(";");
        System.out.println(sb);
    }

    private static String getAssignmentOperationString(
            AssignmentOperation assignmentOperation) {
        return assignmentOperation.getColumnName() + "=" +
                getStringFromValue(assignmentOperation.getValue());
    }

    private static void update(UpdateStatement stmt) {
        StringBuilder sb = new StringBuilder("UPDATE ");
        sb.append(stmt.getDatabaseName());
        sb.append(".");
        sb.append(stmt.getTableName());
        sb.append(" SET ");
        String assignmetns = stmt.getAssignmentOperations().stream()
                .map(op -> getAssignmentOperationString(op))
                .collect(Collectors.joining(", "));
        sb.append(assignmetns);
        if (!stmt.getPredicate().isTrue()) {
            sb.append(" WHERE ");
            sb.append(getPredicateString(stmt.getPredicate()));
        }
        sb.append(";");
        System.out.println(sb);
    }


    private static ResultSet getEmptyResultSet() {
        return new ResultSet() {
            @Override
            public List<String> getColumns() {
                return Collections.EMPTY_LIST;
            }

            @Override
            public List<ResultRow> getRows() {
                return Collections.EMPTY_LIST;
            }
        };
    }

    private static String getTableReferencesString(TableReference tableReference) {

        if (tableReference instanceof DatabaseTableReference) {
            return getBaseTableRefString((DatabaseTableReference) tableReference);
        }
        if (tableReference instanceof JoinTableReference) {
            return getJoinTableRefString((JoinTableReference) tableReference);
        }
        return "";
    }

    private static String getBaseTableRefString(DatabaseTableReference btr) {
        return btr.getDatabaseName() + "." + btr.getTableName();
    }

    private static String getJoinTableRefString(JoinTableReference jtr) {
        String operator;
        switch (jtr.getType()) {
            case INNER_JOIN:
                operator = "INNER JOIN";
                break;
            case LEFT_OUTER_JOIN:
                operator = "LEFT OUTER JOIN";
                break;
            case RIGHT_OUTER_JOIN:
                operator = "RIGHT OUTER JOIN";
                break;
            default:
                operator = "";
        }
        String condition = getPredicateString(jtr.getSelectionPredicate());
        StringBuilder sb = new StringBuilder();
        sb.append(getTableReferencesString(jtr.getLeftTableReference()));
        sb.append(" ");
        sb.append(operator);
        sb.append(" ");
        sb.append(getTableReferencesString(jtr.getRightTableReference()));
        if (!condition.isEmpty()) {
            sb.append(" ON ");
            sb.append(condition);
        }
        return sb.toString();
    }

    private static String getPredicateString(Predicate predicate) {
        if (predicate instanceof CombinedPredicate) {
            return getCombinedPredicateString((CombinedPredicate) predicate);
        }
        if (predicate instanceof ColumnValuePredicate) {
            return getColumnValuePredicateString((ColumnValuePredicate) predicate);
        }
        if (predicate instanceof ColumnColumnPredicate) {
            return getColumnColumnPredicateString((ColumnColumnPredicate) predicate);
        }
        if (predicate instanceof ColumnInPredicateImpl) {
            return getColumnInPredicateString((ColumnInPredicate) predicate);
        }
        return "";
    }


    private static String getColumnInPredicateString(ColumnInPredicate cip) {
        StringBuilder sb = new StringBuilder();
        sb.append(getColumnRefString(cip.getColumnRef()));
        sb.append(" IN ");
        String values =
                cip.getColumnValues().stream()
                        .map(obj -> getStringFromValue(obj.getValue()))
                        .collect(Collectors.joining(",", "(", ")"));
        sb.append(values);
        return sb.toString();
    }


    private static String getColumnColumnPredicateString(ColumnColumnPredicate ccp) {
        StringBuilder sb = new StringBuilder();
        sb.append(getColumnRefString(ccp.getLeftColumnRef()));
        sb.append(" ");
        sb.append(getOperatorString(ccp.getType()));
        sb.append(" ");
        sb.append(ccp.getRightColumnRef());
        return sb.toString();
    }

    private static String getOperatorString(Predicate.Type type) {
        switch (type) {
            case AND:
                return "AND";
            case OR:
                return "OR";
            case IN:
                return "IN";
            case EQUALS:
                return "=";
            case NOT_EQUALS:
                return "!=";
            case GREATER_THAN:
                return ">";
            case GREATER_THAN_OR_EQUALS:
                return ">=";
            case LESS_THAN:
                return "<";
            case LESS_THAN_OR_EQUALS:
                return "<=";
            default:
                return "";
        }
    }

    private static String getColumnValuePredicateString(ColumnValuePredicate cvp) {
        StringBuilder sb = new StringBuilder();
        sb.append(getColumnRefString(cvp.getColumnRef()));
        sb.append(" ");
        if (cvp.getColumnValue().getType() == ColumnExpression.Type.NULL_VALUE) {
            if (cvp.getType() == Predicate.Type.EQUALS) {
                sb.append("IS NULL");
            } else if (cvp.getType() == Predicate.Type.NOT_EQUALS) {
                sb.append("IS NOT NULL");
            }
            return sb.toString();
        }
        sb.append(getOperatorString(cvp.getType()));
        sb.append(" ");
        sb.append(getStringFromValue(cvp.getColumnValue().getValue()));
        return sb.toString();
    }


    private static String getColumnRefString(ColumnRef cr) {
        StringBuilder sb = new StringBuilder(cr.getColumnName());
        if (cr.getTableName() != null) {
            sb.insert(0, ".");
            sb.insert(0, cr.getTableName());
        }
        if (cr.getDatabaseName() != null) {
            sb.insert(0, ".");
            sb.insert(0, cr.getDatabaseName());
        }
        return sb.toString();
    }

    private static String getCombinedPredicateString(CombinedPredicate cp) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(getPredicateString(cp.getLeftPredicate()));
        sb.append(")");
        sb.append(" ");
        sb.append(getOperatorString(cp.getType()));
        sb.append(" ");
        sb.append("(");
        sb.append(getPredicateString(cp.getRightPredicate()));
        sb.append(")");
        return sb.toString();
    }
}
