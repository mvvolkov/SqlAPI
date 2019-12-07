package clientImpl.queries;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnExpression;
import sqlapi.columnExpr.ColumnRef;
import sqlapi.misc.SelectedItem;
import sqlapi.predicates.Predicate;
import sqlapi.queries.SelectQuery;
import sqlapi.tables.DatabaseTableReference;
import sqlapi.tables.TableReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


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


    @Override public void setParameters(Object... values) {
        List<Object> parameters = new ArrayList<>(Arrays.asList(values));
        for (SelectedItem selectedItem : selectedItems) {
            selectedItem.setParameters(parameters);
        }
        for (TableReference tableReference : tableReferences) {
            tableReference.setParameters(parameters);
        }
        predicate.setParameters(parameters);
    }

    @NotNull
    @Override
    public Predicate getPredicate() {
        return predicate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SELECT ");
        if (selectedItems.isEmpty()) {
            sb.append("*");
        } else {
            List<String> columns = new ArrayList<>();
            for (SelectedItem se : selectedItems) {
                if (se instanceof ColumnExpression) {
                    columns.add("" + se.toString());
                }
                if (se instanceof DatabaseTableReference) {
                    columns.add(se + ".*");
                }
            }
            String from = String.join(", ", columns);
            sb.append(from);
        }
        sb.append(" FROM ");
        String tables = tableReferences.stream().
                map(TableReference::toString)
                .collect(Collectors.joining(", "));
        sb.append(tables);
        if (!predicate.isEmpty()) {
            sb.append(" WHERE ");
            sb.append(predicate);
        }
        if (!groupByColumns.isEmpty()) {
            sb.append(" GROUP BY ");
            sb.append(groupByColumns.stream().map(ColumnRef::toString)
                    .collect(Collectors.joining(", ")));
        }
        sb.append(";");
        return sb.toString();
    }
}
