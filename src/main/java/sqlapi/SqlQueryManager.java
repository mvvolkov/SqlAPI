package sqlapi;

import sqlapi.selectionPredicate.SelectionPredicate;

import java.util.List;

public interface SqlQueryManager {

    TableMetadata tableMetadata(String tableName, List<ColumnMetadata> columnsMetadata);

    ColumnMetadataBuilder getIntegerColumnMetadataBuilder(String columnName);

    ColumnMetadataBuilder getVarcharColumnMetadataBuilder(String columnName, int maxLength);

    TableReference baseTableRef(String tableName, String databaseName);

    TableReference innerJoin(TableReference left, TableReference right, SelectionPredicate selectionPredicate);

    TableReference leftOuterJoin(TableReference left, TableReference right, SelectionPredicate selectionPredicate);

    TableReference rightOuterJoin(TableReference left, TableReference right, SelectionPredicate selectionPredicate);
}
