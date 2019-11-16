package api.queries;

import api.columnExpr.ColumnRef;
import api.misc.SelectedItem;
import api.tables.TableReference;
import api.predicates.Predicate;

import java.util.List;

public interface SelectExpression {

    List<TableReference> getTableReferences();

    Predicate getPredicate();

    List<SelectedItem> getSelectedItems();

    List<ColumnRef> getGroupByColumns();

}
