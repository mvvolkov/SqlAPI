package api.queries;

import api.SelectedItem;
import api.tables.TableReference;
import api.predicates.Predicate;

import java.util.List;

public interface SelectExpression extends TableReference {

    List<TableReference> getTableReferences();

    Predicate getPredicate();

    List<SelectedItem> getSelectedItems();

    @Override
    default TableRefType getTableRefType() {
        return TableRefType.SELECT_EXPRESSION;
    }
}
