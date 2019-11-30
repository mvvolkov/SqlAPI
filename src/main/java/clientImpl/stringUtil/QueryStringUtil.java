package clientImpl.stringUtil;

import sqlapi.columnExpr.ColumnExpression;
import sqlapi.exceptions.*;
import sqlapi.misc.AssignmentOperation;
import sqlapi.misc.SelectedItem;
import sqlapi.queries.*;
import sqlapi.tables.DatabaseTableReference;
import sqlapi.tables.TableReference;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class QueryStringUtil {

    private QueryStringUtil() {
    }

    public static void printQuery(SqlQuery query) throws SqlException {
        System.out.println(getQueryString(query));
    }

    public static void printSelectQuery(SelectQuery query) throws SqlException {
        System.out.println(getSelectQueryString(query) + ";");
    }

    public static String getQueryString(SqlQuery query) throws SqlException {


        if (query instanceof CreateDatabaseQuery) {
            return getCreateDatabaseString((CreateDatabaseQuery) query);
        }
        if (query instanceof CreateTableQuery) {
            return getCreateTableString((CreateTableQuery) query);
        }
        if (query instanceof DropTableQuery) {
            return getDropTableString((DropTableQuery) query);
        }
        if (query instanceof InsertQuery) {
            return getInsertQueryString((InsertQuery) query);
        }
        if (query instanceof DeleteQuery) {
            return getDeleteQueryString((DeleteQuery) query);
        }
        if (query instanceof UpdateQuery) {
            return getUpdateQueryString((UpdateQuery) query);
        }

        if (query instanceof InsertFromSelectQuery) {
            return getInsertFromSelectQueryString((InsertFromSelectQuery) query);
        }
        throw new UnsupportedQueryTypeException(query);
    }


    public static String getCreateDatabaseString(CreateDatabaseQuery query) {
        return "CREATE DATABASE " + query.getDatabaseName() + ";";
    }

    public static String getDropTableString(DropTableQuery query) {
        return "DROP TABLE " + query.getDatabaseName() + "." + query.getTableName();
    }

    public static String getCreateTableString(CreateTableQuery query) throws
            UnsupportedColumnConstraintTypeException {
        return "CREATE TABLE " + query.getDatabaseName() + "."
                + MetadataStringUtil.getTableMetadataString(query.getTableMetadata()) + ";";
    }

    public static String getInsertQueryString(InsertQuery query) {
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(query.getDatabaseName());
        sb.append(".");
        sb.append(query.getTableName());
        if (!query.getColumns().isEmpty()) {
            sb.append("(");
            sb.append(String.join(", ", query.getColumns()));
            sb.append(")");
        }
        sb.append(" VALUES (");
        String valuesString = query.getValues().stream().map(ColumnExprStringUtil::getColumnValueString)
                .collect(Collectors.joining(", "));
        sb.append(valuesString);
        sb.append(");");
        return sb.toString();
    }

    public static String getDeleteQueryString(DeleteQuery query) throws UnsupportedPredicateTypeException {
        StringBuilder sb = new StringBuilder("DELETE FROM ");
        sb.append(query.getDatabaseName());
        sb.append(".");
        sb.append(query.getTableName());
        if (!query.getPredicate().isEmpty()) {
            sb.append(" WHERE ").append(PredicateStringUtil.getPredicateString(query.getPredicate()));
        }
        sb.append(";");
        return sb.toString();
    }

    public static String getUpdateQueryString(UpdateQuery query) throws
            UnsupportedAggregateFunctionTypeException, UnsupportedColumnExprTypeException, UnsupportedPredicateTypeException {
        StringBuilder sb = new StringBuilder("UPDATE ");
        sb.append(query.getDatabaseName());
        sb.append(".");
        sb.append(query.getTableName());
        sb.append(" SET ");
        StringJoiner joiner = new StringJoiner(", ");
        for (AssignmentOperation assignmentOperation : query.getAssignmentOperations()) {
            String assignmentOperationString = AssignmentStringUtil.getAssignmentOperationString(assignmentOperation);
            joiner.add(assignmentOperationString);
        }
        sb.append(joiner.toString());
        if (!query.getPredicate().isEmpty()) {
            sb.append(" WHERE ").append(PredicateStringUtil.getPredicateString(query.getPredicate()));
        }
        sb.append(";");
        return sb.toString();
    }

    public static String getSelectQueryString(SelectQuery query)
            throws SqlException {
        StringBuilder sb = new StringBuilder("SELECT ");
        if (query.getSelectedItems().isEmpty()) {
            sb.append("*");
        } else {
            List<String> columns = new ArrayList<>();
            for (SelectedItem se : query.getSelectedItems()) {
                if (se instanceof ColumnExpression) {
                    columns.add(ColumnExprStringUtil.getColumnExpressionString((ColumnExpression) se));
                }
                if (se instanceof DatabaseTableReference) {
                    columns.add(TableRefStringUtil.getDatabaseTableReferenceString((DatabaseTableReference) se)
                            + ".*");
                }
            }
            String from = String.join(", ", columns);
            sb.append(from);
        }
        sb.append(" FROM ");
        StringJoiner joiner = new StringJoiner(", ");
        for (TableReference tableReference : query.getTableReferences()) {
            String tableReferenceString = TableRefStringUtil.getTableReferenceString(tableReference);
            joiner.add(tableReferenceString);
        }
        String tables = joiner.toString();
        sb.append(tables);
        if (!query.getPredicate().isEmpty()) {
            sb.append(" WHERE ").append(PredicateStringUtil.getPredicateString(query.getPredicate()));
        }
        if (!query.getGroupByColumns().isEmpty()) {
            sb.append(" GROUP BY ");
            sb.append(query.getGroupByColumns().stream().map(ColumnExprStringUtil::getColumnRefString)
                    .collect(Collectors.joining(", ")));
        }
        return sb.toString();
    }

    public static String getInsertFromSelectQueryString(InsertFromSelectQuery query) throws SqlException {
        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(query.getDatabaseName());
        sb.append(".");
        sb.append(query.getTableName());
        if (!query.getColumns().isEmpty()) {
            sb.append("(");
            sb.append(String.join(", ", query.getColumns()));
            sb.append(")");
        }
        sb.append(" ").append(getSelectQueryString(query.getSelectQuery())).append(";");
        return sb.toString();
    }


}
