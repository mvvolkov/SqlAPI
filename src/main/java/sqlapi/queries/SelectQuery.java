package sqlapi.queries;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ColumnRef;
import sqlapi.assignment.SelectedItem;
import sqlapi.tables.TableReference;
import sqlapi.predicates.Predicate;

import java.util.List;

public interface SelectQuery {

    @NotNull List<TableReference> getTableReferences();

    @NotNull Predicate getPredicate();

    @NotNull List<SelectedItem> getSelectedItems();

    @NotNull List<ColumnRef> getGroupByColumns();
}
