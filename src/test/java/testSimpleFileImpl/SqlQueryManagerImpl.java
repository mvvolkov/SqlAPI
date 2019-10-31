package testSimpleFileImpl;

import sqlapi.ColumnMetadata;
import sqlapi.ColumnMetadataBuilder;
import sqlapi.SqlQueryManager;
import sqlapi.TableMetadata;
import sqlapi.TableReference;
import sqlapi.selectionPredicate.SelectionPredicate;

import java.util.List;

public class SqlQueryManagerImpl implements SqlQueryManager {

    @Override
    public TableMetadata tableMetadata(String tableName, List<ColumnMetadata> columnsMetadata) {
        return new TableMetadataImpl(tableName, columnsMetadata);
    }

    @Override
    public ColumnMetadataBuilder getIntegerColumnMetadataBuilder(String columnName) {
        return IntegerColumnMetadataImpl.builder(columnName);
    }

    @Override
    public ColumnMetadataBuilder getVarcharColumnMetadataBuilder(String columnName, int maxLength) {
        return VarcharColumnMetadataImpl.builder(columnName, maxLength);
    }

    @Override
    public TableReference baseTableRef(String tableName, String databaseName) {
        return new BaseTableReferenceImpl(tableName, databaseName);
    }

    @Override
    public TableReference innerJoin(TableReference left, TableReference right, SelectionPredicate selectionPredicate) {
        return new JoinTableReference(JoinTableReference.Type.INNER_JOIN, left, right, selectionPredicate);
    }


    @Override
    public TableReference leftOuterJoin(TableReference left, TableReference right, SelectionPredicate selectionPredicate) {
        return new JoinTableReference(JoinTableReference.Type.LEFT_OUTER_JOIN, left, right, selectionPredicate);
    }

    @Override
    public TableReference rightOuterJoin(TableReference left, TableReference right, SelectionPredicate selectionPredicate) {
        return new JoinTableReference(JoinTableReference.Type.RIGHT_OUTER_JOIN, left, right, selectionPredicate);
    }
}
