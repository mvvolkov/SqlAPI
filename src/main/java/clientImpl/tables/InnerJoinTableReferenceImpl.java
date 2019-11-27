package clientImpl.tables;

import org.jetbrains.annotations.NotNull;
import sqlapi.predicates.Predicate;
import sqlapi.tables.InnerJoinTableReference;
import sqlapi.tables.TableReference;

final class InnerJoinTableReferenceImpl extends JoinedTableReferenceImpl implements InnerJoinTableReference {

    InnerJoinTableReferenceImpl(@NotNull TableReference left, @NotNull TableReference right, @NotNull Predicate predicate) {
        super(left, right, predicate);
    }

    @Override
    protected String getJoinName() {
        return "INNER JOIN";
    }
}
