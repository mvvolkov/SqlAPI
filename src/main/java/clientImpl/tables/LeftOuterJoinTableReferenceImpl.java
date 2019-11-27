package clientImpl.tables;

import org.jetbrains.annotations.NotNull;
import sqlapi.predicates.Predicate;
import sqlapi.tables.LeftOuterJoinTableReference;
import sqlapi.tables.TableReference;

final class LeftOuterJoinTableReferenceImpl extends JoinedTableReferenceImpl implements LeftOuterJoinTableReference {

    LeftOuterJoinTableReferenceImpl(@NotNull TableReference left, @NotNull TableReference right, @NotNull Predicate predicate) {
        super(left, right, predicate);
    }

    @Override
    protected String getJoinName() {
        return "LEFT OUTER JOIN";
    }
}
