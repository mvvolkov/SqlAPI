package api;

public interface TableReference {

    enum Type {
        BASE_TABLE,
        INNER_JOIN,
        LEFT_OUTER_JOIN,
        RIGHT_OUTER_JOIN,
        SELECT_EXPRESSION
    }

    Type getType();
}
