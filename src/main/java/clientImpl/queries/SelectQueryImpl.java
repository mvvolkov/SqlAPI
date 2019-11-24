package clientImpl.queries;

import sqlapi.columnExpr.ColumnRef;
import sqlapi.queries.SelectQuery;
import sqlapi.misc.SelectedItem;
import sqlapi.tables.TableReference;
import sqlapi.predicates.Predicate;
import org.jetbrains.annotations.NotNull;

import java.util.List;


final class SelectQueryImpl implements SelectQuery {

    @NotNull
    private final List<TableReference> tableReferences;
    @NotNull
    private final List<SelectedItem> selectedItems;
    @NotNull
    private final Predicate predicate;
    @NotNull
    private final List<ColumnRef> groupByColumns;


    SelectQueryImpl(@NotNull List<TableReference> tableReferences,
                           @NotNull List<SelectedItem> selectedItems,
                           @NotNull Predicate predicate,
                           @NotNull List<ColumnRef> groupByColumns) {
        this.tableReferences = tableReferences;
        this.selectedItems = selectedItems;
        this.predicate = predicate;
        this.groupByColumns = groupByColumns;
    }

    @NotNull
    @Override
    public List<TableReference> getTableReferences() {
        return tableReferences;
    }

    @NotNull
    @Override
    public List<SelectedItem> getSelectedItems() {
        return selectedItems;
    }

    @NotNull
    @Override
    public List<ColumnRef> getGroupByColumns() {
        return groupByColumns;
    }

    @NotNull
    @Override
    public Predicate getPredicate() {
        return predicate;
    }


}
