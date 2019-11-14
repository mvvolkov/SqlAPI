package clientImpl.queries;

import api.AssignmentOperation;
import api.SelectedItem;
import api.tables.TableReference;
import api.metadata.ColumnMetadata;
import api.predicates.Predicate;
import api.queries.*;
import clientImpl.predicates.PredicateFactory;

import java.util.Collections;
import java.util.List;

public class SqlQueryFactory {

    private SqlQueryFactory() {
    }

    public static CreateTableStatement createTable(String tableName,
                                                   List<ColumnMetadata<?>> columns) {
        return new CreateTableStatementImpl(tableName, columns);
    }

    public static InsertStatement insert(String tableName,
                                         List<String> columns,
                                         List<Object> values) {
        return new InsertStatementImpl(tableName, columns, values);
    }

    public static InsertStatement insert(String tableName,
                                         List<Object> values) {
        return insert(tableName, Collections.EMPTY_LIST, values);
    }

    public static InsertFromSelectStatementImpl insert(String tableName,
                                                       List<String> columns,
                                                       SelectExpression selectExpression) {
        return new InsertFromSelectStatementImpl(tableName, columns,
                selectExpression);
    }

    public static InsertFromSelectStatementImpl insert(String tableName,
                                                       SelectExpression selectExpression) {
        return insert(tableName, Collections.EMPTY_LIST,
                selectExpression);
    }

    public static DeleteStatement delete(String databaseName, String tableName) {
        return new DeleteStatementImpl(databaseName, tableName,
                PredicateFactory.empty());
    }


    public static DeleteStatement delete(String databaseName, String tableName,
                                         Predicate predicate) {
        return new DeleteStatementImpl(databaseName, tableName, predicate);
    }

    public static UpdateStatement update(String tableName,
                                         List<AssignmentOperation> assignmentOperations) {
        return new UpdateStatementImpl(tableName, assignmentOperations,
                PredicateFactory.empty());
    }


    public static UpdateStatement update(String tableName,
                                         List<AssignmentOperation> assignmentOperations,
                                         Predicate predicate) {
        return new UpdateStatementImpl(tableName, assignmentOperations,
                predicate);
    }

    public static SelectExpression select(
            List<TableReference> tableReferences, List<SelectedItem> selectedItems,
            Predicate predicate) {
        return new SelectExpressionImpl(tableReferences, selectedItems, predicate);
    }

    public static SelectExpression select(TableReference tableReference,
                                          List<SelectedItem> selectedItems,
                                          Predicate predicate) {
        return new SelectExpressionImpl(Collections.singletonList(tableReference),
                selectedItems,
                predicate);
    }

    public static SelectExpression select(
            List<TableReference> tableReferences,
            Predicate predicate) {
        return new SelectExpressionImpl(tableReferences, Collections.EMPTY_LIST,
                predicate);
    }

    public static SelectExpression select(
            TableReference tableReference,
            Predicate predicate) {
        return new SelectExpressionImpl(Collections.singletonList(tableReference),
                Collections.EMPTY_LIST,
                predicate);
    }

    public static SelectExpression select(
            List<TableReference> tableReferences, List<SelectedItem> selectedItems) {
        return new SelectExpressionImpl(tableReferences, selectedItems,
                PredicateFactory.empty());
    }

    public static SelectExpression select(
            TableReference tableReference, List<SelectedItem> selectedItems) {
        return new SelectExpressionImpl(Collections.singletonList(tableReference),
                selectedItems,
                PredicateFactory.empty());
    }

    public static SelectExpression select(
            List<TableReference> tableReferences) {
        return new SelectExpressionImpl(tableReferences, Collections.emptyList(),
                PredicateFactory.empty());
    }

    public static SelectExpression select(
            TableReference tableReference) {
        return new SelectExpressionImpl(Collections.singletonList(tableReference),
                Collections.emptyList(),
                PredicateFactory.empty());
    }
}
