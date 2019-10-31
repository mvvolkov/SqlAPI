package sqlapi;

import sqlapi.selectionPredicate.SelectionPredicate;

public interface SelectionExpressionBuilder {

    SelectionExpressionBuilder addTableReference(TableReference tableReference);

    SelectionExpressionBuilder addColumn(SelectedColumn selectedColumn);

    SelectionExpressionBuilder addPredicateWithAnd(SelectionPredicate selectionPredicate);

    SelectionExpressionBuilder addPredicateWithOr(SelectionPredicate selectionPredicate);

    SelectExpression build();
}
