package serverLoggerImpl;

import api.*;
import api.exceptions.*;
import api.metadata.ColumnMetadata;
import api.metadata.TableMetadata;
import api.queries.*;
import api.SelectedItem;
import api.selectResult.ResultRow;
import api.selectResult.ResultSet;
import api.tables.DatabaseTableReference;
import api.tables.JoinTableReference;
import api.tables.TableReference;
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
        if (selectExpression.getSelectedItems().isEmpty()) {
            sb.append("*");
        } else {
            String from = selectExpression.getSelectedItems()
                    .stream().map(SelectedItem::toString)
                    .collect(Collectors.joining(", "));
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
        sb.append(";");
        System.out.println(sb);
        return getEmptyResultSet();
    }


    private static void createTable(CreateTableStatement stmt) {
        StringBuilder sb = new StringBuilder("CREATE TABLE ");
        sb.append(stmt.getDatabaseName());
        sb.append(".");
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
                stmt.getValues().stream().map(o -> getStringFromValue(o))
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
        sb.append(stmt.getDatabaseName());
        sb.append(".");
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
