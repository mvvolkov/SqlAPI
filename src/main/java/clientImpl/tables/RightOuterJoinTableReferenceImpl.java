package clientImpl.tables;

import org.jetbrains.annotations.NotNull;
import sqlapi.predicates.Predicate;
import sqlapi.tables.RightOuterJoinTableReference;
import sqlapi.tables.TableReference;

final class RightOuterJoinTableReferenceImpl extends JoinedTableReferenceImpl implements RightOuterJoinTableReference {

    RightOuterJoinTableReferenceImpl(@NotNull TableReference left, @NotNull TableReference right, @NotNull Predicate predicate) {
        super(left, right, predicate);
    }

    @Override
    protected String getJoinName() {
        return "RIGHT OUTER JOIN";
    }
}
