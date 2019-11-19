package sqlapi.queries;

import sqlapi.columnExpr.ColumnRef;
import sqlapi.misc.SelectedItem;
import sqlapi.tables.TableReference;
import sqlapi.predicates.Predicate;

import java.util.List;

public interface SelectExpression {

    List<TableReference> getTableReferences();

    Predicate getPredicate();

    List<SelectedItem> getSelectedItems();

    List<ColumnRef> getGroupByColumns();

}
