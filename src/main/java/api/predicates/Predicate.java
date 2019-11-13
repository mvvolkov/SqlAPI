package api.predicates;

public interface Predicate {

    enum Type {
        EMPTY,
        EQUALS,
        NOT_EQUALS,
        GREATER_THAN,
        GREATER_THAN_OR_EQUALS,
        LESS_THAN,
        LESS_THAN_OR_EQUALS,
        AND,
        OR,
        IN,
        IS_NULL,
        IS_NOT_NULL
    }

    Type getType();

    default boolean isEmpty() {
        return this.getType() == Type.EMPTY;
    }

    Predicate and(Predicate predicate);

    Predicate or(Predicate predicate);
}
