package serverLocalFileImpl;

import api.columnExpr.ColumnRef;

import java.util.Objects;

public class ColumnRefImpl implements ColumnRef {

    private final String dbName;
    private final String tableName;
    private final String columnName;

    public ColumnRefImpl(String dbName, String tableName, String columnName) {
        this.dbName = dbName;
        this.tableName = tableName;
        this.columnName = columnName;
    }

    public ColumnRefImpl(ColumnRef columnRef) {
        this.dbName = columnRef.getDatabaseName();
        this.tableName = columnRef.getTableName();
        this.columnName = columnRef.getColumnName();
    }

    @Override
    public String getColumnName() {
        return columnName;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public String getDatabaseName() {
        return dbName;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ColumnRefImpl)) {
            return false;
        }
        ColumnRefImpl o = (ColumnRefImpl) obj;
        return Objects.equals(dbName, o.dbName) && Objects.equals(tableName, o.tableName)
                && Objects.equals(columnName, o.columnName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dbName, tableName, columnName);
    }
}
