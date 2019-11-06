package clientDefaultImpl;

import api.selectionPredicate.Predicate;
import org.jetbrains.annotations.NotNull;
import api.SelectExpression;
import api.SelectedColumn;
import api.SelectionExpressionBuilder;
import api.TableReference;

import java.util.ArrayList;
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
    private final List<SelectedColumn> selectedColumns;
    @NotNull
    private final SelectionPredicateImpl selectionPredicate;


    private SelectExpressionImpl(Builder builder) {
        tableReferences = builder.tableReferences;
        selectedColumns = builder.selectedColumns;
        selectionPredicate = builder.selectionPredicate;
    }

    @NotNull
    @Override
    public List<TableReference> getTableReferences() {
        return tableReferences;
    }

    @NotNull
    public List<SelectedColumn> getSelectedColumns() {
        return selectedColumns;
    }

    @NotNull
    public SelectionPredicateImpl getSelectionPredicate() {
        return selectionPredicate;
    }

    public final static class Builder implements SelectionExpressionBuilder {


        private List<TableReference> tableReferences = new ArrayList<>();
        private List<SelectedColumn> selectedColumns = new ArrayList<>();
        private SelectionPredicateImpl selectionPredicate = new SelectionPredicateImpl(Predicate.Type.TRUE);
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
        public Builder addColumn(@NotNull SelectedColumn selectedColumn) {
            selectedColumns.add(selectedColumn);
            return this;
        }

        @Override
        public Builder addPredicateWithAnd(@NotNull Predicate selectionPredicate) {
            this.selectionPredicate = new CombinedPredicateImpl(Predicate.Type.AND,
                    this.selectionPredicate, selectionPredicate);
            return this;
        }

        public Builder addPredicateWithOr(@NotNull Predicate selectionPredicate) {
            this.selectionPredicate = new CombinedPredicateImpl(Predicate.Type.OR,
                    this.selectionPredicate, selectionPredicate);
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
