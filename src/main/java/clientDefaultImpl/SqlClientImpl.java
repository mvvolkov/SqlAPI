package clientDefaultImpl;

import api.*;
import api.selectionPredicate.Predicate;

import java.util.List;

public class SqlClientImpl implements SqlClient {

    @Override
    public TableMetadata tableMetadata(String tableName, List<ColumnMetadata> columnsMetadata) {
        return new TableMetadataImpl(tableName, columnsMetadata);
    }

    @Override
    public ColumnMetadataBuilder getIntegerColumnMetadataBuilder(String columnName) {
        return IntegerColumnMetadataImpl.builder(columnName);
    }

    @Override
    public ColumnMetadataBuilder getVarcharColumnMetadataBuilder(String columnName, int maxLength) {
        return VarcharColumnMetadataImpl.builder(columnName, maxLength);
    }

    @Override
    public TableReference baseTableRef(String tableName, String databaseName) {
        return new BaseTableReferenceImpl(tableName, databaseName);
    }

    @Override
    public TableReference innerJoin(TableReference left, TableReference right, Predicate selectionPredicate) {
        return new JoinTableReferenceImpl(JoinTableReference.JoinType.INNER_JOIN, left, right, selectionPredicate);
    }


    @Override
    public TableReference leftOuterJoin(TableReference left, TableReference right, Predicate selectionPredicate) {
        return new JoinTableReferenceImpl(JoinTableReference.JoinType.LEFT_OUTER_JOIN, left, right, selectionPredicate);
    }

    @Override
    public TableReference rightOuterJoin(TableReference left, TableReference right, Predicate selectionPredicate) {
        return new JoinTableReferenceImpl(JoinTableReference.JoinType.RIGHT_OUTER_JOIN, left, right, selectionPredicate);
    }

    @Override
    public SelectionExpressionBuilder getSelectionExpressionBuilder(TableReference tableReference) {
        return SelectExpressionImpl.builder(tableReference);
    }

    @Override
    public ColumnReference createColumnReference(String columnName) {
        return new ColumnReferenceImpl(columnName);
    }

    @Override
    public ColumnReference createColumnReference(String columnName, String tableName) {
        return new ColumnReferenceImpl(columnName, tableName);
    }

    @Override
    public ColumnReference createColumnReference(String columnName, String tableName, String databaseName) {
        return new ColumnReferenceImpl(columnName, tableName, databaseName);
    }

    @Override
    public Predicate getPredicateEmpty() {
        return new SelectionPredicateImpl(Predicate.Type.TRUE);
    }

    @Override
    public Predicate getPredicateIsNull(ColumnReferenceImpl columnReference) {
        return new ColumnNullPredicateImpl(Predicate.Type.IS_NULL, columnReference);
    }

    @Override
    public Predicate getPredicateIsNotNull(ColumnReferenceImpl columnReference) {
        return new ColumnNullPredicateImpl(Predicate.Type.IS_NOT_NULL, columnReference);
    }

    @Override
    public Predicate getPredicateAnd(Predicate left, Predicate right) {
        return new CombinedPredicateImpl(Predicate.Type.AND, left, right);
    }

    @Override
    public Predicate getPredicateOr(Predicate left, Predicate right) {
        return new CombinedPredicateImpl(Predicate.Type.OR, left, right);
    }

    private Predicate getBinaryPredicate(Predicate.Type type, Object leftValue, Object rightValue) {
        if (leftValue instanceof ColumnReferenceImpl) {
            if (rightValue instanceof ColumnReferenceImpl) {
                return new ColumnColumnsPredicateImpl(type, (ColumnReferenceImpl) leftValue, (ColumnReferenceImpl) rightValue);
            }
            return new ColumnValuePredicateImpl(type, (ColumnReferenceImpl) leftValue, (Comparable) rightValue);
        }
        if (rightValue instanceof ColumnReferenceImpl) {
            return new ColumnValuePredicateImpl(type, (ColumnReferenceImpl) rightValue, (Comparable) leftValue);
        }
        Predicate.Type newType = leftValue.equals(rightValue) ? Predicate.Type.TRUE : Predicate.Type.FALSE;
        return new SelectionPredicateImpl(newType);
    }

    @Override
    public Predicate getPredicateEquals(Object leftValue, Object rightValue) {
        return this.getBinaryPredicate(Predicate.Type.EQUALS, leftValue, rightValue);
    }

    @Override
    public Predicate getPredicateNotEquals(Object leftValue, Object rightValue) {
        return this.getBinaryPredicate(Predicate.Type.NOT_EQUALS, leftValue, rightValue);
    }

    @Override
    public Predicate getPredicateGreaterThan(Object leftValue, Object rightValue) {
        return this.getBinaryPredicate(Predicate.Type.GREATER_THAN, leftValue, rightValue);
    }

    @Override
    public Predicate getPredicateGreaterThanOrEquals(Object leftValue, Object rightValue) {
        return this.getBinaryPredicate(Predicate.Type.GREATER_THAN_OR_EQUALS, leftValue, rightValue);
    }

    @Override
    public Predicate getPredicateLessThan(Object leftValue, Object rightValue) {
        return this.getBinaryPredicate(Predicate.Type.LESS_THAN, leftValue, rightValue);
    }

    @Override
    public Predicate getPredicateLessThanOrEquals(Object leftValue, Object rightValue) {
        return this.getBinaryPredicate(Predicate.Type.LESS_THAN_OR_EQUALS, leftValue, rightValue);
    }

    @Override
    public Predicate getPredicateIn(ColumnReferenceImpl columnReference, List<?> values) {
        return new ColumnInPredicate(columnReference, values);
    }
}
