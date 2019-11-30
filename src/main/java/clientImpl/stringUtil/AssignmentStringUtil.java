package clientImpl.stringUtil;

import sqlapi.exceptions.UnsupportedAggregateFunctionTypeException;
import sqlapi.exceptions.UnsupportedColumnExprTypeException;
import sqlapi.misc.AssignmentOperation;

public class AssignmentStringUtil {

    private AssignmentStringUtil() {
    }

    public static String getAssignmentOperationString(AssignmentOperation ao) throws UnsupportedAggregateFunctionTypeException, UnsupportedColumnExprTypeException {
        return ao.getColumnName() + " = " + ColumnExprStringUtil.getColumnExpressionString(ao.getValue());
    }
}
