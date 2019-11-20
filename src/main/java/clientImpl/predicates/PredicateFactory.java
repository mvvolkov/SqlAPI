package clientImpl.predicates;

import clientImpl.columnExpr.ColumnExprFactory;
import sqlapi.columnExpr.ColumnExpression;
import sqlapi.columnExpr.ColumnRef;
import sqlapi.columnExpr.ColumnValue;
import sqlapi.predicates.CombinedPredicate;
import sqlapi.predicates.Predicate;

import java.util.List;

public class PredicateFactory {

    private PredicateFactory() {
    }

    public static Predicate empty() {
        return new EmptyPredicateImpl();
    }

    public static Predicate isNull(ColumnRef columnRef) {
        return new ColumnIsNullPredicateImpl(columnRef);
    }

    public static Predicate isNull(String tableName, String columnName) {
        return isNull(ColumnExprFactory.columnRef(tableName, columnName));
    }

    public static Predicate isNull(String columnName) {
        return isNull(ColumnExprFactory.columnRef(columnName));
    }

    public static Predicate isNotNull(ColumnRef columnRef) {
        return new ColumnIsNotNullPredicateImpl(columnRef);
    }

    public static Predicate isNotNull(String tableName, String columnName) {
        return isNotNull(ColumnExprFactory.columnRef(tableName, columnName));
    }

    public static Predicate isNotNull(String columnName) {
        return isNotNull(ColumnExprFactory.columnRef(columnName));
    }

    public static Predicate in(ColumnRef columnReference,
                               List<ColumnValue> values) {
        return new ColumnInPredicateImpl(columnReference, values);
    }

    public static Predicate in(String columnName,
                               List<ColumnValue> values) {
        return in(ColumnExprFactory.columnRef(columnName), values);
    }

    public static Predicate in(String tableName, String columnName,
                               List<ColumnValue> values) {
        return in(ColumnExprFactory.columnRef(tableName, columnName), values);
    }

    public static CombinedPredicate and(Predicate left, Predicate right) {
        return new CombinedPredicateImpl(Predicate.Type.AND, left, right);
    }


    public static CombinedPredicate or(Predicate left, Predicate right) {
        return new CombinedPredicateImpl(Predicate.Type.OR, left, right);
    }

    public static Predicate equals(ColumnExpression leftValue,
                                   ColumnExpression rightValue) {
        return new BinaryPredicateImpl(Predicate.Type.EQUALS, leftValue, rightValue);
    }

    public static Predicate equals(String columnName1, String columnName2) {
        return equals(ColumnExprFactory.columnRef(columnName1),
                ColumnExprFactory.columnRef(columnName2));
    }

    public static Predicate equals(ColumnExpression leftValue, String columnName) {
        return equals(leftValue, ColumnExprFactory.columnRef(columnName));
    }

    public static Predicate equals(ColumnExpression leftValue, String tableName, String columnName) {
        return equals(leftValue, ColumnExprFactory.columnRef(tableName, columnName));
    }

    public static Predicate equals(String columnName, ColumnExpression rightValue) {
        return equals(ColumnExprFactory.columnRef(columnName),
                rightValue);
    }

    public static Predicate equals(String tableName, String columnName, ColumnExpression rightValue) {
        return equals(ColumnExprFactory.columnRef(tableName, columnName),
                rightValue);
    }

    public static Predicate equals(String tableName1, String columnName1, String tableName2, String columnName2) {
        return equals(ColumnExprFactory.columnRef(tableName1, columnName1),
                ColumnExprFactory.columnRef(tableName2, columnName2));
    }

    public static Predicate notEquals(ColumnExpression leftValue,
                                      ColumnExpression rightValue) {
        return new BinaryPredicateImpl(Predicate.Type.NOT_EQUALS, leftValue, rightValue);
    }

    public static Predicate notEquals(ColumnExpression leftValue, String columnName) {
        return notEquals(leftValue, ColumnExprFactory.columnRef(columnName));
    }

    public static Predicate notEquals(ColumnExpression leftValue, String tableName, String columnName) {
        return notEquals(leftValue, ColumnExprFactory.columnRef(tableName, columnName));
    }

    public static Predicate notEquals(String columnName,
                                      ColumnExpression rightValue) {
        return notEquals(ColumnExprFactory.columnRef(columnName), rightValue);
    }

    public static Predicate notEquals(String tableName, String columnName,
                                      ColumnExpression rightValue) {
        return notEquals(ColumnExprFactory.columnRef(tableName, columnName), rightValue);
    }

    public static Predicate notEquals(String columnName1, String columnName2) {
        return notEquals(ColumnExprFactory.columnRef(columnName1), ColumnExprFactory.columnRef(columnName2));
    }

    public static Predicate notEquals(String tableName1, String columnName1, String tableName2, String columnName2) {
        return notEquals(ColumnExprFactory.columnRef(tableName1, columnName1),
                ColumnExprFactory.columnRef(tableName2, columnName2));
    }


    public static Predicate greaterThan(ColumnExpression leftValue,
                                        ColumnExpression rightValue) {
        return new BinaryPredicateImpl(Predicate.Type.GREATER_THAN, leftValue,
                rightValue);
    }

    public static Predicate greaterThan(ColumnExpression leftValue, String columnName) {
        return greaterThan(leftValue, ColumnExprFactory.columnRef(columnName));
    }

    public static Predicate greaterThan(ColumnExpression leftValue, String tableName, String columnName) {
        return greaterThan(leftValue, ColumnExprFactory.columnRef(tableName, columnName));
    }

    public static Predicate greaterThan(String columnName,
                                        ColumnExpression rightValue) {
        return greaterThan(ColumnExprFactory.columnRef(columnName), rightValue);
    }

    public static Predicate greaterThan(String tableName, String columnName,
                                        ColumnExpression rightValue) {
        return greaterThan(ColumnExprFactory.columnRef(tableName, columnName), rightValue);
    }

    public static Predicate greaterThan(String columnName1, String columnName2) {
        return greaterThan(ColumnExprFactory.columnRef(columnName1), ColumnExprFactory.columnRef(columnName2));
    }

    public static Predicate greaterThan(String tableName1, String columnName1, String tableName2, String columnName2) {
        return greaterThan(ColumnExprFactory.columnRef(tableName1, columnName1),
                ColumnExprFactory.columnRef(tableName2, columnName2));
    }

    public static Predicate greaterThanOrEquals(ColumnExpression leftValue,
                                                ColumnExpression rightValue) {
        return new BinaryPredicateImpl(Predicate.Type.GREATER_THAN_OR_EQUALS, leftValue,
                rightValue);
    }

    public static Predicate greaterThanOrEquals(ColumnExpression leftValue, String columnName) {
        return greaterThanOrEquals(leftValue, ColumnExprFactory.columnRef(columnName));
    }

    public static Predicate greaterThanOrEquals(ColumnExpression leftValue, String tableName, String columnName) {
        return greaterThanOrEquals(leftValue, ColumnExprFactory.columnRef(tableName, columnName));
    }

    public static Predicate greaterThanOrEquals(String columnName,
                                                ColumnExpression rightValue) {
        return greaterThanOrEquals(ColumnExprFactory.columnRef(columnName), rightValue);
    }

    public static Predicate greaterThanOrEquals(String tableName, String columnName,
                                                ColumnExpression rightValue) {
        return greaterThanOrEquals(ColumnExprFactory.columnRef(tableName, columnName), rightValue);
    }

    public static Predicate greaterThanOrEquals(String columnName1, String columnName2) {
        return greaterThanOrEquals(ColumnExprFactory.columnRef(columnName1), ColumnExprFactory.columnRef(columnName2));
    }

    public static Predicate greaterThanOrEquals(String tableName1, String columnName1, String tableName2, String columnName2) {
        return greaterThanOrEquals(ColumnExprFactory.columnRef(tableName1, columnName1),
                ColumnExprFactory.columnRef(tableName2, columnName2));
    }

    public static Predicate lessThan(ColumnExpression leftValue,
                                     ColumnExpression rightValue) {
        return new BinaryPredicateImpl(Predicate.Type.LESS_THAN, leftValue, rightValue);
    }

    public static Predicate lessThan(ColumnExpression leftValue, String columnName) {
        return lessThan(leftValue, ColumnExprFactory.columnRef(columnName));
    }

    public static Predicate lessThan(ColumnExpression leftValue, String tableName, String columnName) {
        return lessThan(leftValue, ColumnExprFactory.columnRef(tableName, columnName));
    }

    public static Predicate lessThan(String columnName,
                                     ColumnExpression rightValue) {
        return lessThan(ColumnExprFactory.columnRef(columnName), rightValue);
    }

    public static Predicate lessThan(String tableName, String columnName,
                                     ColumnExpression rightValue) {
        return lessThan(ColumnExprFactory.columnRef(tableName, columnName), rightValue);
    }

    public static Predicate lessThan(String columnName1, String columnName2) {
        return lessThan(ColumnExprFactory.columnRef(columnName1), ColumnExprFactory.columnRef(columnName2));
    }

    public static Predicate lessThan(String tableName1, String columnName1, String tableName2, String columnName2) {
        return lessThan(ColumnExprFactory.columnRef(tableName1, columnName1),
                ColumnExprFactory.columnRef(tableName2, columnName2));
    }

    public static Predicate lessThanOrEquals(ColumnExpression leftValue,
                                             ColumnExpression rightValue) {
        return new BinaryPredicateImpl(Predicate.Type.LESS_THAN_OR_EQUALS, leftValue,
                rightValue);
    }

    public static Predicate lessThanOrEquals(ColumnExpression leftValue, String columnName) {
        return lessThanOrEquals(leftValue, ColumnExprFactory.columnRef(columnName));
    }

    public static Predicate lessThanOrEquals(ColumnExpression leftValue, String tableName, String columnName) {
        return lessThanOrEquals(leftValue, ColumnExprFactory.columnRef(tableName, columnName));
    }

    public static Predicate lessThanOrEquals(String columnName,
                                             ColumnExpression rightValue) {
        return lessThanOrEquals(ColumnExprFactory.columnRef(columnName), rightValue);
    }

    public static Predicate lessThanOrEquals(String tableName, String columnName,
                                             ColumnExpression rightValue) {
        return lessThanOrEquals(ColumnExprFactory.columnRef(tableName, columnName), rightValue);
    }

    public static Predicate lessThanOrEquals(String columnName1, String columnName2) {
        return lessThanOrEquals(ColumnExprFactory.columnRef(columnName1), ColumnExprFactory.columnRef(columnName2));
    }

    public static Predicate lessThanOrEquals(String tableName1, String columnName1, String tableName2, String columnName2) {
        return lessThanOrEquals(ColumnExprFactory.columnRef(tableName1, columnName1),
                ColumnExprFactory.columnRef(tableName2, columnName2));
    }
}
