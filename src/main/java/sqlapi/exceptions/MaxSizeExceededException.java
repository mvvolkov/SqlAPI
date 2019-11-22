package sqlapi.exceptions;

import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MaxSizeExceededException extends SqlException {

    @NotNull
    private final String databaseName;

    @NotNull
    private final String tableName;

    @NotNull
    private final String columnName;


    private final int maxSize;

    private final int actualSize;


    public MaxSizeExceededException(
            String databaseName, String tableName, String columnName, int maxSize, int actualSize) {
        this.databaseName = databaseName;
        this.tableName = tableName;
        this.columnName = columnName;
        this.maxSize = maxSize;
        this.actualSize = actualSize;
    }

    @Override
    public String getMessage() {
        return "Max size exceeded: column = "
                + Stream.of(databaseName, tableName, columnName)
                .collect(Collectors.joining(".")) +
                "; max size = " + maxSize + "actual size = " + actualSize;
    }


    public String getDatabaseName() {
        return databaseName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int getActualSize() {
        return actualSize;
    }

}
