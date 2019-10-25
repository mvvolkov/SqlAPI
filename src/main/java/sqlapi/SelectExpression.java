package sqlapi;

import org.jetbrains.annotations.NotNull;
import sqlapi.selectionPredicate.EmptyPredicate;
import sqlapi.selectionPredicate.SelectionPredicate;

import java.util.ArrayList;
import java.util.List;

/**
 * Do we need a builder here?
 * builder.addTableReference(tableReference1).add(tableReference2)
 * .addUnit(selectionUnit1).addUnit(selectionUnit2).addPredicate(SelectionPredicate)
 */
public final class SelectExpression extends TableReference {

    @NotNull
    private final List<TableReference> tableReferences;
    @NotNull
    private final List<SelectionUnit> selectionUnits;
    @NotNull
    private final SelectionPredicate selectionPredicate;


    private SelectExpression(Builder builder) {
        super(builder.alias);
        tableReferences = builder.tableReferences;
        selectionUnits = builder.selectionUnits;
        selectionPredicate = builder.selectionPredicate;
    }

    @NotNull
    public List<TableReference> getTableReferences() {
        return tableReferences;
    }

    @NotNull
    public List<SelectionUnit> getSelectionUnits() {
        return selectionUnits;
    }

    @NotNull
    public SelectionPredicate getSelectionPredicate() {
        return selectionPredicate;
    }

    public final static class Builder {


        private List<TableReference> tableReferences = new ArrayList<>();
        private List<SelectionUnit> selectionUnits = new ArrayList<>();
        private SelectionPredicate selectionPredicate = new EmptyPredicate();
        private String alias = null;


        public Builder(@NotNull TableReference tableReference) {
            tableReferences.add(tableReference);
        }

        public Builder addTableReference(@NotNull TableReference tableReference) {
            tableReferences.add(tableReference);
            return this;
        }

        public Builder addColumn(@NotNull SelectionUnit selectionUnit) {
            selectionUnits.add(selectionUnit);
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

        public SelectExpression build() {
            return new SelectExpression(this);
        }
    }

    public static Builder builder(TableReference tableReference) {
        return new Builder(tableReference);
    }
}
