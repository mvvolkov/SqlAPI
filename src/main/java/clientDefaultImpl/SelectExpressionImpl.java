package clientDefaultImpl;

import api.SelectExpression;
import api.SelectedItem;
import api.SelectionExpressionBuilder;
import api.TableReference;
import api.selectionPredicate.Predicate;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Do we need a builder here?
 * builder.addTableReference(tableReference1).add(tableReference2)
 * .addUnit(selectionUnit1).addUnit(selectionUnit2).addPredicate(SelectionPredicate)
 */
public final class SelectExpressionImpl implements SelectExpression {

    @NotNull
    private final List<TableReference> tableReferences;
    @NotNull
    private final List<SelectedItem> selectedItems;
    @NotNull
    private final Predicate predicate;


    private SelectExpressionImpl(Builder builder) {
        tableReferences = builder.tableReferences;
        if (builder.selectedItems.isEmpty()) {
            selectedItems = Arrays.asList(new SelectedAllImpl());
        } else {
            selectedItems = builder.selectedItems;
        }
        predicate = builder.predicate;
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

    public final static class Builder implements SelectionExpressionBuilder {

        private List<TableReference> tableReferences = new ArrayList<>();
        private List<SelectedItem> selectedItems = new ArrayList<>();
        private Predicate predicate = new SelectionPredicateImpl(Predicate.Type.TRUE);
        private String alias = null;


        public Builder(@NotNull TableReference tableReference) {
            tableReferences.add(tableReference);
        }

        @Override
        public Builder addTableReference(@NotNull TableReference tableReference) {
            tableReferences.add(tableReference);
            return this;
        }

        @Override
        public Builder addSelectedItem(@NotNull SelectedItem selectedItem) {
            selectedItems.add(selectedItem);
            return this;
        }

        @Override
        public Builder addPredicate(@NotNull Predicate selectionPredicate) {
            if (this.predicate.isTrue()) {
                this.predicate = selectionPredicate;
            } else {
                this.predicate = new CombinedPredicateImpl(Predicate.Type.AND,
                        this.predicate, selectionPredicate);
            }
            return this;
        }


        public SelectExpressionImpl build() {
            return new SelectExpressionImpl(this);
        }
    }

    public static Builder builder(TableReference tableReference) {
        return new Builder(tableReference);
    }
}
