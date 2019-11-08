package api.selectionPredicate;

import api.ColumnReference;

import java.util.List;

public interface ColumnInPredicate extends Predicate {


    ColumnReference getColumnReference();

    List<?> getValues();

    @Override
    default Type getType() {
        return Type.IN;
    }

}
