package api;

import api.selectionPredicate.Predicate;

public interface SelectionExpressionBuilder {

    SelectionExpressionBuilder addTableReference(TableReference tableReference);

    SelectionExpressionBuilder addColumn(SelectedColumn selectedColumn);

    SelectionExpressionBuilder addPredicateWithAnd(Predicate selectionPredicate);

    SelectionExpressionBuilder addPredicateWithOr(Predicate selectionPredicate);

    SelectExpression build();
}
