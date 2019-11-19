package sqlapi.tables;

public interface TableReference {

    enum TableRefType {
        DATABASE_TABLE,
        INNER_JOIN,
        LEFT_OUTER_JOIN,
        RIGHT_OUTER_JOIN,
        SELECT_SUBQUERY
    }

    TableRefType getTableRefType();
}
