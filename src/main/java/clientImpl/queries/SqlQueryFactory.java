package clientImpl.queries;

import api.*;
import api.columnExpr.ColumnValue;
import api.metadata.ColumnMetadata;
import api.predicates.Predicate;
import api.queries.*;
import clientImpl.predicates.SelectionPredicateImpl;

import java.util.List;

public class SqlQueryFactory {

    private SqlQueryFactory() {
    }

    public static CreateTableStatement createTable(String databaseName,
                                                   String tableName,
                                                   List<ColumnMetadata> columns) {
        return new CreateTableStatementImpl(databaseName, tableName, columns);
    }


    public static InsertStatement insert(String databaseName, String tableName,
                                         List<ColumnValue> values) {
        return new InsertStatementImpl(databaseName, tableName, values);
    }


    public static InsertStatement insert(String databaseName, String tableName,
                                         List<String> columns,
                                         List<ColumnValue> values) {
        return new InsertStatementImpl(databaseName, tableName, columns, values);
    }


    public static DeleteStatement delete(String databaseName, String tableName) {
        return new DeleteStatementImpl(databaseName, tableName,
                new SelectionPredicateImpl(Predicate.Type.TRUE));
    }


    public static DeleteStatement delete(String databaseName, String tableName,
                                         Predicate predicate) {
        return new DeleteStatementImpl(databaseName, tableName, predicate);
    }

    public static UpdateStatement update(String databaseName, String tableName,
                                         List<AssignmentOperation> assignmentOperations) {
        return new UpdateStatementImpl(databaseName, tableName, assignmentOperations,
                new SelectionPredicateImpl(Predicate.Type.TRUE));
    }


    public static UpdateStatement update(String databaseName, String tableName,
                                         List<AssignmentOperation> assignmentOperations,
                                         Predicate predicate) {
        return new UpdateStatementImpl(databaseName, tableName, assignmentOperations,
                predicate);
    }

    public static SelectExpression select(
            List<TableReference> tableReferences, List<SelectedItem> selectedItems,
            Predicate predicate) {
        return new SelectExpressionImpl(tableReferences, selectedItems, predicate);
    }
}
