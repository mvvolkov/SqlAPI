package localFileDatabase.server.intermediateResult;

import sqlapi.columnExpr.*;
import sqlapi.exceptions.*;
import sqlapi.predicates.*;

import java.util.ArrayList;
import java.util.List;

public final class DataRow {

    private final List<DataValue> values;

    public DataRow(List<DataValue> values) {
        this.values = values;
    }

    public List<DataValue> getValues() {
        return values;
    }

    public boolean matchPredicate(Predicate predicate)
            throws SqlException {

        if (predicate instanceof EmptyPredicate) {
            return true;
        }
        if (predicate instanceof ColumnInPredicate) {
            return this.matchInPredicate((ColumnInPredicate) predicate);
        }
        if (predicate instanceof ColumnIsNullPredicate) {
            return this.matchIsNullPredicate((ColumnIsNullPredicate) predicate);
        }
        if (predicate instanceof ColumnIsNotNullPredicate) {
            return this.matchIsNotNullPredicate((ColumnIsNotNullPredicate) predicate);
        }
        if (predicate instanceof CombinedPredicate) {
            return this.matchCombinedPredicate((CombinedPredicate) predicate);
        }
        if (predicate instanceof BinaryPredicate) {
            return this.matchBinaryPredicate((BinaryPredicate) predicate);
        }
        throw new UnsupportedPredicateTypeException(predicate);
    }

    private boolean matchCombinedPredicate(CombinedPredicate predicate)
            throws SqlException {
        if (predicate instanceof AndPredicate) {
            return this.matchPredicate(predicate.getLeftPredicate()) &&
                    this.matchPredicate(predicate.getRightPredicate());
        } else if (predicate instanceof OrPredicate) {
            return this.matchPredicate(predicate.getLeftPredicate()) ||
                    this.matchPredicate(predicate.getRightPredicate());
        }
        throw new UnsupportedPredicateTypeException(predicate);
    }

    private boolean matchBinaryPredicate(BinaryPredicate predicate)
            throws SqlException {


        DataValue leftValue = evaluateColumnExpr(predicate.getLeftOperand());
        DataValue rightValue = evaluateColumnExpr(predicate.getRightOperand());

        if (leftValue.getValue() == null || rightValue.getValue() == null) {
            return false;
        }
        int compResult = leftValue.getComparisonResult(rightValue.getValue());
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

    private boolean matchInPredicate(ColumnInPredicate predicate)
            throws SqlException {

        DataValue value = this.evaluateColumnExpr(predicate.getColumnRef());
        for (ColumnValue columnValue : predicate.getColumnValues()) {
            if (value.isEqual(columnValue)) {
                return true;
            }
        }
        return false;
    }


    private boolean matchIsNullPredicate(ColumnIsNullPredicate predicate)
            throws SqlException {

        DataValue leftValue = evaluateColumnRef(predicate.getColumnRef());
        return leftValue.getValue() == null;
    }

    private boolean matchIsNotNullPredicate(ColumnIsNotNullPredicate predicate)
            throws SqlException {

        DataValue leftValue = evaluateColumnRef(predicate.getColumnRef());
        return leftValue.getValue() != null;
    }

    public DataValue evaluateColumnExpr(ColumnExpression ce)
            throws SqlException {

        if (ce instanceof BinaryColumnExpression) {
            return evaluateBinaryColumnExpr((BinaryColumnExpression) ce);
        }
        if (ce instanceof ColumnRef) {
            return evaluateColumnRef((ColumnRef) ce);
        }
        if (ce instanceof ColumnValue) {
            return new DataValue(((ColumnValue) ce).getValue());
        }
        throw new UnsupportedColumnExprTypeException(ce);
    }

    private DataValue evaluateBinaryColumnExpr(BinaryColumnExpression bce)
            throws SqlException {
        DataValue leftValue = this.evaluateColumnExpr(bce.getLeftOperand());
        DataValue rightValue = this.evaluateColumnExpr(bce.getRightOperand());

        if (bce instanceof SumColumnExpression) {
            return leftValue.add(rightValue);
        }
        if (bce instanceof DiffColumnExpression) {
            return leftValue.subtract(rightValue);
        }
        if (bce instanceof ProductColumnExpression) {
            return leftValue.multiply(rightValue);
        }
        if (bce instanceof DivisionColumnExpression) {
            return leftValue.divide(rightValue);
        }
        throw new UnsupportedColumnExprTypeException(bce);
    }


    public DataValue evaluateColumnRef(ColumnRef cr)
            throws NoSuchColumnException, AmbiguousColumnNameException {

        List<DataValue> matchingValues = new ArrayList<>();

        for (DataValue value : values) {
            if (!cr.getDatabaseName().isEmpty() &&
                    !cr.getDatabaseName().equals(value.getHeader().getDatabaseName())) {
                continue;
            }
            if (!cr.getTableName().isEmpty() &&
                    !cr.getTableName().equals(value.getHeader().getTableName())) {
                continue;
            }
            if (cr.getColumnName().equals(value.getHeader().getColumnName())) {
                matchingValues.add(value);
            }
        }
        if (matchingValues.isEmpty()) {
            throw new NoSuchColumnException(cr.getColumnName());
        }
        if (matchingValues.size() > 1) {
            throw new AmbiguousColumnNameException(cr.getColumnName());
        }
        return matchingValues.get(0);
    }
}
