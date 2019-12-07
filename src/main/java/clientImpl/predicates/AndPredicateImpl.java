package clientImpl.predicates;

import org.jetbrains.annotations.NotNull;
import sqlapi.predicates.combined.AndPredicate;
import sqlapi.predicates.Predicate;

final class AndPredicateImpl extends CombinedPredicateImpl implements AndPredicate {

    AndPredicateImpl(@NotNull Predicate leftPredicate, @NotNull Predicate rightPredicate) {
        super(leftPredicate, rightPredicate);
    }

    @Override
    protected String getOperatorString() {
        return "AND";
    }
}
