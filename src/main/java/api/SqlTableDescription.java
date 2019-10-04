package api;

import java.util.List;

public final class SqlTableDescription {
    private final List<SqlColumnDescription> columns;
    private final String name;

    public SqlTableDescription(String name, List<SqlColumnDescription> columns) {
        this.name = name;
        this.columns = columns;
    }

    public List<SqlColumnDescription> getColumnsDescriptions() {
        return columns;
    }

    public String getName() {
        return name;
    }
}
