package serverLocalFileImpl.intermediateresult;

import sqlapi.columnExpr.BinaryColumnExpression;
import sqlapi.columnExpr.ColumnExpression;
import sqlapi.columnExpr.ColumnRef;
import sqlapi.columnExpr.ColumnValue;
import sqlapi.exceptions.AmbiguousColumnNameException;
import sqlapi.exceptions.InvalidQueryException;
import sqlapi.exceptions.NoSuchColumnException;
import sqlapi.exceptions.WrongValueTypeException;
import sqlapi.predicates.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class DataRow {

    private final Map<DataHeader, Object> values;

    public DataRow(Map<DataHeader, Object> values) {
        this.values = values;
    }

    public Map<DataHeader, Object> getCells() {
        return values;
    }

    public boolean evaluatePredicate(Predicate predicate)
            throws NoSuchColumnException, WrongValueTypeException,
            AmbiguousColumnNameException, InvalidQueryException {

        switch (predicate.getType()) {
            case EMPTY:
                return true;
            case IN:
                return this.evaluateInPredicate((ColumnInPredicate) predicate);
            case IS_NULL:
                return this.evaluateIsNullPredicate((ColumnIsNullPredicate) predicate);
            case IS_NOT_NULL:
                return this
                        .evaluateIsNotNullPredicate((ColumnIsNotNullPredicate) predicate);
            case AND:
            case OR:
                return this.evaluateCombinedPredicate((CombinedPredicate) predicate);
            case EQUALS:
            case NOT_EQUALS:
            case GREATER_THAN:
            case GREATER_THAN_OR_EQUALS:
            case LESS_THAN:
            case LESS_THAN_OR_EQUALS:
                return this.evaluatePredicate((BinaryPredicate) predicate);
        }
        return false;
    }

    private boolean evaluateCombinedPredicate(CombinedPredicate predicate)
            throws NoSuchColumnException, WrongValueTypeException,
            AmbiguousColumnNameException, InvalidQueryException {
        if (predicate.getType() == Predicate.Type.AND) {
            return this.evaluatePredicate(predicate.getLeftPredicate()) &&
                    this.evaluatePredicate(predicate.getRightPredicate());
        } else if (predicate.getType() == Predicate.Type.OR) {
            return this.evaluatePredicate(predicate.getLeftPredicate()) ||
                    this.evaluatePredicate(predicate.getRightPredicate());
        }
        return false;
    }

    private boolean evaluatePredicate(BinaryPredicate predicate)
            throws NoSuchColumnException,
            AmbiguousColumnNameException, InvalidQueryException {


        Comparable leftValue =
                (Comparable) evaluateColumnExpr(predicate.getLeftOperand());
        Comparable rightValue =
                (Comparable) evaluateColumnExpr(predicate.getRightOperand());


        if (leftValue == null || rightValue == null) {
            return false;
        }

        if (leftValue.getClass() != rightValue.getClass()) {
            throw new InvalidQueryException("Objects of different classes are used in " +
                    "the predicate: " + predicate.toString());
        }


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

    private boolean evaluateInPredicate(ColumnInPredicate predicate)
            throws NoSuchColumnException, AmbiguousColumnNameException,
            InvalidQueryException {

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

    private boolean evaluateIsNullPredicate(ColumnIsNullPredicate predicate)
            throws NoSuchColumnException, AmbiguousColumnNameException,
            InvalidQueryException {

        Object leftValue = evaluateColumnExpr(predicate.getColumnRef());
        return leftValue == null;
    }

    private boolean evaluateIsNotNullPredicate(ColumnIsNotNullPredicate predicate)
            throws NoSuchColumnException, AmbiguousColumnNameException,
            InvalidQueryException {

        Object leftValue = evaluateColumnExpr(predicate.getColumnRef());
        return leftValue != null;
    }

    public Object evaluateColumnExpr(ColumnExpression ce)
            throws NoSuchColumnException, AmbiguousColumnNameException,
            InvalidQueryException {

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
            throws NoSuchColumnException, AmbiguousColumnNameException,
            InvalidQueryException {
        Object leftValue = evaluateColumnExpr(bce.getLeftOperand());
        Object rightValue = evaluateColumnExpr(bce.getRightOperand());
        if (leftValue == null || rightValue == null) {
            throw new InvalidQueryException("Null values can not be used in arithmetical expressions");
        }
        if (!(leftValue instanceof Integer) || !(rightValue instanceof Integer)) {
            throw new InvalidQueryException("Only numerical types can be used in " +
                    "arithmetical expressions");
        }
        Integer i1 = (Integer) leftValue;
        Integer i2 = (Integer) rightValue;
        switch (bce.getExprType()) {
            case SUM:
                return i1 + i2;
            case DIFF:
                return i1 - i2;
            case PRODUCT:
                return i1 * i2;
            case DIVIDE:
                if (i2 == 0) {
                    throw new InvalidQueryException("Here we need another type of " +
                            "exception :(");
                }
                return i1 / i2;
            default:
                throw new InvalidQueryException("Unknown arithmetical operation");
        }
    }

    public Object evaluateColumnRef(ColumnRef cr)
            throws NoSuchColumnException, AmbiguousColumnNameException {

        List<DataHeader> matchingHeaders = new ArrayList<>();

        for (DataHeader key : values.keySet()) {
            if (!cr.getSchemaName().isEmpty() &&
                    !cr.getSchemaName().equals(key.getSchemaName())) {
                continue;
            }
            if (!cr.getTableName().isEmpty() &&
                    !cr.getTableName().equals(key.getTableName())) {
                continue;
            }
            if (cr.getColumnName().equals(key.getColumnName())) {
                matchingHeaders.add(key);
            }
        }
        if (matchingHeaders.isEmpty()) {
            throw new NoSuchColumnException(cr.getColumnName());
        }
        if (matchingHeaders.size() > 1) {
            throw new AmbiguousColumnNameException(cr.getColumnName());
        }
        return this.getCells().get(matchingHeaders.get(0));
    }
}
