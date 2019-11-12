package clientImpl.queries;

import api.AssignmentOperation;
import api.SelectedItem;
import api.TableReference;
import api.metadata.ColumnMetadata;
import api.predicates.Predicate;
import api.queries.*;
import clientImpl.predicates.PredicateImpl;

import java.util.List;

public class SqlQueryFactory {

    private SqlQueryFactory() {
    }

    public static CreateTableStatement createTable(String databaseName,
                                                   String tableName,
                                                   List<ColumnMetadata<?>> columns) {
        return new CreateTableStatementImpl(databaseName, tableName, columns);
    }


    public static InsertStatement insert(String databaseName, String tableName,
                                         List<Object> values) {
        return new InsertStatementImpl(databaseName, tableName, values);
    }


    public static InsertStatement insert(String databaseName, String tableName,
                                         List<String> columns,
                                         List<Object> values) {
        return new InsertStatementImpl(databaseName, tableName, columns, values);
    }


    public static DeleteStatement delete(String databaseName, String tableName) {
        return new DeleteStatementImpl(databaseName, tableName,
                new PredicateImpl(Predicate.Type.TRUE));
    }


    public static DeleteStatement delete(String databaseName, String tableName,
                                         Predicate predicate) {
        return new DeleteStatementImpl(databaseName, tableName, predicate);
    }

    public static UpdateStatement update(String databaseName, String tableName,
                                         List<AssignmentOperation> assignmentOperations) {
        return new UpdateStatementImpl(databaseName, tableName, assignmentOperations,
                new PredicateImpl(Predicate.Type.TRUE));
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
