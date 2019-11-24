package serverLoggerImpl;

import sqlapi.columnExpr.ColumnExpression;
import sqlapi.columnExpr.ColumnRef;
import sqlapi.server.SqlServer;
import sqlapi.exceptions.*;
import sqlapi.metadata.ColumnMetadata;
import sqlapi.metadata.TableMetadata;
import sqlapi.misc.AssignmentOperation;
import sqlapi.queries.*;
import sqlapi.misc.SelectedItem;
import sqlapi.selectResult.ResultRow;
import sqlapi.selectResult.ResultSet;
import sqlapi.tables.DatabaseTableReference;
import sqlapi.tables.JoinedTableReference;
import sqlapi.tables.TableFromSelectReference;
import sqlapi.tables.TableReference;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SqlServerLoggerImpl implements SqlServer {


    @Override public void createDatabase(String databaseName) {
        System.out.println("Creating new database: " + databaseName);
    }

    @Override public void readDatabase(String fileName, String databaseName,
                                       Collection<TableMetadata> tables) {
        StringBuilder sb = new StringBuilder();
        sb.append("Reading database \"").append(databaseName);
        sb.append("\" from file ").append(fileName);
        sb.append("\nTables: ");
        sb.append(tables.stream().map(TableMetadata::toString)
                .collect(Collectors.joining("; ")));
        System.out.println(sb);
    }

    @Override public void saveDatabase(String databaseName, String fileName) {
        System.out
                .println("Saving database \"" + databaseName + "\" to file " + fileName);
    }

    @Override
    public void executeQuery(SqlQuery query) throws SqlException {
        if (query instanceof CreateTableQuery) {
            createTable((CreateTableQuery) query);
        }
        if (query instanceof InsertQuery) {
            insert((InsertQuery) query);
        }
        if (query instanceof InsertFromSelectQuery) {
            insert((InsertFromSelectQuery) query);
        }
        if (query instanceof DeleteQuery) {
            delete((DeleteQuery) query);
        }
        if (query instanceof UpdateQuery) {
            update((UpdateQuery) query);
        }
    }

    @Override
    public @NotNull ResultSet getQueryResult(@NotNull SelectQuery selectQuery) {
        System.out.println(getSelectString(selectQuery) + ";");
        return getEmptyResultSet();
    }

    private static String getSelectString(SelectQuery selectQuery) {
        StringBuilder sb = new StringBuilder("SELECT ");
        if (selectQuery.getSelectedItems().isEmpty()) {
            sb.append("*");
        } else {
            List<String> columns = new ArrayList<>();
            for (SelectedItem se : selectQuery.getSelectedItems()) {
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
        String tables = selectQuery.getTableReferences().stream().
                map(table -> getTableReferencesString(table))
                .collect(Collectors.joining(", "));
        sb.append(tables);
        if (!selectQuery.getPredicate().isEmpty()) {
            sb.append(" WHERE ");
            sb.append(selectQuery.getPredicate());
        }
        if (!selectQuery.getGroupByColumns().isEmpty()) {
            sb.append(" GROUP BY ");
            sb.append(
                    selectQuery.getGroupByColumns().stream().map(ColumnRef::toString)
                            .collect(Collectors.joining(", ")));
        }
        return sb.toString();
    }


    private static void createTable(CreateTableQuery query) {
        StringBuilder sb = new StringBuilder("CREATE TABLE ");
        sb.append(query.getDatabaseName());
        sb.append(".");
        sb.append(query.getTableMetadata().toString());
        sb.append(";");
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

    private static void insert(InsertQuery query) {
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(query.getDatabaseName() + "." + query.getTableName());
        if (!query.getColumns().isEmpty()) {
            String columns = query.getColumns().stream()
                    .collect(Collectors.joining(", ", "(", ")"));
            sb.append(columns);
        }
        sb.append(" VALUES (");
        String valuesString =
                query.getValues().stream().map(o -> getStringFromValue(o))
                        .collect(Collectors.joining(", "));
        sb.append(valuesString);
        sb.append(");");
        System.out.println(sb);
    }

    private static void insert(InsertFromSelectQuery stmt) {

        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(stmt.getDatabaseName() + "." + stmt.getTableName());
        if (!stmt.getColumns().isEmpty()) {
            String columns = stmt.getColumns().stream()
                    .collect(Collectors.joining(", ", "(", ")"));
            sb.append(columns);
        }
        sb.append(" ");
        sb.append(getSelectString(stmt.getSelectQuery()));
        sb.append(";");
        System.out.println(sb);
    }

    private static void delete(DeleteQuery stmt) {
        StringBuilder sb = new StringBuilder("DELETE FROM ");
        sb.append(stmt.getDatabaseName() + "." + stmt.getTableName());
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

    private static void update(UpdateQuery stmt) {
        StringBuilder sb = new StringBuilder("UPDATE ");
        sb.append(stmt.getDatabaseName() + "." + stmt.getTableName());
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
            public List<String> getHeaders() {
                return Collections.emptyList();
            }

            @Override
            public List<ResultRow> getRows() {
                return Collections.emptyList();
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
        return btr.getDatabaseName() + "." + btr.getSchemaName() + "." +
                btr.getTableName();
    }

    private static String getTableFromSelectString(TableFromSelectReference tsr) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(getSelectString(tsr.getSelectQuery()));
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
        boolean leftRefIsDbTable =
                jtr.getLeftTableReference() instanceof DatabaseTableReference;
        boolean rightRefIsDbTable =
                jtr.getRightTableReference() instanceof DatabaseTableReference;
        if (!leftRefIsDbTable) {
            sb.append("(");
        }
        sb.append(getTableReferencesString(jtr.getLeftTableReference()));
        if (!leftRefIsDbTable) {
            sb.append(")");
        }
        sb.append(" ");
        sb.append(operator);
        sb.append(" ");
        if (!rightRefIsDbTable) {
            sb.append("(");
        }
        sb.append(getTableReferencesString(jtr.getRightTableReference()));
        if (!rightRefIsDbTable) {
            sb.append(")");
        }
        if (!jtr.getPredicate().isEmpty()) {
            sb.append(" ON ");
            sb.append(jtr.getPredicate());
        }
        return sb.toString();
    }
}
