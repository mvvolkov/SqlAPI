package sqlapi.columnExpr;

public interface BinaryColumnExpression extends ColumnExpression {

    ColumnExpression getLeftOperand();

    ColumnExpression getRightOperand();
}
