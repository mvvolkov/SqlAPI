package clientImpl.tables;

import org.jetbrains.annotations.NotNull;
import sqlapi.tables.DatabaseTableReference;
import sqlapi.tables.JoinedTableReference;
import sqlapi.tables.TableReference;
import sqlapi.predicates.Predicate;

import java.util.ArrayDeque;

abstract class JoinedTableReferenceImpl implements JoinedTableReference {


    @NotNull
    private final TableReference left;

    @NotNull
    private final TableReference right;

    @NotNull
    private final Predicate predicate;


    JoinedTableReferenceImpl(@NotNull TableReference left,
                             @NotNull TableReference right,
                             @NotNull Predicate predicate) {
        this.left = left;
        this.right = right;
        this.predicate = predicate;
    }

    @NotNull
    @Override
    public TableReference getLeftTableReference() {
        return left;
    }

    @NotNull
    @Override
    public TableReference getRightTableReference() {
        return right;
    }

    @NotNull
    @Override
    public Predicate getPredicate() {
        return predicate;
    }


    protected abstract String getJoinName();


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean leftRefIsDbTable = left instanceof DatabaseTableReference;
        boolean rightRefIsDbTable = right instanceof DatabaseTableReference;
        if (!leftRefIsDbTable) {
            sb.append("(");
        }
        sb.append(left);
        if (!leftRefIsDbTable) {
            sb.append(")");
        }
        sb.append(" ");
        sb.append(this.getJoinName());
        sb.append(" ");
        if (!rightRefIsDbTable) {
            sb.append("(");
        }
        sb.append(right);
        if (!rightRefIsDbTable) {
            sb.append(")");
        }
        if (!predicate.isEmpty()) {
            sb.append(" ON ");
            sb.append(predicate);
        }
        return sb.toString();
    }
}
