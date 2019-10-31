package testSimpleFileImpl;

import sqlapi.TableReference;
import sqlapi.selectionPredicate.SelectionPredicate;

public class JoinTableReference implements TableReference {

    private final Type type;
    private final TableReference left;
    private final TableReference right;
    private final SelectionPredicate selectionPredicate;

    public enum Type {
        INNER_JOIN,
        LEFT_OUTER_JOIN,
        RIGHT_OUTER_JOIN
    }

    JoinTableReference(Type type, TableReference left, TableReference right, SelectionPredicate selectionPredicate) {
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


}
