package clientImpl.predicates;

import sqlapi.predicates.Predicate;
import org.jetbrains.annotations.NotNull;

abstract class PredicateImpl implements Predicate {


    @NotNull
    @Override
    public Predicate and(@NotNull Predicate predicate) {
        return new AndPredicateImpl(this, predicate);
    }

    @NotNull
    @Override
    public Predicate or(@NotNull Predicate predicate) {
        return new OrPredicateImpl(this, predicate);
    }

}
