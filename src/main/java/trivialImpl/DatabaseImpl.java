package trivialImpl;

import api.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class DatabaseImpl implements Database {

    private final String name;

    private final Collection<SqlTable> tables = new ArrayList<>();

    public DatabaseImpl(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public SqlTable getTableOrNull(String tableName) {
        for (SqlTable table : tables) {
            if (table.getName().equals(tableName)) {
                return table;
            }
        }
        return null;
    }

    private String getColumnDescriptionString(SqlColumnDescription description) {
        StringBuilder sb = new StringBuilder(description.getName());
        sb.append(" ");
        sb.append(description.getType().toString());
        if (description.getType().equals(SqlColumnType.VARCHAR)) {
            sb.append("(");
            sb.append(((SqlColumnDescription.VarcharColumn) description).getMaxLength());
            sb.append(")");
        }
        return sb.toString();
    }

    @Override
    public void createTable(SqlTableDescription tableDescription) {
        tables.add(new SqlTableImpl(this, tableDescription));
        String columns = tableDescription.getColumnsDescriptions().stream().map(this::getColumnDescriptionString).collect(Collectors.joining(", "));
        StringBuilder sb = new StringBuilder("CREATE TABLE ");
        sb.append(tableDescription.getName());
        sb.append("(");
        sb.append(columns);
        sb.append(");");
        System.out.println(sb);
    }

    @Override
    public void dropTable(String tableName) {
    }
}
