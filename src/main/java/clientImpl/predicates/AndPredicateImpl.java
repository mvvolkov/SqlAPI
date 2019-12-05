package clientImpl.predicates;

import org.jetbrains.annotations.NotNull;
import sqlapi.predicates.AndPredicate;
import sqlapi.predicates.Predicate;

import java.util.ArrayDeque;

final class AndPredicateImpl extends CombinedPredicateImpl implements AndPredicate {

    AndPredicateImpl(@NotNull Predicate leftPredicate, @NotNull Predicate rightPredicate) {
        super(leftPredicate, rightPredicate);
    }

    @Override
    protected String getOperatorString() {
        return "AND";
    }
}
