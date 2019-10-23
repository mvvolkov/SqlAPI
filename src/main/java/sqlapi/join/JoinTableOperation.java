package sqlapi.join;

import sqlapi.selectionPredicate.AbstractPredicate;

public class JoinTableOperation extends JoinTableOperand {

    private final Type type;
    private final JoinTableOperand left;
    private final JoinTableOperand right;
    private final AbstractPredicate selectionPredicate;

    public enum Type {
        INNER_JOIN,
        LEFT_OUTER_JOIN,
        RIGHT_OUTER_JOIN
    }

    private JoinTableOperation(Type type, JoinTableOperand left, JoinTableOperand right, AbstractPredicate selectionPredicate) {
        this.type = type;
        this.left = left;
        this.right = right;
        this.selectionPredicate = selectionPredicate;
    }

    public Type getType() {
        return type;
    }

    public final JoinTableOperand getLeft() {
        return left;
    }

    public final JoinTableOperand getRight() {
        return right;
    }

    public final AbstractPredicate getSelectionPredicate() {
        return selectionPredicate;
    }

    public static JoinTableOperation newInnerJoin(JoinTableOperand left, JoinTableOperand right, AbstractPredicate selectionPredicate) {
        return new JoinTableOperation(Type.INNER_JOIN, left, right, selectionPredicate);
    }

    public static JoinTableOperation newLeftOuterJoin(JoinTableOperand left, JoinTableOperand right, AbstractPredicate selectionPredicate) {
        return new JoinTableOperation(Type.LEFT_OUTER_JOIN, left, right, selectionPredicate);
    }

    public static JoinTableOperation newRightOuterJoin(JoinTableOperand left, JoinTableOperand right, AbstractPredicate selectionPredicate) {
        return new JoinTableOperation(Type.RIGHT_OUTER_JOIN, left, right, selectionPredicate);
    }
}
