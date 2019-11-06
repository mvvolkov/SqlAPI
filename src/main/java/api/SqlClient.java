package api;

import api.selectionPredicate.Predicate;
import clientDefaultImpl.ColumnReferenceImpl;

import java.util.List;

public interface SqlClient {

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
