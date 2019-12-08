package sqlapi.queries;

import sqlapi.metadata.TableMetadata;

import java.util.Collection;

public interface ValidateDatabaseQuery extends DatabaseQuery {

    Collection<TableMetadata> getTables();
}
