package serverLoggerImpl;

import api.columnExpr.ColumnExpression;
import api.columnExpr.ColumnRef;
import api.connect.SqlServer;
import api.exceptions.*;
import api.metadata.ColumnMetadata;
import api.metadata.TableMetadata;
import api.misc.AssignmentOperation;
import api.queries.*;
import api.misc.SelectedItem;
import api.selectResult.ResultRow;
import api.selectResult.ResultSet;
import api.tables.DatabaseTableReference;
import api.tables.JoinedTableReference;
import api.tables.TableFromSelectReference;
import api.tables.TableReference;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
            case INSERT_FROM_SELECT:
                insert((InsertFromSelectStatement) stmt);
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
    public @NotNull ResultSet getQueryResult(SelectExpression selectExpression) {
        System.out.println(getSelectString(selectExpression) + ";");
        return getEmptyResultSet();
    }

    private static String getSelectString(SelectExpression selectExpression) {
        StringBuilder sb = new StringBuilder("SELECT ");
        if (selectExpression.getSelectedItems().isEmpty()) {
            sb.append("*");
        } else {
            List<String> columns = new ArrayList<>();
            for (SelectedItem se : selectExpression.getSelectedItems()) {
                if (se instanceof ColumnExpression) {
                    columns.add(se.toString());
                }
                if (se instanceof DatabaseTableReference) {
                    columns.add(se.toString() + ".*");
                }
            }
            String from = columns.stream().collect(Collectors.joining(", "));
            sb.append(from);
        }
        sb.append(" FROM ");
        String tables = selectExpression.getTableReferences().stream().
                map(table -> getTableReferencesString(table))
                .collect(Collectors.joining(", "));
        sb.append(tables);
        if (!selectExpression.getPredicate().isEmpty()) {
            sb.append(" WHERE ");
            sb.append(selectExpression.getPredicate());
        }
        if (!selectExpression.getGroupByColumns().isEmpty()) {
            sb.append(" GROUP BY ");
            sb.append(
                    selectExpression.getGroupByColumns().stream().map(ColumnRef::toString)
                            .collect(Collectors.joining(", ")));
        }
        return sb.toString();
    }


    private static void createTable(CreateTableStatement stmt) {
        StringBuilder sb = new StringBuilder("CREATE TABLE ");
        sb.append(stmt.getTableName());
        sb.append("(");
        sb.append(stmt.getColumns().stream().map(ColumnMetadata::toString)
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
        sb.append(stmt.getTableName());
        if (!stmt.getColumns().isEmpty()) {
            String columns = stmt.getColumns().stream()
                    .collect(Collectors.joining(", ", "(", ")"));
            sb.append(columns);
        }
        sb.append(" VALUES (");
        String valuesString =
                stmt.getValues().stream().map(o -> getStringFromValue(o))
                        .collect(Collectors.joining(", "));
        sb.append(valuesString);
        sb.append(");");
        System.out.println(sb);
    }

    private static void insert(InsertFromSelectStatement stmt) {

        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(stmt.getTableName());
        if (!stmt.getColumns().isEmpty()) {
            String columns = stmt.getColumns().stream()
                    .collect(Collectors.joining(", ", "(", ")"));
            sb.append(columns);
        }
        sb.append(" ");
        sb.append(getSelectString(stmt.getSelectExpression()));
        sb.append(";");
        System.out.println(sb);
    }

    private static void delete(DeleteStatement stmt) {
        StringBuilder sb = new StringBuilder("DELETE FROM ");
        sb.append(stmt.getTableName());
        if (!stmt.getPredicate().isEmpty()) {
            sb.append(" WHERE ");
            sb.append(stmt.getPredicate());
        }
        sb.append(";");
        System.out.println(sb);
    }

    private static String getAssignmentOperationString(
            AssignmentOperation assignmentOperation) {
        return assignmentOperation.getColumnName() + "=" +
                assignmentOperation.getValue();
    }

    private static void update(UpdateStatement stmt) {
        StringBuilder sb = new StringBuilder("UPDATE ");
        sb.append(stmt.getTableName());
        sb.append(" SET ");
        String assignmetns = stmt.getAssignmentOperations().stream()
                .map(op -> getAssignmentOperationString(op))
                .collect(Collectors.joining(", "));
        sb.append(assignmetns);
        if (!stmt.getPredicate().isEmpty()) {
            sb.append(" WHERE ");
            sb.append(stmt.getPredicate());
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
        if (tableReference instanceof JoinedTableReference) {
            return getJoinTableRefString((JoinedTableReference) tableReference);
        }
        if (tableReference instanceof TableFromSelectReference) {
            return getTableFromSelectString((TableFromSelectReference) tableReference);
        }
        return "";
    }

    private static String getBaseTableRefString(DatabaseTableReference btr) {

        if (btr.getSchemaName().isEmpty()) {
            return btr.getDatabaseName() + "." + btr.getTableName();
        }
        return btr.getDatabaseName() + "." + btr.getSchemaName() + "." + btr.getTableName();
    }

    private static String getTableFromSelectString(TableFromSelectReference tsr) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(getSelectString(tsr.getSelectExpression()));
        sb.append(")");
        if (!tsr.getAlias().isEmpty()) {
            sb.append(" ");
            sb.append(tsr.getAlias());
        }
        return sb.toString();
    }

    private static String getJoinTableRefString(JoinedTableReference jtr) {
        String operator;
        switch (jtr.getTableRefType()) {
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
        StringBuilder sb = new StringBuilder();
        sb.append(getTableReferencesString(jtr.getLeftTableReference()));
        sb.append(" ");
        sb.append(operator);
        sb.append(" ");
        sb.append(getTableReferencesString(jtr.getRightTableReference()));
        if (!jtr.getPredicate().isEmpty()) {
            sb.append(" ON ");
            sb.append(jtr.getPredicate());
        }
        return sb.toString();
    }
}
