package api;

import api.selectionPredicate.Predicate;

import java.util.List;

public interface SelectExpression extends TableReference {

    List<TableReference> getTableReferences();

    Predicate getPredicate();

    List<SelectedItem> getSelectedItems();

    @Override
    default Type getType() {
        return Type.SELECT_EXPRESSION;
    }
}
