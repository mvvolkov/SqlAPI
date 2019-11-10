package clientImpl.predicates;

import api.columnExpr.ColumnRef;
import api.columnExpr.ColumnValue;
import api.predicates.ColumnValuePredicate;
import api.predicates.CombinedPredicate;
import api.predicates.Predicate;
import clientImpl.columnExpr.ColumnExprFactory;
import clientImpl.columnExpr.ColumnValueImpl;

import java.util.List;

public class PredicateFactory {

    private PredicateFactory() {
    }

    public static Predicate empty() {
        return new SelectionPredicateImpl(Predicate.Type.TRUE);
    }


    public static ColumnValuePredicate isNull(ColumnRef columnReference) {
        return new ColumnValuePredicateImpl(Predicate.Type.EQUALS, columnReference,
                ColumnExprFactory.nullValue());
    }


    public static ColumnValuePredicate isNotNull(ColumnRef columnReference) {
        return new ColumnValuePredicateImpl(Predicate.Type.NOT_EQUALS, columnReference,
                ColumnExprFactory.nullValue());
    }


    public static CombinedPredicate and(Predicate left, Predicate right) {
        return new CombinedPredicateImpl(Predicate.Type.AND, left, right);
    }


    public static CombinedPredicate or(Predicate left, Predicate right) {
        return new CombinedPredicateImpl(Predicate.Type.OR, left, right);
    }

    private static void checkBinaryPredicateOperandType(Object value) {
        if (!(value instanceof ColumnRef) && !(value instanceof ColumnValue) &&
                value != null) {
            throw new IllegalArgumentException();
        }
    }

    private static Predicate getBinaryPredicate(Predicate.Type type, Object leftValue,
                                                Object rightValue) {
        checkBinaryPredicateOperandType(leftValue);
        checkBinaryPredicateOperandType(rightValue);

        if (leftValue instanceof ColumnRef) {
            if (rightValue instanceof ColumnRef) {
                return new ColumnColumnsPredicateImpl(type,
                        (ColumnRef) leftValue,
                        (ColumnRef) rightValue);
            }
            return new ColumnValuePredicateImpl(type, (ColumnRef) leftValue,
                    (ColumnValue) rightValue);
        }
        if (rightValue instanceof ColumnRef) {
            return new ColumnValuePredicateImpl(type, (ColumnRef) rightValue,
                    (ColumnValue) leftValue);
        }
        Predicate.Type newType =
                leftValue.equals(rightValue) ? Predicate.Type.TRUE : Predicate.Type.FALSE;
        return new SelectionPredicateImpl(newType);
    }


    public static Predicate equals(Object leftValue, Object rightValue) {
        return getBinaryPredicate(Predicate.Type.EQUALS, leftValue, rightValue);
    }


    public static Predicate notEquals(Object leftValue, Object rightValue) {
        return getBinaryPredicate(Predicate.Type.NOT_EQUALS, leftValue, rightValue);
    }


    public static Predicate greaterThan(Object leftValue, Object rightValue) {
        return getBinaryPredicate(Predicate.Type.GREATER_THAN, leftValue, rightValue);
    }


    public static Predicate greaterThanOrEquals(Object leftValue, Object rightValue) {
        return getBinaryPredicate(Predicate.Type.GREATER_THAN_OR_EQUALS, leftValue,
                rightValue);
    }


    public static Predicate lessThan(Object leftValue, Object rightValue) {
        return getBinaryPredicate(Predicate.Type.LESS_THAN, leftValue, rightValue);
    }


    public static Predicate lessThanOrEquals(Object leftValue,
                                             Object rightValue) {
        return getBinaryPredicate(Predicate.Type.LESS_THAN_OR_EQUALS, leftValue,
                rightValue);
    }


    public static Predicate in(ColumnRef columnReference,
                               List<ColumnValue> values) {
        return new ColumnInPredicateImpl(columnReference, values);
    }
}
