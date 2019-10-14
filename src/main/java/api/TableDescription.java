package api;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public final class TableDescription {

    @NotNull
    private final String name;

    @NotNull
    private final List<ColumnDescription> columnDescriptions;


    public TableDescription(@NotNull String name, @NotNull List<ColumnDescription> columnDescriptions) {
        this.name = name;
        this.columnDescriptions = columnDescriptions;
    }

    public List<ColumnDescription> getColumnDescriptions() {
        return columnDescriptions;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + "(" + this.getColumnsDescriptionString() + ")";
    }

    private String getColumnsDescriptionString() {
        return columnDescriptions.stream().map(ColumnDescription::toString).collect(Collectors.joining(", "));
    }
}
