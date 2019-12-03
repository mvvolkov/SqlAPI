package localFileDatabase.server.intermediate;

import sqlapi.columnExpr.*;
import sqlapi.exceptions.*;
import sqlapi.misc.SelectedItem;
import sqlapi.predicates.*;
import sqlapi.tables.DatabaseTableReference;

import java.util.ArrayList;
import java.util.List;

public final class ResultRow {

    private final List<ResultValue> values;

    public ResultRow(List<ResultValue> values) {
        this.values = values;
    }

    public List<ResultValue> getValues() {
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

    private boolean matchInPredicate(ColumnInPredicate predicate)
            throws SqlException {

        ResultValue value = this.evaluateColumnExpr(predicate.getColumnRef());
        for (ColumnValue columnValue : predicate.getColumnValues()) {
            if (value.isEqual(columnValue)) {
                return true;
            }
        }
        return false;
    }


    private boolean matchIsNullPredicate(ColumnIsNullPredicate predicate)
            throws SqlException {

        ResultValue leftValue = evaluateColumnRef(predicate.getColumnRef());
        return leftValue.getValue() == null;
    }

    private boolean matchIsNotNullPredicate(ColumnIsNotNullPredicate predicate)
            throws SqlException {

        ResultValue leftValue = evaluateColumnRef(predicate.getColumnRef());
        return leftValue.getValue() != null;
    }

    public ResultValue evaluateColumnExpr(ColumnExpression ce)
            throws SqlException {

        if (ce instanceof BinaryColumnExpression) {
            return evaluateBinaryColumnExpr((BinaryColumnExpression) ce);
        }
        if (ce instanceof ColumnRef) {
            return evaluateColumnRef((ColumnRef) ce);
        }
        if (ce instanceof ColumnValue) {
            return new ResultValue(((ColumnValue) ce).getValue());
        }
        throw new UnsupportedColumnExprTypeException(ce);
    }

    private ResultValue evaluateBinaryColumnExpr(BinaryColumnExpression bce)
            throws SqlException {
        ResultValue leftValue = this.evaluateColumnExpr(bce.getLeftOperand());
        ResultValue rightValue = this.evaluateColumnExpr(bce.getRightOperand());

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

    private ResultValue evaluateColumnRef(String databaseName, String tableName, String columnName)
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


    ResultValue evaluateColumnRef(ColumnRef cr)
            throws NoSuchColumnException, AmbiguousColumnNameException {

        return evaluateColumnRef(cr.getDatabaseName(), cr.getTableName(), cr.getColumnName());
    }

    ResultValue evaluateHeaderValue(ResultHeader header)
            throws NoSuchColumnException, AmbiguousColumnNameException {
        return evaluateColumnRef(header.getDatabaseName(), header.getTableName(), header.getColumnName());
    }

    ResultRow evaluateSelectedItems(List<SelectedItem> selectedItems) throws SqlException {

        List<ResultValue> newValues = new ArrayList<>();
        for (SelectedItem selectedItem : selectedItems) {
            if (selectedItem instanceof DatabaseTableReference) {
                DatabaseTableReference tableRef = (DatabaseTableReference) selectedItem;
                for (ResultValue value : values) {
                    ResultHeader header = value.getHeader();
                    if (header.getDatabaseName().equals(tableRef.getDatabaseName()) &&
                            header.getTableName().equals(tableRef.getTableName())) {
                        newValues.add(this.evaluateHeaderValue(header));
                    }
                }
            } else {
                newValues.add(this.evaluateColumnExpr((ColumnExpression) selectedItem));
            }
        }
        return new ResultRow(newValues);
    }
}
