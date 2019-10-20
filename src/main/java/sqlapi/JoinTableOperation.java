package sqlapi;

public class JoinTableOperation extends JoinTableOperand {

    private final Type type;
    private final JoinTableOperand left;
    private final JoinTableOperand right;
    private final SelectionCriteria selectionCriteria;

    public enum Type {
        INNER_JOIN,
        LEFT_OUTER_JOIN,
        RIGHT_OUTER_JOIN
    }

    private JoinTableOperation(Type type, JoinTableOperand left, JoinTableOperand right, SelectionCriteria selectionCriteria) {
        this.type = type;
        this.left = left;
        this.right = right;
        this.selectionCriteria = selectionCriteria;
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

    public final SelectionCriteria getSelectionCriteria() {
        return selectionCriteria;
    }

    public static JoinTableOperation newInnerJoin(JoinTableOperand left, JoinTableOperand right, SelectionCriteria selectionCriteria) {
        return new JoinTableOperation(Type.INNER_JOIN, left, right, selectionCriteria);
    }

    public static JoinTableOperation newLeftOuterJoin(JoinTableOperand left, JoinTableOperand right, SelectionCriteria selectionCriteria) {
        return new JoinTableOperation(Type.LEFT_OUTER_JOIN, left, right, selectionCriteria);
    }

    public static JoinTableOperation newRightOuterJoin(JoinTableOperand left, JoinTableOperand right, SelectionCriteria selectionCriteria) {
        return new JoinTableOperation(Type.RIGHT_OUTER_JOIN, left, right, selectionCriteria);
    }
}
