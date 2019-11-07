package api;

import api.selectionPredicate.Predicate;

public interface SelectionExpressionBuilder {

    SelectionExpressionBuilder addTableReference(TableReference tableReference);

    SelectionExpressionBuilder addSelectedItem(SelectedItem selectedItem);

    SelectionExpressionBuilder addPredicate(Predicate predicate);

    SelectExpression build();
}
