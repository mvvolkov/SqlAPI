package clientImpl.predicates;

import clientImpl.columnExpr.ColumnExprFactory;
import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnExpression;
import sqlapi.columnExpr.ColumnRef;
import sqlapi.columnExpr.InputValue;
import sqlapi.predicates.combined.CombinedPredicate;
import sqlapi.predicates.Predicate;

import java.util.List;

public class PredicateFactory {

    private PredicateFactory() {
    }

    public static @NotNull Predicate empty() {
        return new EmptyPredicateImpl();
    }

    public static @NotNull Predicate isNull(@NotNull ColumnRef columnRef) {
        return new ColumnIsNullPredicateImpl(columnRef);
    }

    public static @NotNull Predicate isNull(@NotNull String tableName, @NotNull String columnName) {
        return isNull(ColumnExprFactory.columnRef(tableName, columnName));
    }

    public static @NotNull Predicate isNull(@NotNull String columnName) {
        return isNull(ColumnExprFactory.columnRef(columnName));
    }

    public static @NotNull Predicate isNotNull(@NotNull ColumnRef columnRef) {
        return new ColumnIsNotNullPredicateImpl(columnRef);
    }

    public static @NotNull Predicate isNotNull(@NotNull String tableName, @NotNull String columnName) {
        return isNotNull(ColumnExprFactory.columnRef(tableName, columnName));
    }

    public static @NotNull Predicate isNotNull(@NotNull String columnName) {
        return isNotNull(ColumnExprFactory.columnRef(columnName));
    }

    public static @NotNull Predicate in(@NotNull ColumnRef columnReference, @NotNull List<InputValue> values) {
        return new ColumnInPredicateImpl(columnReference, values);
    }

    public static @NotNull Predicate in(@NotNull String columnName,
                                        @NotNull List<InputValue> values) {
        return in(ColumnExprFactory.columnRef(columnName), values);
    }

    public static @NotNull Predicate in(@NotNull String tableName, @NotNull String columnName,
                                        @NotNull List<InputValue> values) {
        return in(ColumnExprFactory.columnRef(tableName, columnName), values);
    }

    public static @NotNull CombinedPredicate and(@NotNull Predicate left, @NotNull Predicate right) {
        return new AndPredicateImpl(left, right);
    }


    public static @NotNull CombinedPredicate or(@NotNull Predicate left, @NotNull Predicate right) {
        return new OrPredicateImpl(left, right);
    }

    public static @NotNull Predicate equals(@NotNull ColumnExpression leftValue, @NotNull ColumnExpression rightValue) {
        return new EqualsPredicateImpl(leftValue, rightValue);
    }

    public static @NotNull Predicate equals(@NotNull String columnName1, @NotNull String columnName2) {
        return equals(ColumnExprFactory.columnRef(columnName1),
                ColumnExprFactory.columnRef(columnName2));
    }

    public static @NotNull Predicate equals(@NotNull ColumnExpression leftValue, @NotNull String columnName) {
        return equals(leftValue, ColumnExprFactory.columnRef(columnName));
    }

    public static @NotNull Predicate equals(@NotNull ColumnExpression leftValue, @NotNull String tableName, @NotNull String columnName) {
        return equals(leftValue, ColumnExprFactory.columnRef(tableName, columnName));
    }

    public static @NotNull Predicate equals(@NotNull String columnName, @NotNull ColumnExpression rightValue) {
        return equals(ColumnExprFactory.columnRef(columnName),
                rightValue);
    }

    public static @NotNull Predicate equals(@NotNull String tableName, @NotNull String columnName, @NotNull ColumnExpression rightValue) {
        return equals(ColumnExprFactory.columnRef(tableName, columnName),
                rightValue);
    }

    public static Predicate equals(@NotNull String tableName1, @NotNull String columnName1, @NotNull String tableName2, @NotNull String columnName2) {
        return equals(ColumnExprFactory.columnRef(tableName1, columnName1),
                ColumnExprFactory.columnRef(tableName2, columnName2));
    }

    public static @NotNull Predicate notEquals(@NotNull ColumnExpression leftValue,
                                               @NotNull ColumnExpression rightValue) {
        return new NotEqualsPredicateImpl(leftValue, rightValue);
    }

    public static @NotNull Predicate notEquals(@NotNull ColumnExpression leftValue, @NotNull String columnName) {
        return notEquals(leftValue, ColumnExprFactory.columnRef(columnName));
    }

    public static @NotNull Predicate notEquals(@NotNull ColumnExpression leftValue, @NotNull String tableName,
                                               @NotNull String columnName) {
        return notEquals(leftValue, ColumnExprFactory.columnRef(tableName, columnName));
    }

    public static @NotNull Predicate notEquals(@NotNull String columnName,
                                               @NotNull ColumnExpression rightValue) {
        return notEquals(ColumnExprFactory.columnRef(columnName), rightValue);
    }

    public static @NotNull Predicate notEquals(@NotNull String tableName, @NotNull String columnName,
                                               @NotNull ColumnExpression rightValue) {
        return notEquals(ColumnExprFactory.columnRef(tableName, columnName), rightValue);
    }

    public static @NotNull Predicate notEquals(@NotNull String columnName1, @NotNull String columnName2) {
        return notEquals(ColumnExprFactory.columnRef(columnName1), ColumnExprFactory.columnRef(columnName2));
    }

    public static @NotNull Predicate notEquals(@NotNull String tableName1, @NotNull String columnName1,
                                               @NotNull String tableName2, @NotNull String columnName2) {
        return notEquals(ColumnExprFactory.columnRef(tableName1, columnName1),
                ColumnExprFactory.columnRef(tableName2, columnName2));
    }


    public static @NotNull Predicate greaterThan(@NotNull ColumnExpression leftValue,
                                                 @NotNull ColumnExpression rightValue) {
        return new GreaterThanPredicateImpl(leftValue,
                rightValue);
    }

    public static @NotNull Predicate greaterThan(@NotNull ColumnExpression leftValue, @NotNull String columnName) {
        return greaterThan(leftValue, ColumnExprFactory.columnRef(columnName));
    }

    public static @NotNull Predicate greaterThan(@NotNull ColumnExpression leftValue, @NotNull String tableName,
                                                 @NotNull String columnName) {
        return greaterThan(leftValue, ColumnExprFactory.columnRef(tableName, columnName));
    }

    public static @NotNull Predicate greaterThan(@NotNull String columnName,
                                                 @NotNull ColumnExpression rightValue) {
        return greaterThan(ColumnExprFactory.columnRef(columnName), rightValue);
    }

    public static @NotNull Predicate greaterThan(@NotNull String tableName, @NotNull String columnName,
                                                 @NotNull ColumnExpression rightValue) {
        return greaterThan(ColumnExprFactory.columnRef(tableName, columnName), rightValue);
    }

    public static @NotNull Predicate greaterThan(@NotNull String columnName1, @NotNull String columnName2) {
        return greaterThan(ColumnExprFactory.columnRef(columnName1), ColumnExprFactory.columnRef(columnName2));
    }

    public static @NotNull Predicate greaterThan(@NotNull String tableName1, @NotNull String columnName1,
                                                 @NotNull String tableName2, @NotNull String columnName2) {
        return greaterThan(ColumnExprFactory.columnRef(tableName1, columnName1),
                ColumnExprFactory.columnRef(tableName2, columnName2));
    }

    public static @NotNull Predicate greaterThanOrEquals(@NotNull ColumnExpression leftValue,
                                                         @NotNull ColumnExpression rightValue) {
        return new GreaterThanOrEqualsPredicateImpl(leftValue, rightValue);
    }

    public static @NotNull Predicate greaterThanOrEquals(@NotNull ColumnExpression leftValue, @NotNull String columnName) {
        return greaterThanOrEquals(leftValue, ColumnExprFactory.columnRef(columnName));
    }

    public static @NotNull Predicate greaterThanOrEquals(@NotNull ColumnExpression leftValue, @NotNull String tableName,
                                                         @NotNull String columnName) {
        return greaterThanOrEquals(leftValue, ColumnExprFactory.columnRef(tableName, columnName));
    }

    public static @NotNull Predicate greaterThanOrEquals(@NotNull String columnName,
                                                         @NotNull ColumnExpression rightValue) {
        return greaterThanOrEquals(ColumnExprFactory.columnRef(columnName), rightValue);
    }

    public static @NotNull Predicate greaterThanOrEquals(@NotNull String tableName, @NotNull String columnName,
                                                         ColumnExpression rightValue) {
        return greaterThanOrEquals(ColumnExprFactory.columnRef(tableName, columnName), rightValue);
    }

    public static @NotNull Predicate greaterThanOrEquals(@NotNull String columnName1, @NotNull String columnName2) {
        return greaterThanOrEquals(ColumnExprFactory.columnRef(columnName1), ColumnExprFactory.columnRef(columnName2));
    }

    public static @NotNull Predicate greaterThanOrEquals(@NotNull String tableName1, @NotNull String columnName1,
                                                         @NotNull String tableName2, @NotNull String columnName2) {
        return greaterThanOrEquals(ColumnExprFactory.columnRef(tableName1, columnName1),
                ColumnExprFactory.columnRef(tableName2, columnName2));
    }

    public static @NotNull Predicate lessThan(@NotNull ColumnExpression leftValue,
                                              @NotNull ColumnExpression rightValue) {
        return new LessThanPredicateImpl(leftValue, rightValue);
    }

    public static @NotNull Predicate lessThan(@NotNull ColumnExpression leftValue, @NotNull String columnName) {
        return lessThan(leftValue, ColumnExprFactory.columnRef(columnName));
    }

    public static @NotNull Predicate lessThan(@NotNull ColumnExpression leftValue, @NotNull String tableName, @NotNull String columnName) {
        return lessThan(leftValue, ColumnExprFactory.columnRef(tableName, columnName));
    }

    public static @NotNull Predicate lessThan(@NotNull String columnName,
                                              @NotNull ColumnExpression rightValue) {
        return lessThan(ColumnExprFactory.columnRef(columnName), rightValue);
    }

    public static @NotNull Predicate lessThan(@NotNull String tableName, @NotNull String columnName,
                                              @NotNull ColumnExpression rightValue) {
        return lessThan(ColumnExprFactory.columnRef(tableName, columnName), rightValue);
    }

    public static @NotNull Predicate lessThan(@NotNull String columnName1, @NotNull String columnName2) {
        return lessThan(ColumnExprFactory.columnRef(columnName1), ColumnExprFactory.columnRef(columnName2));
    }

    public static @NotNull Predicate lessThan(@NotNull String tableName1, @NotNull String columnName1,
                                              @NotNull String tableName2, @NotNull String columnName2) {
        return lessThan(ColumnExprFactory.columnRef(tableName1, columnName1),
                ColumnExprFactory.columnRef(tableName2, columnName2));
    }

    public static @NotNull Predicate lessThanOrEquals(@NotNull ColumnExpression leftValue,
                                                      @NotNull ColumnExpression rightValue) {
        return new LessThanOrEqualsPredicateImpl(leftValue, rightValue);
    }

    public static @NotNull Predicate lessThanOrEquals(@NotNull ColumnExpression leftValue, @NotNull String columnName) {
        return lessThanOrEquals(leftValue, ColumnExprFactory.columnRef(columnName));
    }

    public static @NotNull Predicate lessThanOrEquals(@NotNull ColumnExpression leftValue,
                                                      @NotNull String tableName, @NotNull String columnName) {
        return lessThanOrEquals(leftValue, ColumnExprFactory.columnRef(tableName, columnName));
    }

    public static @NotNull Predicate lessThanOrEquals(@NotNull String columnName,
                                                      @NotNull ColumnExpression rightValue) {
        return lessThanOrEquals(ColumnExprFactory.columnRef(columnName), rightValue);
    }

    public static @NotNull Predicate lessThanOrEquals(@NotNull String tableName, @NotNull String columnName,
                                                      ColumnExpression rightValue) {
        return lessThanOrEquals(ColumnExprFactory.columnRef(tableName, columnName), rightValue);
    }

    public static @NotNull Predicate lessThanOrEquals(@NotNull String columnName1, @NotNull String columnName2) {
        return lessThanOrEquals(ColumnExprFactory.columnRef(columnName1), ColumnExprFactory.columnRef(columnName2));
    }

    public static @NotNull Predicate lessThanOrEquals(@NotNull String tableName1, @NotNull String columnName1,
                                                      @NotNull String tableName2, @NotNull String columnName2) {
        return lessThanOrEquals(ColumnExprFactory.columnRef(tableName1, columnName1),
                ColumnExprFactory.columnRef(tableName2, columnName2));
    }
}
