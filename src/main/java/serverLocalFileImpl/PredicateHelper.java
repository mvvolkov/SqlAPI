package serverLocalFileImpl;

import api.columnExpr.BinaryColumnExpression;
import api.columnExpr.ColumnExpression;
import api.columnExpr.ColumnRef;
import api.columnExpr.ColumnValue;
import api.exceptions.NoSuchColumnException;
import api.exceptions.WrongValueTypeException;
import api.predicates.BinaryPredicate;
import api.predicates.CombinedPredicate;
import api.predicates.Predicate;

public class PredicateHelper {

    private PredicateHelper() {
    }

    public static boolean matchRow(InternalResultRow resultRow, Predicate predicate)
            throws NoSuchColumnException, WrongValueTypeException {

        if (predicate.getType() == Predicate.Type.TRUE) {
            return true;
        } else if (predicate.getType() == Predicate.Type.FALSE) {
            return false;
        }
        if (predicate instanceof CombinedPredicate) {
            switch (predicate.getType()) {
                case AND:
                    CombinedPredicate cp1 = (CombinedPredicate) predicate;
                    return matchRow(resultRow, cp1.getLeftPredicate()) &&
                            matchRow(resultRow, cp1.getRightPredicate());
                case OR:
                    CombinedPredicate cp2 = (CombinedPredicate) predicate;
                    return matchRow(resultRow, cp2.getLeftPredicate()) ||
                            matchRow(resultRow, cp2.getRightPredicate());
            }
        }
        if (predicate instanceof BinaryPredicate) {
            return matchRow(resultRow, (BinaryPredicate) predicate);
        }
        return false;
    }

    private static boolean matchRow(InternalResultRow resultRow, BinaryPredicate predicate)
            throws WrongValueTypeException, NoSuchColumnException {


        Comparable leftValue =
                (Comparable) evaluateColumnExpr(resultRow, predicate.getLeftOperand());
        Comparable rightValue =
                (Comparable) evaluateColumnExpr(resultRow, predicate.getRightOperand());


        if (leftValue == null || rightValue == null) {
            return false;
        }

//        if (!leftValue.getClass().equals(rightValue.getClass())) {
//            throw new WrongValueTypeException(cr, leftValue.getClass(),
//                    rightValue.getClass());
//        }


        int compResult = leftValue.compareTo(rightValue);
        switch (predicate.getType()) {
            case EQUALS:
                return compResult == 0;
            case NOT_EQUALS:
                return compResult != 0;
            case GREATER_THAN:
                return compResult > 0;
            case GREATER_THAN_OR_EQUALS:
                return compResult >= 0;
            case LESS_THAN:
                return compResult < 0;
            case LESS_THAN_OR_EQUALS:
                return compResult <= 0;
            default:
                return false;
        }
    }

    public static Object evaluateColumnExpr(InternalResultRow row,
                                             ColumnExpression ce) {

        if (ce instanceof BinaryColumnExpression) {
            return evaluateBinaryColumnExpr(row, (BinaryColumnExpression) ce);
        }
        if (ce instanceof ColumnRef) {
            return evaluateColumnRef(row, (ColumnRef) ce);
        }
        if (ce instanceof ColumnValue) {
            return ((ColumnValue) ce).getValue();
        }
        return null;
    }

    private static Object evaluateBinaryColumnExpr(InternalResultRow row,
                                                   BinaryColumnExpression bce) {
        Object leftValue = evaluateColumnExpr(row, bce.getLeftOperand());
        Object rightValue = evaluateColumnExpr(row, bce.getRightOperand());
        switch (bce.getExprType()) {
            case SUM:
                if (leftValue instanceof Integer && rightValue instanceof Integer) {
                    return (Integer) (((Integer) leftValue) + ((Integer) rightValue));
                }
                if (leftValue instanceof String && rightValue instanceof String) {
                    return (String) (((String) leftValue) + ((String) rightValue));
                }
            case DIFF:
                if (leftValue instanceof Integer && rightValue instanceof Integer) {
                    return (Integer) (((Integer) leftValue) - ((Integer) rightValue));
                }
            case PRODUCT:
                if (leftValue instanceof Integer && rightValue instanceof Integer) {
                    return (Integer) (((Integer) leftValue) * ((Integer) rightValue));
                }
            case DIVIDE:
                if (leftValue instanceof Integer && rightValue instanceof Integer) {
                    return (Integer) (((Integer) leftValue) / ((Integer) rightValue));
                }
        }
        return null;
    }

    private static Object evaluateColumnRef(InternalResultRow row, ColumnRef cr) {
        return row.getValues().get(new ColumnRefImpl(cr));
    }
}