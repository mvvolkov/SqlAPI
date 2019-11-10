package clientImpl.queries;

import api.queries.SelectExpression;
import api.SelectedItem;
import api.TableReference;
import api.predicates.Predicate;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public final class SelectExpressionImpl implements SelectExpression {

    @NotNull
    private final List<TableReference> tableReferences;
    @NotNull
    private final List<SelectedItem> selectedItems;
    @NotNull
    private final Predicate predicate;


    public SelectExpressionImpl(List<TableReference> tableReferences,
                                List<SelectedItem> selectedItems, Predicate predicate) {
        this.tableReferences = tableReferences;
        this.selectedItems = selectedItems;
        this.predicate = predicate;
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
    public Predicate getPredicate() {
        return predicate;
    }


}
