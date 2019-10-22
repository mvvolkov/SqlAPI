package SimpleFileImpl;

import sqlapi.SelectionCriteria;

public class IntegerValue extends Value {
    Integer value;

    public IntegerValue(Object value) throws Exception {
        if (!(value instanceof Integer)) {
            throw new Exception("");
        }
        this.value = (Integer) value;
    }

    public boolean evaluate(SelectionCriteria.BinaryPredicate bp) throws Exception {
        if (!(bp.getRight() instanceof Integer)){
            throw new Exception("");
        }
        Integer other = (Integer) bp.getRight();
        int compResult = value.compareTo(other);
        return false;
    }
}
