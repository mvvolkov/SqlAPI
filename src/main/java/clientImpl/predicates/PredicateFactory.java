package clientImpl.predicates;

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

    public static Predicate isNotNull(ColumnRef columnRef) {
        return new ColumnIsNotNullPredicateImpl(columnRef);
    }

    public static Predicate in(ColumnRef columnReference,
                               List<ColumnValue> values) {
        return new ColumnInPredicateImpl(columnReference, values);
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

    public static Predicate notEquals(ColumnExpression leftValue,
                                      ColumnExpression rightValue) {
        return new BinaryPredicateImpl(Predicate.Type.NOT_EQUALS, leftValue, rightValue);
    }

    public static Predicate greaterThan(ColumnExpression leftValue,
                                        ColumnExpression rightValue) {
        return new BinaryPredicateImpl(Predicate.Type.GREATER_THAN, leftValue,
                rightValue);
    }

    public static Predicate greaterThanOrEquals(ColumnExpression leftValue,
                                                ColumnExpression rightValue) {
        return new BinaryPredicateImpl(Predicate.Type.GREATER_THAN_OR_EQUALS, leftValue,
                rightValue);
    }

    public static Predicate lessThan(ColumnExpression leftValue,
                                     ColumnExpression rightValue) {
        return new BinaryPredicateImpl(Predicate.Type.LESS_THAN, leftValue, rightValue);
    }

    public static Predicate lessThanOrEquals(ColumnExpression leftValue,
                                             ColumnExpression rightValue) {
        return new BinaryPredicateImpl(Predicate.Type.LESS_THAN_OR_EQUALS, leftValue,
                rightValue);
    }
}
