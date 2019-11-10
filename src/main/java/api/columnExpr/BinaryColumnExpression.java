package api.columnExpr;

public interface BinaryColumnExpression extends ColumnExpression {

    ColumnExpression getLeftOperand();

    ColumnExpression getRightOperand();
}
