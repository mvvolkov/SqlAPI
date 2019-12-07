package clientImpl.stringUtil;

import sqlapi.exceptions.UnsupportedPredicateTypeException;
import sqlapi.predicates.*;
import sqlapi.predicates.binaryPredicate.*;
import sqlapi.predicates.combined.AndPredicate;
import sqlapi.predicates.combined.CombinedPredicate;
import sqlapi.predicates.combined.OrPredicate;

import java.util.stream.Collectors;

public class PredicateStringUtil {

    private PredicateStringUtil() {
    }

    public static String getPredicateString(Predicate predicate) throws UnsupportedPredicateTypeException {
        if (predicate instanceof BinaryPredicate) {
            return getBinaryPredicateString((BinaryPredicate) predicate);
        }
        if (predicate instanceof CombinedPredicate) {
            return getCombinedPredicateString((CombinedPredicate) predicate);
        }
        if (predicate instanceof EmptyPredicate) {
            return "";
        }
        if (predicate instanceof ColumnInPredicate) {
            return getColumnInPredicateString((ColumnInPredicate) predicate);
        }
        if (predicate instanceof ColumnIsNullPredicate) {
            return getColumnIsNullPredicateString((ColumnIsNullPredicate) predicate);
        }
        if (predicate instanceof ColumnIsNotNullPredicate) {
            return getColumnIsNotNullPredicateString((ColumnIsNotNullPredicate) predicate);
        }
        throw new UnsupportedPredicateTypeException(predicate);
    }

    public static String getCombinedPredicateString(CombinedPredicate predicate) throws UnsupportedPredicateTypeException {
        return "(" + getPredicateString(predicate.getLeftPredicate()) + ") "
                + getCombinedPredicateOperatorString(predicate) + " (" +
                getPredicateString(predicate.getRightPredicate()) + ")";
    }

    private static String getCombinedPredicateOperatorString(CombinedPredicate predicate) throws UnsupportedPredicateTypeException {
        if (predicate instanceof AndPredicate) {
            return "AND";
        }
        if (predicate instanceof OrPredicate) {
            return "OR";
        }
        throw new UnsupportedPredicateTypeException(predicate);
    }


    public static String getColumnIsNullPredicateString(ColumnIsNullPredicate predicate) {
        return ColumnExprStringUtil.getColumnRefString(predicate.getColumnRef()) + " IS NULL";
    }

    public static String getColumnIsNotNullPredicateString(ColumnIsNotNullPredicate predicate) {
        return ColumnExprStringUtil.getColumnRefString(predicate.getColumnRef()) + " IS NOT NULL";
    }


    public static String getColumnInPredicateString(ColumnInPredicate predicate) {
        return ColumnExprStringUtil.getColumnRefString(predicate.getColumnRef())
                + " IN (" + predicate.getColumnValues().stream().map(ColumnExprStringUtil::getInputValueString)
                .collect(Collectors.joining(", ")) + ")";
    }

    public static String getBinaryPredicateString(BinaryPredicate bp) throws UnsupportedPredicateTypeException {
        return bp.getLeftOperand() + " " + getBinaryOperatorString(bp) + " " + bp.getRightOperand();
    }

    private static String getBinaryOperatorString(BinaryPredicate bp) throws UnsupportedPredicateTypeException {
        if (bp instanceof EqualsPredicate) {
            return "=";
        }
        if (bp instanceof NotEqualsPredicate) {
            return "!=";
        }
        if (bp instanceof GreaterThanPredicate) {
            return ">";
        }
        if (bp instanceof GreaterThanOrEqualsPredicate) {
            return ">=";
        }
        if (bp instanceof LessThanPredicate) {
            return "<";
        }
        if (bp instanceof LessThanOrEqualsPredicate) {
            return "<=";
        }
        throw new UnsupportedPredicateTypeException(bp);
    }
}
