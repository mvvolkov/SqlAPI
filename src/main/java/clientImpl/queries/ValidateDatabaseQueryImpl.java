package clientImpl.queries;

import org.jetbrains.annotations.NotNull;
import sqlapi.metadata.TableMetadata;
import sqlapi.queries.ValidateDatabaseQuery;

import java.util.Collection;

public class ValidateDatabaseQueryImpl implements ValidateDatabaseQuery {

    private final String databaseName;

    private final Collection<TableMetadata> tables;

    public ValidateDatabaseQueryImpl(String databaseName,
                                     Collection<TableMetadata> tables) {
        this.databaseName = databaseName;
        this.tables = tables;
    }

    @Override public Collection<TableMetadata> getTables() {
        return tables;
    }

    @Override public @NotNull String getDatabaseName() {
        return databaseName;
    }
}
