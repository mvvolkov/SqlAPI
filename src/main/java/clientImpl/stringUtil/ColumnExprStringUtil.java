package clientImpl.stringUtil;

import sqlapi.columnExpr.*;
import sqlapi.exceptions.UnsupportedAggregateFunctionTypeException;
import sqlapi.exceptions.UnsupportedColumnExprTypeException;

public class ColumnExprStringUtil {

    private ColumnExprStringUtil() {
    }

    public static String getColumnExpressionString(ColumnExpression ce)
            throws UnsupportedAggregateFunctionTypeException,
            UnsupportedColumnExprTypeException {

        if (ce instanceof ColumnRef) {
            return getColumnRefString((ColumnRef) ce);
        }
        if (ce instanceof InputValue) {
            return getInputValueString((InputValue) ce);
        }
        if (ce instanceof BinaryColumnExpression) {
            return getBinaryColumnExpressionString((BinaryColumnExpression) ce);
        }
        if (ce instanceof AggregateFunction) {
            return getAggregateFunctionString((AggregateFunction) ce);
        }
        throw new UnsupportedColumnExprTypeException(ce);
    }

    public static String getColumnRefString(ColumnRef cr) {
        StringBuilder sb = new StringBuilder(cr.getColumnName());
        if (!cr.getTableName().isEmpty()) {
            sb.insert(0, ".");
            sb.insert(0, cr.getTableName());
        }
        if (!cr.getDatabaseName().isEmpty()) {
            sb.insert(0, ".");
            sb.insert(0, cr.getDatabaseName());
        }
        return sb.toString();
    }

    public static String getInputValueString(InputValue cv) {
        StringBuilder sb = new StringBuilder();
        if (cv instanceof ParametrizedInputValue &&
                !((ParametrizedInputValue) cv).hasValue()) {
            sb.append("??");
        } else {
            Object value = cv.getValue();
            if (value instanceof String) {
                sb.append('\'').append(value).append('\'');
            } else if (value == null) {
                sb.append("NULL");
            } else {
                sb.append(value.toString());
            }
        }
        if (!cv.getAlias().isEmpty()) {
            sb.append(" AS ");
            sb.append(cv.getAlias());
        }
        return sb.toString();
    }

    public static String getBinaryColumnExpressionString(BinaryColumnExpression bce)
            throws UnsupportedColumnExprTypeException,
            UnsupportedAggregateFunctionTypeException {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(getColumnExpressionString(bce.getLeftOperand()));
        sb.append(" ");
        sb.append(getBinaryExpressionOperatorName(bce));
        sb.append(" ");
        sb.append(getColumnExpressionString(bce.getRightOperand()));
        sb.append(")");
        if (!bce.getAlias().isEmpty()) {
            sb.append(" AS ");
            sb.append(bce.getAlias());
        }
        return sb.toString();
    }

    private static String getBinaryExpressionOperatorName(BinaryColumnExpression bce)
            throws UnsupportedColumnExprTypeException {
        if (bce instanceof SumColumnExpression) {
            return "+";
        }
        if (bce instanceof DiffColumnExpression) {
            return "-";
        }
        if (bce instanceof ProductColumnExpression) {
            return "*";
        }
        if (bce instanceof DivisionColumnExpression) {
            return "/";
        }
        throw new UnsupportedColumnExprTypeException(bce);
    }

    public static String getAggregateFunctionString(AggregateFunction af)
            throws UnsupportedAggregateFunctionTypeException {
        StringBuilder sb = new StringBuilder();
        sb.append(getAggregateFunctionName(af));
        sb.append("(");
        if (af.getColumnRef().getColumnName().isEmpty()) {
            sb.append("*");
        } else {
            sb.append(getColumnRefString(af.getColumnRef()));
        }
        sb.append(")");
        if (!af.getAlias().isEmpty()) {
            sb.append(" AS ");
            sb.append(af.getAlias());
        }
        return sb.toString();
    }

    private static String getAggregateFunctionName(AggregateFunction af)
            throws UnsupportedAggregateFunctionTypeException {
        if (af instanceof CountAggregateFunction) {
            return "COUNT";
        }
        if (af instanceof SumAggregateFunction) {
            return "SUM";
        }
        if (af instanceof MaxAggregateFunction) {
            return "MAX";
        }
        if (af instanceof MinAggregateFunction) {
            return "MIN";
        }
        if (af instanceof AvgAggregateFunction) {
            return "AVG";
        }
        throw new UnsupportedAggregateFunctionTypeException(af);
    }
}
