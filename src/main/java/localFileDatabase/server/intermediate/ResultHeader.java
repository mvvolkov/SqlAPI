package localFileDatabase.server.intermediate;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnExpression;
import sqlapi.columnExpr.ColumnRef;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ResultHeader {


    @NotNull
    private final String databaseName;


    @NotNull
    private final String tableName;

    @NotNull
    private final String columnName;


    public ResultHeader(@NotNull String databaseName,
                        @NotNull String tableName,
                        @NotNull String columnName) {
        this.databaseName = databaseName;
        this.tableName = tableName;
        this.columnName = columnName;

    }


    public ResultHeader(@NotNull String columnName) {
        this("", "", columnName);
    }

    public ResultHeader() {
        this("", "", "");
    }


    public ResultHeader(@NotNull ColumnRef columnRef) {
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


    @NotNull
    public String getDatabaseName() {
        return databaseName;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof ResultHeader)) {
            return false;
        }
        ResultHeader cr = (ResultHeader) obj;
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

    public static List<ResultHeader> getSelectedColumns(
            List<ColumnExpression> columnExpressions) {

        List<ResultHeader> resultColumns = new ArrayList<>();
        for (ColumnExpression columnExpression : columnExpressions) {
            if (!columnExpression.getAlias().isEmpty()) {
                resultColumns
                        .add(new ResultHeader((columnExpression).getAlias()));
            } else if (columnExpression instanceof ColumnRef) {
                resultColumns.add(new ResultHeader((ColumnRef) columnExpression));
            } else {
                resultColumns.add(new ResultHeader(columnExpression.toString()));
            }
        }
        return resultColumns;
    }
}
