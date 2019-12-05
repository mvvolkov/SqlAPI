package localFileDatabase.server.intermediate;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.*;
import sqlapi.exceptions.*;
import sqlapi.predicates.*;

import java.util.ArrayList;
import java.util.List;

public final class ResultRow extends AbstractResultRow {

    @NotNull
    private final List<ResultValue> values;

    public ResultRow(@NotNull List<ResultValue> values) {
        this.values = values;
    }

    @NotNull
    public List<ResultValue> getValues() {
        return values;
    }

    public boolean matchPredicate(@NotNull Predicate predicate)
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

    private boolean matchCombinedPredicate(@NotNull CombinedPredicate predicate)
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

    private boolean matchBinaryPredicate(@NotNull BinaryPredicate predicate)
            throws SqlException {


        ResultValue leftValue = evaluateColumnExpr(predicate.getLeftOperand());
        ResultValue rightValue = evaluateColumnExpr(predicate.getRightOperand());

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

    private boolean matchInPredicate(@NotNull ColumnInPredicate predicate)
            throws SqlException {

        ResultValue value = this.evaluateColumnExpr(predicate.getColumnRef());
        for (ColumnValue columnValue : predicate.getColumnValues()) {
            if (value.isEqual(columnValue)) {
                return true;
            }
        }
        return false;
    }


    private boolean matchIsNullPredicate(@NotNull ColumnIsNullPredicate predicate)
            throws SqlException {

        ResultValue leftValue = evaluateColumnRef(predicate.getColumnRef());
        return leftValue.getValue() == null;
    }

    private boolean matchIsNotNullPredicate(@NotNull ColumnIsNotNullPredicate predicate)
            throws SqlException {

        ResultValue leftValue = evaluateColumnRef(predicate.getColumnRef());
        return leftValue.getValue() != null;
    }


    @NotNull
    private ResultValue evaluateColumnRef(@NotNull String databaseName,
                                          @NotNull String tableName,
                                          @NotNull String columnName)
            throws NoSuchColumnException, AmbiguousColumnNameException {

        List<ResultValue> matchingValues = new ArrayList<>();

        for (ResultValue value : values) {
            if (!databaseName.isEmpty() &&
                    !databaseName.equals(value.getHeader().getDatabaseName())) {
                continue;
            }
            if (!tableName.isEmpty() &&
                    !tableName.equals(value.getHeader().getTableName())) {
                continue;
            }
            if (columnName.equals(value.getHeader().getColumnName())) {
                matchingValues.add(value);
            }
        }
        if (matchingValues.isEmpty()) {
            throw new NoSuchColumnException(columnName);
        }
        if (matchingValues.size() > 1) {
            throw new AmbiguousColumnNameException(columnName);
        }
        return matchingValues.get(0);
    }


    @NotNull
    protected ResultValue evaluateColumnRef(@NotNull ColumnRef cr)
            throws NoSuchColumnException, AmbiguousColumnNameException {

        return this.evaluateColumnRef(cr.getDatabaseName(), cr.getTableName(),
                cr.getColumnName());
    }

    @Override
    protected ResultValue evaluateAggregateFunction(@NotNull AggregateFunction af) throws SqlException {
        throw new UnsupportedColumnExprTypeException(af);
    }


    @NotNull
    ResultValue evaluateHeaderValue(@NotNull ResultHeader header)
            throws NoSuchColumnException, AmbiguousColumnNameException {
        return this.evaluateColumnRef(header.getDatabaseName(), header.getTableName(),
                header.getColumnName());
    }


}
