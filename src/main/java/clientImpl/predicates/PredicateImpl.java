package clientImpl.predicates;

import sqlapi.predicates.Predicate;
import org.jetbrains.annotations.NotNull;

abstract class PredicateImpl implements Predicate {

    @NotNull
    private final Type type;

    PredicateImpl(@NotNull Type type) {
        this.type = type;
    }

    @NotNull
    @Override
    public Type getType() {
        return type;
    }

    @Override
    public Predicate and(Predicate predicate) {
        return new CombinedPredicateImpl(Type.AND, this, predicate);
    }

    @Override
    public Predicate or(Predicate predicate) {
        return new CombinedPredicateImpl(Type.OR, this, predicate);
    }


    String getOperatorString() {
        switch (type) {
            case IN:
                return "IN";
            case EQUALS:
                return "=";
            case NOT_EQUALS:
                return "!=";
            case GREATER_THAN:
                return ">";
            case GREATER_THAN_OR_EQUALS:
                return ">=";
            case LESS_THAN:
                return "<";
            case LESS_THAN_OR_EQUALS:
                return "<=";
            case AND:
                return "AND";
            case OR:
                return "OR";
            default:
                return "";
        }
    }
}
