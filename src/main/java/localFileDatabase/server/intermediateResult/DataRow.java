package localFileDatabase.server.intermediateResult;

import sqlapi.columnExpr.*;
import sqlapi.exceptions.*;
import sqlapi.metadata.SqlType;
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


        SqlValue leftValue = evaluateColumnExpr(predicate.getLeftOperand());
        SqlValue rightValue = evaluateColumnExpr(predicate.getRightOperand());

        if (leftValue.getValue() == null || rightValue.getValue() == null) {
            return false;
        }
        int compResult = SqlValue.getComparisonResult(leftValue, rightValue);
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

        SqlValue value = evaluateColumnExpr(predicate.getColumnRef());

        for (ColumnValue columnValue : predicate.getColumnValues()) {
            if (isEqual(columnValue, value)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isEqual(ColumnValue columnValue, SqlValue sqlValue)
            throws WrongValueTypeException {
        if (columnValue.getValue() == null && sqlValue.getValue() == null) {
            return true;
        }
        if (sqlValue.getSqlType() == SqlType.INTEGER) {
            if (!(columnValue.getValue() instanceof Integer)) {
                throw new WrongValueTypeException();
            }
            Integer i1 = (Integer) columnValue.getValue();
            Integer i2 = (Integer) sqlValue.getValue();
            return i1.equals(i2);
        }
        if (sqlValue.getSqlType() == SqlType.VARCHAR) {
            if (!(columnValue.getValue() instanceof String)) {
                throw new WrongValueTypeException();
            }
            String s1 = (String) columnValue.getValue();
            String s2 = (String) sqlValue.getValue();
            return s1.equals(s2);
        }
        throw new WrongValueTypeException();
    }

    private boolean matchIsNullPredicate(ColumnIsNullPredicate predicate)
            throws SqlException {

        SqlValue leftValue = evaluateColumnExpr(predicate.getColumnRef());
        return leftValue.getValue() == null;
    }

    private boolean matchIsNotNullPredicate(ColumnIsNotNullPredicate predicate)
            throws SqlException {

        SqlValue leftValue = evaluateColumnExpr(predicate.getColumnRef());
        return leftValue.getValue() != null;
    }

    public SqlValue evaluateColumnExpr(ColumnExpression ce)
            throws SqlException {

        if (ce instanceof BinaryColumnExpression) {
            return evaluateBinaryColumnExpr((BinaryColumnExpression) ce);
        }
        if (ce instanceof ColumnRef) {
            return evaluateColumnRef((ColumnRef) ce);
        }
        if (ce instanceof ColumnValue) {
            Object value = ((ColumnValue) ce).getValue();
            return new SqlValue(SqlValue.getSqlType(value), value);
        }
        throw new UnsupportedColumnExprTypeException(ce);
    }

    private SqlValue evaluateBinaryColumnExpr(BinaryColumnExpression bce)
            throws SqlException {
        SqlValue leftValue = this.evaluateColumnExpr(bce.getLeftOperand());
        SqlValue rightValue = this.evaluateColumnExpr(bce.getRightOperand());

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
        throw new UnsupportedColumnExprTypeException(bce);
    }


    static SqlValue evaluateSumColumnExpr(SqlValue leftValue, SqlValue rightValue)
            throws WrongValueTypeException {
        if (leftValue.getValue() == null || rightValue.getValue() == null) {
            throw new WrongValueTypeException();
        }
        if (leftValue.getSqlType() != rightValue.getSqlType()) {
            throw new WrongValueTypeException();
        }
        if (leftValue.getSqlType() == SqlType.INTEGER) {
            Integer i1 = (Integer) leftValue.getValue();
            Integer i2 = (Integer) rightValue.getValue();
            return new SqlValue(SqlType.INTEGER, i1 + i2);
        }
        throw new WrongValueTypeException();
    }

    static SqlValue evaluateDiffColumnExpr(SqlValue leftValue, SqlValue rightValue)
            throws WrongValueTypeException {
        if (leftValue.getValue() == null || rightValue.getValue() == null) {
            throw new WrongValueTypeException();
        }
        if (leftValue.getSqlType() != rightValue.getSqlType()) {
            throw new WrongValueTypeException();
        }
        if (leftValue.getSqlType() == SqlType.INTEGER) {
            Integer i1 = (Integer) leftValue.getValue();
            Integer i2 = (Integer) rightValue.getValue();
            return new SqlValue(SqlType.INTEGER, i1 - i2);
        }
        throw new WrongValueTypeException();
    }

    static SqlValue evaluateProductColumnExpr(SqlValue leftValue, SqlValue rightValue)
            throws WrongValueTypeException {
        if (leftValue.getValue() == null || rightValue.getValue() == null) {
            throw new WrongValueTypeException();
        }
        if (leftValue.getSqlType() != rightValue.getSqlType()) {
            throw new WrongValueTypeException();
        }
        if (leftValue.getSqlType() == SqlType.INTEGER) {
            Integer i1 = (Integer) leftValue.getValue();
            Integer i2 = (Integer) rightValue.getValue();
            return new SqlValue(SqlType.INTEGER, i1 * i2);
        }
        throw new WrongValueTypeException();
    }

    static SqlValue evaluateDivisionColumnExpr(SqlValue leftValue, SqlValue rightValue)
            throws WrongValueTypeException {
        if (leftValue.getValue() == null || rightValue.getValue() == null) {
            throw new WrongValueTypeException();
        }
        if (leftValue.getSqlType() != rightValue.getSqlType()) {
            throw new WrongValueTypeException();
        }
        if (leftValue.getSqlType() == SqlType.INTEGER) {
            Integer i1 = (Integer) leftValue.getValue();
            Integer i2 = (Integer) rightValue.getValue();
            if (i2 == 0) {
                throw new WrongValueTypeException();
            }
            return new SqlValue(SqlType.INTEGER, i1 / i2);
        }
        throw new WrongValueTypeException();
    }


    public SqlValue evaluateColumnRef(ColumnRef cr)
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
        DataHeader matchingHeader = matchingHeaders.get(0);
        Object value = this.getCells().get(matchingHeader);
        return new SqlValue(matchingHeader.getSqlType(), value);
    }
}
