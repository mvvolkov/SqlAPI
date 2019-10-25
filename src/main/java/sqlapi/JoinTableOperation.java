package sqlapi;

import sqlapi.selectionPredicate.SelectionPredicate;

public class JoinTableOperation extends TableReference {

    private final Type type;
    private final TableReference left;
    private final TableReference right;
    private final SelectionPredicate selectionPredicate;

    public enum Type {
        INNER_JOIN,
        LEFT_OUTER_JOIN,
        RIGHT_OUTER_JOIN
    }

    private JoinTableOperation(Type type, TableReference left, TableReference right, SelectionPredicate selectionPredicate) {
        super(null);
        this.type = type;
        this.left = left;
        this.right = right;
        this.selectionPredicate = selectionPredicate;
    }

    public Type getType() {
        return type;
    }

    public final TableReference getLeft() {
        return left;
    }

    public final TableReference getRight() {
        return right;
    }

    public final SelectionPredicate getSelectionPredicate() {
        return selectionPredicate;
    }

    public static JoinTableOperation newInnerJoin(TableReference left, TableReference right, SelectionPredicate selectionPredicate) {
        return new JoinTableOperation(Type.INNER_JOIN, left, right, selectionPredicate);
    }

    public static JoinTableOperation newLeftOuterJoin(TableReference left, TableReference right, SelectionPredicate selectionPredicate) {
        return new JoinTableOperation(Type.LEFT_OUTER_JOIN, left, right, selectionPredicate);
    }

    public static JoinTableOperation newRightOuterJoin(TableReference left, TableReference right, SelectionPredicate selectionPredicate) {
        return new JoinTableOperation(Type.RIGHT_OUTER_JOIN, left, right, selectionPredicate);
    }
}
