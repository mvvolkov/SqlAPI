package serverLoggerImpl;

import api.*;
import api.exceptions.*;
import api.selectionPredicate.ColumnColumnPredicate;
import api.selectionPredicate.ColumnValuePredicate;
import api.selectionPredicate.CombinedPredicate;
import api.selectionPredicate.Predicate;
import clientDefaultImpl.VarcharColumnMetadataImpl;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SqlServerLoggerImpl implements SqlServer {

    private final Collection<DatabaseImpl> databases = new ArrayList<>();

    public static String getColumnMetadataString(ColumnMetadata columnMetadata) {
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

    @Override
    public void executeStatement(SqlStatement stmt) throws SqlException {
        switch (stmt.getType()) {
            case CREATE_TABLE:
                this.createTable((CreateTableStatement) stmt);
                return;
            case INSERT:
                this.insert((InsertStatement) stmt);
        }
    }

    private void createTable(CreateTableStatement stmt) {
        StringBuilder sb = new StringBuilder("CREATE TABLE ");
        sb.append(stmt.getTableName());
        sb.append("(");
        sb.append(stmt.getColumns().stream().map(c -> getColumnMetadataString(c)).collect(Collectors.joining(", ")));
        sb.append(");");
        System.out.println(sb);
    }

    private static String getStringFromInsertValue(Object value) {
        if (value instanceof String) {
            return '\'' + (String) value + '\'';
        } else if (value == null) {
            return "NULL";
        }
        return String.valueOf(value);
    }

    private void insert(InsertStatement stmt) {

        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(stmt.getDatabaseName());
        sb.append(".");
        sb.append(stmt.getTableName());
        sb.append(" VALUES (");
        String valuesString = stmt.getValues().stream().map(o -> getStringFromInsertValue(o)).collect(Collectors.joining(", "));
        sb.append(valuesString);
        sb.append(");");
        System.out.println(sb);

    }

    @Override
    public void createDatabase(String dbName) throws DatabaseAlreadyExistsException {
        for (Database database : databases) {
            if (database.getName().equals(dbName)) {
                throw new DatabaseAlreadyExistsException(dbName);
            }
        }
        databases.add(new DatabaseImpl(dbName));
    }

    @Override
    public void openDatabaseWithTables(String dbName, List<TableMetadata> tables) {
    }

    @Override
    public void persistDatabase(String dbName) {
    }

    @Override
    public Database getDatabaseOrNull(String dbName) {
        for (Database database : databases) {
            if (database.getName().equals(dbName)) {
                return database;
            }
        }
        return null;
    }

    @Override
    public Database getDatabase(String dbName) throws NoSuchDatabaseException {
        Database database = this.getDatabaseOrNull(dbName);
        if (database == null) {
            throw new NoSuchDatabaseException(dbName);
        }
        return database;
    }

    @Override
    public @NotNull ResultSet select(SelectExpression selectExpression) throws WrongValueTypeException, NoSuchTableException {
        StringBuilder sb = new StringBuilder("SELECT ");
        String from = selectExpression.getSelectedItems()
                .stream().map(SelectedItem::toString).collect(Collectors.joining(", "));
        sb.append(from);
        sb.append(" FROM ");
        String tables = selectExpression.getTableReferences().stream().
                map(table -> getTableReferencesString(table)).collect(Collectors.joining(", "));
        sb.append(tables);
        if (!selectExpression.getPredicate().isTrue()) {
            sb.append(" WHERE ");
            sb.append(getPredicateString(selectExpression.getPredicate()));
        }
        sb.append(";");
        System.out.println(sb);
        return this.getEmptyResultSet();
    }

    private ResultSet getEmptyResultSet() {
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

        if (tableReference instanceof BaseTableReference) {
            return getBaseTableRefString((BaseTableReference) tableReference);
        }
        if (tableReference instanceof JoinTableReference) {
            return getJoinTableRefString((JoinTableReference) tableReference);
        }
        return "";
    }

    private static String getBaseTableRefString(BaseTableReference btr) {
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
        return "";
    }

    private static String getColumnColumnPredicateString(ColumnColumnPredicate ccp) {
        StringBuilder sb = new StringBuilder();
        sb.append(getColumnRefString(ccp.getLeftColumn()));
        sb.append(" ");
        sb.append(getOperatorString(ccp.getType()));
        sb.append(" ");
        sb.append(ccp.getRightColumn());
        return sb.toString();
    }

    private static String getOperatorString(Predicate.Type type) {
        switch (type) {
            case AND:
                return "AND";
            case OR:
                return "OR";
            case IS_NOT_NULL:
                return "IS NOT NULL";
            case IS_NULL:
                return "IS NULL";
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
        sb.append(getColumnRefString(cvp.getColumnReference()));
        sb.append(" ");
        sb.append(getOperatorString(cvp.getType()));
        sb.append(" ");
        sb.append(cvp.getValue());
        return sb.toString();
    }


    private static String getColumnRefString(ColumnReference cr) {
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
