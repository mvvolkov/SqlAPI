package api.queries;

import api.SelectedItem;
import api.TableReference;
import api.predicates.Predicate;

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
