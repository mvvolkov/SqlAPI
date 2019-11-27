package serverLocalFileImpl.intermediateResult;

import sqlapi.columnExpr.*;
import sqlapi.exceptions.*;
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
            throws SqlException {

        if (predicate instanceof EmptyPredicate) {
            return true;
        }
        if (predicate instanceof ColumnInPredicate) {
            return this.evaluateInPredicate((ColumnInPredicate) predicate);
        }
        if (predicate instanceof ColumnIsNullPredicate) {
            return this.evaluateIsNullPredicate((ColumnIsNullPredicate) predicate);
        }
        if (predicate instanceof ColumnIsNotNullPredicate) {
            return this.evaluateIsNotNullPredicate((ColumnIsNotNullPredicate) predicate);
        }
        if (predicate instanceof CombinedPredicate) {
            return this.evaluateCombinedPredicate((CombinedPredicate) predicate);
        }
        if (predicate instanceof BinaryPredicate) {
            return this.evaluateBinaryPredicate((BinaryPredicate) predicate);
        }
        throw new UnsupportedPredicateTypeException(predicate.getClass().getSimpleName());
    }

    private boolean evaluateCombinedPredicate(CombinedPredicate predicate)
            throws SqlException {
        if (predicate instanceof AndPredicate) {
            return this.evaluatePredicate(predicate.getLeftPredicate()) &&
                    this.evaluatePredicate(predicate.getRightPredicate());
        } else if (predicate instanceof OrPredicate) {
            return this.evaluatePredicate(predicate.getLeftPredicate()) ||
                    this.evaluatePredicate(predicate.getRightPredicate());
        }
        return false;
    }

    private boolean evaluateBinaryPredicate(BinaryPredicate predicate)
            throws SqlException {


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
        if (predicate instanceof EqualsPredicate) {
            return compResult == 0;
        }
        if (predicate instanceof NotEqualsPredicate) {
            return compResult != 0;
        }
        if (predicate instanceof GreaterThanPredicate) {
            return compResult > 0;
        }
        if (predicate instanceof GreaterThanOrEqualsPredicate) {
            return compResult >= 0;
        }
        if (predicate instanceof LessThanPredicate) {
            return compResult < 0;
        }
        if (predicate instanceof LessThanOrEqualsPredicate) {
            return compResult <= 0;
        }
        return false;
    }

    private boolean evaluateInPredicate(ColumnInPredicate predicate)
            throws SqlException {

        Comparable leftValue = (Comparable) evaluateColumnExpr(predicate.getColumnRef());

        for (ColumnValue columnValue : predicate.getColumnValues()) {
            Comparable rightValue = (Comparable) columnValue.getValue();
            if (leftValue != null && rightValue != null &&
                    leftValue.compareTo(rightValue) == 0) {
                return true;
            }
        }
        return false;
    }

    private boolean evaluateIsNullPredicate(ColumnIsNullPredicate predicate)
            throws SqlException {

        Object leftValue = evaluateColumnExpr(predicate.getColumnRef());
        return leftValue == null;
    }

    private boolean evaluateIsNotNullPredicate(ColumnIsNotNullPredicate predicate)
            throws SqlException {

        Object leftValue = evaluateColumnExpr(predicate.getColumnRef());
        return leftValue != null;
    }

    private Object evaluateBinaryColumnExpr(BinaryColumnExpression bce)
            throws SqlException {
        Object leftValue = this.evaluateColumnExpr(bce.getLeftOperand());
        Object rightValue = this.evaluateColumnExpr(bce.getRightOperand());

        if (bce instanceof SumColumnExpression) {
            return evaluateSumColumnExpr(leftValue, rightValue);
        }
        if (bce instanceof DiffColumnExpression) {
            return evaluateDiffColumnExpr(leftValue, rightValue);
        }
        if (bce instanceof ProductColumnExpression) {
            return evaluateProductColumnExpr(leftValue, rightValue);
        }
        if (bce instanceof DivisionColumnExpression) {
            return evaluateDivisionColumnExpr(leftValue, rightValue);
        }
        throw new InvalidQueryException("Unknown arithmetical operation");
    }

    public Object evaluateColumnExpr(ColumnExpression ce)
            throws SqlException {

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

    static private void checkArithmeticalOperands(Object leftValue, Object rightValue) throws InvalidQueryException {
        if (leftValue == null || rightValue == null) {
            throw new InvalidQueryException(
                    "Null values can not be used in arithmetical expressions");
        }
        if (!(leftValue instanceof Integer) || !(rightValue instanceof Integer)) {
            throw new InvalidQueryException("Only numerical types can be used in " +
                    "arithmetical expressions");
        }
    }

    static Object evaluateSumColumnExpr(Object leftValue, Object rightValue)
            throws InvalidQueryException {
        checkArithmeticalOperands(leftValue, rightValue);
        Integer i1 = (Integer) leftValue;
        Integer i2 = (Integer) rightValue;
        return i1 + i2;
    }

    static Object evaluateDiffColumnExpr(Object leftValue, Object rightValue)
            throws InvalidQueryException {
        checkArithmeticalOperands(leftValue, rightValue);
        Integer i1 = (Integer) leftValue;
        Integer i2 = (Integer) rightValue;
        return i1 - i2;
    }

    static Object evaluateProductColumnExpr(Object leftValue, Object rightValue)
            throws InvalidQueryException {
        checkArithmeticalOperands(leftValue, rightValue);
        Integer i1 = (Integer) leftValue;
        Integer i2 = (Integer) rightValue;
        return i1 * i2;
    }

    static Object evaluateDivisionColumnExpr(Object leftValue, Object rightValue)
            throws InvalidQueryException {
        checkArithmeticalOperands(leftValue, rightValue);
        Integer i1 = (Integer) leftValue;
        Integer i2 = (Integer) rightValue;
        if (i2 == 0) {
            throw new InvalidQueryException("Here we need another type of " +
                    "exception :(");
        }
        return i1 / i2;
    }


    public Object evaluateColumnRef(ColumnRef cr)
            throws NoSuchColumnException, AmbiguousColumnNameException {

        List<DataHeader> matchingHeaders = new ArrayList<>();

        for (DataHeader key : values.keySet()) {
            if (!cr.getDatabaseName().isEmpty() &&
                    !cr.getDatabaseName().equals(key.getDatabaseName())) {
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
