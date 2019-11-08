package api;

import api.selectionPredicate.Predicate;
import clientDefaultImpl.ColumnReferenceImpl;

import java.util.List;

public interface SqlClient {

    SqlStatement newCreateTableStatement(String databaseName,
                                         String tableName, List<ColumnMetadata> columns);

    SqlStatement newInsertStatement(String databaseName, String tableName, List<Object> values);

    SqlStatement newInsertStatement(String databaseName, String tableName, List<String> columns, List<Object> values);

    SqlStatement newDeleteStatement(String databaseName, String tableName);

    SqlStatement newDeleteStatement(String databaseName, String tableName, Predicate predicate);

    SqlStatement newUpdateStatement(String databaseName, String tableName,
                                    List<AssignmentOperation> assignmentOperations);

    SqlStatement newUpdateStatement(String databaseName, String tableName,
                                    List<AssignmentOperation> assignmentOperations, Predicate predicate);

    TableMetadata tableMetadata(String tableName, List<ColumnMetadata> columnsMetadata);

    ColumnMetadataBuilder getIntegerColumnMetadataBuilder(String columnName);

    ColumnMetadataBuilder getVarcharColumnMetadataBuilder(String columnName, int maxLength);

    TableReference baseTableRef(String tableName, String databaseName);

    TableReference innerJoin(TableReference left, TableReference right, Predicate selectionPredicate);

    TableReference leftOuterJoin(TableReference left, TableReference right, Predicate selectionPredicate);

    TableReference rightOuterJoin(TableReference left, TableReference right, Predicate selectionPredicate);

    SelectionExpressionBuilder getSelectionExpressionBuilder(TableReference tableReference);

    ColumnReference createColumnReference(String columnName);

    ColumnReference createColumnReference(String columnName, String tableName);

    ColumnReference createColumnReference(String columnName, String tableName, String databaseName);

    SelectedItem getAllColumns();

    SelectedItem getAllColumnsFromTable(String tableName);

    SelectedItem getColumnExpression(ColumnExpression columnExpression);


    Predicate getPredicateEmpty();

    Predicate getPredicateIsNull(ColumnReferenceImpl columnReference);

    Predicate getPredicateIsNotNull(ColumnReferenceImpl columnReference);

    Predicate getPredicateAnd(Predicate left, Predicate right);

    Predicate getPredicateOr(Predicate left, Predicate right);

    Predicate getPredicateEquals(Object leftValue, Object rightValue);

    Predicate getPredicateNotEquals(Object leftValue, Object rightValue);

    Predicate getPredicateGreaterThan(Object leftValue, Object rightValue);

    Predicate getPredicateGreaterThanOrEquals(Object leftValue, Object rightValue);

    Predicate getPredicateLessThan(Object leftValue, Object rightValue);

    Predicate getPredicateLessThanOrEquals(Object leftValue, Object rightValue);

    Predicate getPredicateIn(ColumnReferenceImpl columnReference, List<?> values);
}
