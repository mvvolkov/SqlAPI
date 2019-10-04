package api;

import java.util.List;

public interface SqlManager {

    Database createDatabase(String dbName);

    void openDatabaseWithTables(String dbName, List<SqlTableDescription> tables);

    void persistDatabase(String dbName);

    Database getDatabaseOrNull(String dbName);

    List<SelectionResultRow> select(JoinTableOperation joinOperation, List<SelectionUnit> selectionUnits, SelectionCondition selectionCondition);
}
