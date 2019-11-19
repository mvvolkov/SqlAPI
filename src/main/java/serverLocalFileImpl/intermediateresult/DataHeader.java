package serverLocalFileImpl.intermediateresult;

import sqlapi.columnExpr.ColumnRef;
import org.jetbrains.annotations.NotNull;
import sqlapi.metadata.SqlType;

import java.util.Objects;

public final class DataHeader {

    @NotNull
    private final String schemaName;

    @NotNull
    private final String tableName;

    @NotNull
    private final String columnName;


    SqlType sqlType;


    public DataHeader(SqlType sqlType, @NotNull String schemaName,
                      @NotNull String tableName,
                      @NotNull String columnName) {
        this.schemaName = schemaName;
        this.tableName = tableName;
        this.columnName = columnName;
        this.sqlType = sqlType;
    }


    public DataHeader(@NotNull String tableName,
                      @NotNull String columnName) {
        this(null, "", tableName, columnName);
    }

    public DataHeader(@NotNull String columnName) {
        this(null, "", "", columnName);
    }


    public DataHeader(@NotNull ColumnRef columnRef) {
        this.schemaName = columnRef.getSchemaName();
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

    @NotNull
    public String getSchemaName() {
        return schemaName;
    }

    public SqlType getSqlType() {
        return sqlType;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof DataHeader)) {
            return false;
        }
        DataHeader cr = (DataHeader) obj;
        return Objects.equals(schemaName, cr.schemaName) &&
                Objects.equals(tableName, cr.tableName)
                && Objects.equals(columnName, cr.columnName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schemaName, tableName, columnName);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(columnName);
        if (!tableName.isEmpty()) {
            sb.insert(0, ".");
            sb.insert(0, tableName);
        }
        if (!schemaName.isEmpty()) {
            sb.insert(0, ".");
            sb.insert(0, schemaName);
        }
        return sb.toString();
    }
}
