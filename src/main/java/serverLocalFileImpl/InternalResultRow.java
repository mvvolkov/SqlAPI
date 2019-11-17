package serverLocalFileImpl;

import api.columnExpr.BinaryColumnExpression;
import api.columnExpr.ColumnExpression;
import api.columnExpr.ColumnRef;
import api.columnExpr.ColumnValue;
import api.exceptions.AmbiguousColumnNameException;
import api.exceptions.NoSuchColumnException;
import api.exceptions.WrongValueTypeException;
import api.predicates.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class InternalResultRow {

    private final Map<ColumnRef, Object> values;

    public InternalResultRow(Map<ColumnRef, Object> values) {
        this.values = values;
    }

    public Map<ColumnRef, Object> getValues() {
        return values;
    }

    public boolean matchPredicate(Predicate predicate)
            throws NoSuchColumnException, WrongValueTypeException,
            AmbiguousColumnNameException {

        switch (predicate.getType()) {
            case EMPTY:
                return true;
            case IN:
                return this.matchPredicate((ColumnInPredicate) predicate);
            case IS_NULL:
                return this.matchPredicate((ColumnIsNullPredicate) predicate);
            case IS_NOT_NULL:
                return this.matchPredicate((ColumnIsNotNullPredicate) predicate);
            case AND:
            case OR:
                return this.matchPredicate((CombinedPredicate) predicate);
            case EQUALS:
            case NOT_EQUALS:
            case GREATER_THAN:
            case GREATER_THAN_OR_EQUALS:
            case LESS_THAN:
            case LESS_THAN_OR_EQUALS:
                return this.matchPredicate((BinaryPredicate) predicate);
        }
        return false;
    }

    private boolean matchPredicate(CombinedPredicate predicate)
            throws NoSuchColumnException, WrongValueTypeException,
            AmbiguousColumnNameException {
        if (predicate.getType() == Predicate.Type.AND) {
            return this.matchPredicate(predicate.getLeftPredicate()) &&
                    this.matchPredicate(predicate.getRightPredicate());
        } else if (predicate.getType() == Predicate.Type.OR) {
            return this.matchPredicate(predicate.getLeftPredicate()) ||
                    this.matchPredicate(predicate.getRightPredicate());
        }
        return false;
    }

    private boolean matchPredicate(BinaryPredicate predicate)
            throws WrongValueTypeException, NoSuchColumnException,
            AmbiguousColumnNameException {


        Comparable leftValue =
                (Comparable) evaluateColumnExpr(predicate.getLeftOperand());
        Comparable rightValue =
                (Comparable) evaluateColumnExpr(predicate.getRightOperand());


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

    private boolean matchPredicate(ColumnInPredicate predicate)
            throws NoSuchColumnException, AmbiguousColumnNameException {

        Comparable leftValue =
                (Comparable) evaluateColumnExpr(predicate.getColumnRef());

        for (ColumnValue columnValue : predicate.getColumnValues()) {
            Comparable rightValue = (Comparable) columnValue.getValue();
            if (leftValue.compareTo(rightValue) == 0) {
                return true;
            }
        }
        return false;
    }

    private boolean matchPredicate(ColumnIsNullPredicate predicate)
            throws NoSuchColumnException, AmbiguousColumnNameException {

        Object leftValue = evaluateColumnExpr(predicate.getColumnRef());
        return leftValue == null;
    }

    private boolean matchPredicate(ColumnIsNotNullPredicate predicate)
            throws NoSuchColumnException, AmbiguousColumnNameException {

        Object leftValue = evaluateColumnExpr(predicate.getColumnRef());
        return leftValue != null;
    }

    public Object evaluateColumnExpr(ColumnExpression ce)
            throws NoSuchColumnException, AmbiguousColumnNameException {

        if (ce instanceof BinaryColumnExpression) {
            return evaluateBinaryColumnExpr((BinaryColumnExpression) ce);
        }
        if (ce instanceof ColumnRef) {
            return evaluateColumnRef((ColumnRef) ce);
        }
        if (ce instanceof ColumnValue) {
            return ((ColumnValue) ce).getValue();
        }
        return null;
    }

    private Object evaluateBinaryColumnExpr(BinaryColumnExpression bce)
            throws NoSuchColumnException, AmbiguousColumnNameException {
        Object leftValue = evaluateColumnExpr(bce.getLeftOperand());
        Object rightValue = evaluateColumnExpr(bce.getRightOperand());
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

    public Object evaluateColumnRef(ColumnRef cr)
            throws NoSuchColumnException, AmbiguousColumnNameException {

        List<ColumnRef> matchingColumns = new ArrayList<>();

        for (ColumnRef key : this.getValues().keySet()) {
            if (!cr.getSchemaName().isEmpty() &&
                    !cr.getSchemaName().equals(key.getSchemaName())) {
                continue;
            }
            if (!cr.getTableName().isEmpty() &&
                    !cr.getTableName().equals(key.getTableName())) {
                continue;
            }
            if (cr.getColumnName().equals(key.getColumnName())) {
                matchingColumns.add(key);
            }
        }
        if (matchingColumns.isEmpty()) {
            throw new NoSuchColumnException(cr.getColumnName());
        }
        if (matchingColumns.size() > 1) {
            throw new AmbiguousColumnNameException(cr.getColumnName());
        }

        return this.getValues().get(matchingColumns.get(0));
    }
}
