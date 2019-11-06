package api.selectionPredicate;

public interface Predicate {

    enum Type {
        TRUE,
        FALSE,
        IS_NULL,
        IS_NOT_NULL,
        EQUALS,
        NOT_EQUALS,
        GREATER_THAN,
        GREATER_THAN_OR_EQUALS,
        LESS_THAN,
        LESS_THAN_OR_EQUALS,
        AND,
        OR,
        IN
    }

    Type getType();

    default boolean isTrue() {
        return this.getType() == Type.TRUE;
    }

    Predicate and(Predicate predicate);

    Predicate or(Predicate predicate);
}
