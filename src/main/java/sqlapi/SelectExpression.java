package sqlapi;

import sqlapi.selectionPredicate.SelectionPredicate;

import java.util.List;

public interface SelectExpression extends TableReference {


    public List<TableReference> getTableReferences();
    
    public List<SelectedColumn> getSelectedColumns();

    public SelectionPredicate getSelectionPredicate();
}
