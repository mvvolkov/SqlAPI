package clientImpl.predicates;

import org.jetbrains.annotations.NotNull;
import sqlapi.predicates.combined.OrPredicate;
import sqlapi.predicates.Predicate;

final class OrPredicateImpl extends CombinedPredicateImpl implements OrPredicate {

    OrPredicateImpl(@NotNull Predicate leftPredicate, @NotNull Predicate rightPredicate) {
        super(leftPredicate, rightPredicate);
    }

    @Override
    protected String getOperatorString() {
        return "OR";
    }
}
