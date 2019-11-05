package api;

import api.selectionPredicate.Predicate;
import clientDefaultImpl.SelectionPredicateImpl;

import java.util.List;

public interface SelectExpression extends TableReference {
    
    public List<TableReference> getTableReferences();

    public Predicate getSelectionPredicate();

    public List<SelectedColumn> getSelectedColumns();
}
