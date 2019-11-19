package sqlapi.queries;

import sqlapi.predicates.Predicate;

public interface DeleteStatement extends SqlStatement {

    Predicate getPredicate();

    @Override
    default Type getType() {
        return Type.DELETE;
    }
}
