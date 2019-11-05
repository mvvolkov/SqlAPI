package api;

import clientDefaultImpl.SelectionPredicateImpl;

public interface SelectionExpressionBuilder {

    SelectionExpressionBuilder addTableReference(TableReference tableReference);

    SelectionExpressionBuilder addColumn(SelectedColumn selectedColumn);

    SelectionExpressionBuilder addPredicateWithAnd(SelectionPredicateImpl selectionPredicate);

    SelectionExpressionBuilder addPredicateWithOr(SelectionPredicateImpl selectionPredicate);

    SelectExpression build();
}
