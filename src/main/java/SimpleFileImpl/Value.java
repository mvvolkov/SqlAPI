package SimpleFileImpl;

import sqlapi.SelectionCriteria;

public abstract class Value {

    public boolean evaluate(SelectionCriteria.BinaryPredicate bp) throws Exception {
        return false;
    }
}
