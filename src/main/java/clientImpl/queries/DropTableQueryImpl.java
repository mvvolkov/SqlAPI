package clientImpl.queries;

import org.jetbrains.annotations.NotNull;
import sqlapi.queries.DropTableQuery;

final class DropTableQueryImpl extends TableActionQueryImpl implements DropTableQuery {

    DropTableQueryImpl(@NotNull String databaseName, @NotNull String tableName) {
        super(databaseName, tableName);
    }

    @Override
    public String toString() {
        return "DROP TABLE " + this.getDatabaseName() + "." + this.getTableName();
    }
}
