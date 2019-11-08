package api;

import api.selectionPredicate.Predicate;

public interface DeleteStatement extends SqlStatement {

    Predicate getPredicate();

    @Override
    default Type getType() {
        return Type.DELETE;
    }
}
