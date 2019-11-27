package LocalFileDatabase.server.intermediateResult;

import sqlapi.columnExpr.ColumnRef;
import org.jetbrains.annotations.NotNull;
import sqlapi.metadata.SqlType;

import java.util.Objects;

public final class DataHeader {


    @NotNull
    private final String databaseName;


    @NotNull
    private final String tableName;

    @NotNull
    private final String columnName;


    private SqlType sqlType;


    public DataHeader(SqlType sqlType,
                      @NotNull String databaseName,
                      @NotNull String tableName,
                      @NotNull String columnName) {
        this.sqlType = sqlType;
        this.databaseName = databaseName;
        this.tableName = tableName;
        this.columnName = columnName;

    }


    public DataHeader(@NotNull String columnName) {
        this(null, "", "", columnName);
    }


    public DataHeader(@NotNull ColumnRef columnRef) {
        this.databaseName = columnRef.getDatabaseName();
        this.tableName = columnRef.getTableName();
        this.columnName = columnRef.getColumnName();
    }

    @NotNull
    public String getColumnName() {
        return columnName;
    }

    @NotNull
    public String getTableName() {
        return tableName;
    }


    public SqlType getSqlType() {
        return sqlType;
    }

    @NotNull
    public String getDatabaseName() {
        return databaseName;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof DataHeader)) {
            return false;
        }
        DataHeader cr = (DataHeader) obj;
        return Objects.equals(databaseName, cr.databaseName)
                && Objects.equals(tableName, cr.tableName)
                && Objects.equals(columnName, cr.columnName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(databaseName, tableName, columnName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(columnName);
        if (!databaseName.isEmpty()) {
            sb.insert(0, ".");
            sb.insert(0, databaseName);
        }
        if (!tableName.isEmpty()) {
            sb.insert(0, ".");
            sb.insert(0, tableName);
        }
        return sb.toString();
    }
}
