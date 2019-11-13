package clientImpl.columnExpr;

import api.columnExpr.BinaryColumnExpression;
import api.columnExpr.ColumnExpression;
import api.columnExpr.ColumnValue;

public class ColumnExprFactory {

    private ColumnExprFactory() {
    }

    public static ColumnValue nullValue() {
        return new ColumnValueImpl(null);
    }


    public static ColumnValue value(Object value){
        return new ColumnValueImpl(value);
    }

    public static ColumnRefImpl columnRef(String databaseName, String tableName,
                                      String columnName) {
        return new ColumnRefImpl(databaseName, tableName, columnName);
    }

    public static ColumnValue integer(Integer value) {
        return new ColumnValueImpl(value);
    }

    public static ColumnValue string(String value) {
        return new ColumnValueImpl(value);
    }


    public static BinaryColumnExpression sum(ColumnExpression leftOperand,
                                             ColumnExpression rightOperand) {
        return new BinaryColumnExprImpl(ColumnExpression.ExprType.SUM, leftOperand,
                rightOperand);
    }

    public static BinaryColumnExpression diff(ColumnExpression leftOperand,
                                              ColumnExpression rightOperand) {
        return new BinaryColumnExprImpl(ColumnExpression.ExprType.DIFF, leftOperand,
                rightOperand);
    }

    public static BinaryColumnExpression product(ColumnExpression leftOperand,
                                                 ColumnExpression rightOperand) {
        return new BinaryColumnExprImpl(ColumnExpression.ExprType.PRODUCT, leftOperand,
                rightOperand);
    }

    public static BinaryColumnExpression divide(ColumnExpression leftOperand,
                                                ColumnExpression rightOperand) {
        return new BinaryColumnExprImpl(ColumnExpression.ExprType.DIVIDE, leftOperand,
                rightOperand);
    }
}
