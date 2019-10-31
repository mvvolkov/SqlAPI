package testSimpleFileImpl;

import org.jetbrains.annotations.NotNull;
import sqlapi.SelectExpression;
import sqlapi.SelectedColumn;
import sqlapi.SelectionExpressionBuilder;
import sqlapi.TableReference;
import sqlapi.selectionPredicate.TruePredicate;
import sqlapi.selectionPredicate.SelectionPredicate;

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
    private final SelectionPredicate selectionPredicate;


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
    public SelectionPredicate getSelectionPredicate() {
        return selectionPredicate;
    }

    public final static class Builder implements SelectionExpressionBuilder {


        private List<TableReference> tableReferences = new ArrayList<>();
        private List<SelectedColumn> selectedColumns = new ArrayList<>();
        private SelectionPredicate selectionPredicate = new TruePredicate();
        private String alias = null;


        public Builder(@NotNull TableReference tableReference) {
            tableReferences.add(tableReference);
        }

        public Builder addTableReference(@NotNull TableReference tableReference) {
            tableReferences.add(tableReference);
            return this;
        }

        public Builder addColumn(@NotNull SelectedColumn selectedColumn) {
            selectedColumns.add(selectedColumn);
            return this;
        }

        public Builder addPredicateWithAnd(@NotNull SelectionPredicate selectionPredicate) {
            this.selectionPredicate = SelectionPredicate.and(this.selectionPredicate, selectionPredicate);
            return this;
        }

        public Builder addPredicateWithOr(@NotNull SelectionPredicate selectionPredicate) {
            this.selectionPredicate = SelectionPredicate.or(this.selectionPredicate, selectionPredicate);
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
