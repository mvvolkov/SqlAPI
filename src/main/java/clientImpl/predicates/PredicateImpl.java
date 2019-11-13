package clientImpl.predicates;

import api.predicates.Predicate;
import org.jetbrains.annotations.NotNull;

public abstract class PredicateImpl implements Predicate {

    @NotNull
    public final Type type;

    public PredicateImpl(@NotNull Type type) {
        this.type = type;
    }

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
    

    protected String getOperatorString() {
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
