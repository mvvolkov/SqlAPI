package api.tables;

public interface TableReference {

    enum TableRefType {
        DATABASE_TABLE,
        INNER_JOIN,
        LEFT_OUTER_JOIN,
        RIGHT_OUTER_JOIN,
        TABLE_FROM_SELECT
    }

    TableRefType getTableRefType();
}
