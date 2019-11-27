package clientImpl.queries;

import clientImpl.predicates.PredicateFactory;
import org.jetbrains.annotations.NotNull;
import sqlapi.misc.AssignmentOperation;
import sqlapi.misc.SelectedItem;
import sqlapi.columnExpr.ColumnRef;
import sqlapi.metadata.TableMetadata;
import sqlapi.predicates.Predicate;
import sqlapi.queries.*;
import sqlapi.tables.TableReference;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class QueryFactory {

    private QueryFactory() {
    }

    public static @NotNull CreateDatabaseQuery createDatabase(@NotNull String databaseName) {
        return new CreateDatabaseQueryImpl(databaseName);
    }

    public static @NotNull CreateTableQuery createTable(@NotNull String databaseName,
                                                        @NotNull TableMetadata tableMetadata) {
        return new CreateTableQueryImpl(databaseName, tableMetadata);
    }

    public static @NotNull DropTableQuery dropTable(@NotNull String databaseName,
                                                    @NotNull String tableName) {
        return new DropTableQueryImpl(databaseName, tableName);
    }

    public static @NotNull InsertQuery insert(@NotNull String databaseName,
                                              @NotNull String tableName,
                                              @NotNull List<String> columns,
                                              @NotNull List<Object> values) {
        return new @NotNull InsertQueryImpl(databaseName, tableName, columns, values);
    }

    public static @NotNull InsertQuery insert(@NotNull String databaseName,
                                              @NotNull String tableName,
                                              @NotNull List<Object> values) {
        return insert(databaseName, tableName, Collections.emptyList(), values);
    }

    public static @NotNull InsertFromSelectQueryImpl insert(@NotNull String databaseName,
                                                            @NotNull String tableName,
                                                            @NotNull List<String> columns,
                                                            @NotNull SelectQuery selectQuery) {
        return new InsertFromSelectQueryImpl(databaseName, tableName, columns,
                selectQuery);
    }

    public static @NotNull InsertFromSelectQueryImpl insert(@NotNull String databaseName,
                                                            @NotNull String tableName,
                                                            @NotNull SelectQuery selectQuery) {
        return insert(databaseName, tableName, Collections.emptyList(),
                selectQuery);
    }

    public static @NotNull DeleteQuery delete(@NotNull String databaseName,
                                              @NotNull String tableName,
                                              @NotNull Predicate predicate) {
        return new DeleteQueryImpl(databaseName, tableName, predicate);
    }

    public static @NotNull DeleteQuery delete(@NotNull String databaseName,
                                              @NotNull String tableName) {
        return delete(databaseName, tableName, PredicateFactory.empty());
    }


    public static @NotNull UpdateQuery update(@NotNull String databaseName,
                                              @NotNull String tableName,
                                              @NotNull Collection<AssignmentOperation> assignmentOperations,
                                              @NotNull Predicate predicate) {
        return new UpdateQueryImpl(databaseName, tableName, assignmentOperations,
                predicate);
    }

    public static @NotNull UpdateQuery update(@NotNull String databaseName,
                                              @NotNull String tableName,
                                              @NotNull Collection<AssignmentOperation> assignmentOperations) {
        return update(databaseName, tableName, assignmentOperations,
                PredicateFactory.empty());
    }


    public static @NotNull SelectQuery select(
            @NotNull List<TableReference> tableReferences,
            @NotNull List<SelectedItem> selectedItems,
            @NotNull Predicate predicate) {
        return new SelectQueryImpl(tableReferences, selectedItems, predicate,
                Collections.emptyList());
    }

    public static @NotNull SelectQuery select(@NotNull TableReference tableReference,
                                              @NotNull List<SelectedItem> selectedItems,
                                              @NotNull Predicate predicate) {
        return select(Collections.singletonList(tableReference), selectedItems,
                predicate);
    }


    public static @NotNull SelectQuery select(
            @NotNull List<TableReference> tableReferences,
            @NotNull Predicate predicate) {
        return select(tableReferences, Collections.emptyList(), predicate);
    }


    public static @NotNull SelectQuery select(
            @NotNull TableReference tableReference,
            @NotNull Predicate predicate) {
        return select(Collections.singletonList(tableReference), predicate);
    }

    public static @NotNull SelectQuery select(
            @NotNull List<TableReference> tableReferences,
            @NotNull List<SelectedItem> selectedItems) {
        return select(tableReferences, selectedItems, PredicateFactory.empty());
    }

    public static @NotNull SelectQuery select(
            @NotNull TableReference tableReference,
            @NotNull List<SelectedItem> selectedItems) {
        return select(Collections.singletonList(tableReference), selectedItems);
    }

    public static @NotNull SelectQuery select(
            @NotNull List<TableReference> tableReferences) {
        return select(tableReferences, Collections.emptyList());
    }

    public static @NotNull SelectQuery select(
            @NotNull TableReference tableReference) {
        return select(Collections.singletonList(tableReference));
    }


    public static SelectQuery selectGrouped(
            @NotNull List<TableReference> tableReferences,
            @NotNull List<SelectedItem> selectedItems,
            @NotNull Predicate predicate, @NotNull List<ColumnRef> groupByColumns) {
        return new SelectQueryImpl(tableReferences, selectedItems, predicate,
                groupByColumns);
    }

    public static @NotNull SelectQuery selectGrouped(
            @NotNull TableReference tableReference,
            @NotNull List<SelectedItem> selectedItems,
            @NotNull Predicate predicate,
            @NotNull List<ColumnRef> groupByColumns) {
        return selectGrouped(Collections.singletonList(tableReference), selectedItems,
                predicate, groupByColumns);
    }


    public static @NotNull SelectQuery selectGrouped(
            @NotNull List<TableReference> tableReferences,
            @NotNull List<SelectedItem> selectedItems,
            @NotNull List<ColumnRef> groupByColumns) {
        return selectGrouped(tableReferences, selectedItems, PredicateFactory.empty(),
                groupByColumns);
    }


    public static @NotNull SelectQuery selectGrouped(
            @NotNull TableReference tableReference,
            @NotNull List<SelectedItem> selectedItems,
            @NotNull List<ColumnRef> groupByColumns) {
        return selectGrouped(tableReference, selectedItems, PredicateFactory.empty(),
                groupByColumns);
    }
}
