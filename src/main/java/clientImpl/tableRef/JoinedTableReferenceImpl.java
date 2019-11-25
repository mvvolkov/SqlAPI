package clientImpl.tableRef;

import org.jetbrains.annotations.NotNull;
import sqlapi.tables.DatabaseTableReference;
import sqlapi.tables.JoinedTableReference;
import sqlapi.tables.TableReference;
import sqlapi.predicates.Predicate;

final class JoinedTableReferenceImpl implements JoinedTableReference {

    @NotNull
    private final TableRefType joinType;

    @NotNull
    private final TableReference left;

    @NotNull
    private final TableReference right;

    @NotNull
    private final Predicate predicate;


    JoinedTableReferenceImpl(@NotNull TableRefType joinType, @NotNull TableReference left,
                             @NotNull TableReference right,
                             @NotNull Predicate predicate) {
        this.joinType = joinType;
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

    @NotNull
    @Override
    public TableRefType getTableRefType() {
        return joinType;
    }

    @Override public String toString() {
        String operator;
        switch (joinType) {
            case INNER_JOIN:
                operator = "INNER JOIN";
                break;
            case LEFT_OUTER_JOIN:
                operator = "LEFT OUTER JOIN";
                break;
            case RIGHT_OUTER_JOIN:
                operator = "RIGHT OUTER JOIN";
                break;
            default:
                operator = "";
        }
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
        sb.append(operator);
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
