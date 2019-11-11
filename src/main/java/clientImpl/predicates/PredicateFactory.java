package clientImpl.predicates;

import api.columnExpr.ColumnExpression;
import api.columnExpr.ColumnRef;
import api.columnExpr.ColumnValue;
import api.predicates.CombinedPredicate;
import api.predicates.Predicate;

import java.util.List;

public class PredicateFactory {

    private PredicateFactory() {
    }

    public static Predicate empty() {
        return new PredicateImpl(Predicate.Type.TRUE);
    }


//    public static ColumnValuePredicate isNull(ColumnRef columnReference) {
//        return new ColumnValuePredicateImpl(Predicate.Type.EQUALS, columnReference,
//                ColumnExprFactory.nullValue());
//    }
//
//
//    public static ColumnValuePredicate isNotNull(ColumnRef columnReference) {
//        return new ColumnValuePredicateImpl(Predicate.Type.NOT_EQUALS, columnReference,
//                ColumnExprFactory.nullValue());
//    }


    public static CombinedPredicate and(Predicate left, Predicate right) {
        return new CombinedPredicateImpl(Predicate.Type.AND, left, right);
    }


    public static CombinedPredicate or(Predicate left, Predicate right) {
        return new CombinedPredicateImpl(Predicate.Type.OR, left, right);
    }


//    private static Predicate getBinaryPredicate(Predicate.Type type, ColumnExpression leftValue,
//                                                ColumnExpression rightValue) {
//
//        return new BinaryPredicateImpl(type, leftValue, rightValue);
//    }


    public static Predicate equals(ColumnExpression leftValue, ColumnExpression rightValue) {
        return new BinaryPredicateImpl(Predicate.Type.EQUALS, leftValue, rightValue);
    }


    public static Predicate notEquals(ColumnExpression leftValue, ColumnExpression rightValue) {
        return new BinaryPredicateImpl(Predicate.Type.NOT_EQUALS, leftValue, rightValue);
    }


    public static Predicate greaterThan(ColumnExpression leftValue, ColumnExpression rightValue) {
        return new BinaryPredicateImpl(Predicate.Type.GREATER_THAN, leftValue, rightValue);
    }


    public static Predicate greaterThanOrEquals(ColumnExpression leftValue, ColumnExpression rightValue) {
        return new BinaryPredicateImpl(Predicate.Type.GREATER_THAN_OR_EQUALS, leftValue, rightValue);
    }


    public static Predicate lessThan(ColumnExpression leftValue, ColumnExpression rightValue) {
        return new BinaryPredicateImpl(Predicate.Type.LESS_THAN, leftValue, rightValue);
    }


    public static Predicate lessThanOrEquals(ColumnExpression leftValue,
                                             ColumnExpression rightValue) {
        return new BinaryPredicateImpl(Predicate.Type.LESS_THAN_OR_EQUALS, leftValue, rightValue);
    }


    public static Predicate in(ColumnRef columnReference,
                               List<ColumnValue> values) {
        return new ColumnInPredicateImpl(columnReference, values);
    }
}
