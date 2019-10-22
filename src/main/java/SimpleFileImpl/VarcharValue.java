package SimpleFileImpl;

import sqlapi.SelectionCriteria;

public class VarcharValue extends Value {

    String value;

    public VarcharValue(Object value) throws Exception {
        if (!(value instanceof String)) {
            throw new Exception("");
        }
        this.value = (String) value;
    }

    public boolean evaluate(SelectionCriteria.BinaryPredicate bp) throws Exception {
        if (!(bp.getRight() instanceof String)){
            throw new Exception("");
        }
        String other = (String) bp.getRight();
        int compResult = value.compareTo(other);
        return false;
    }
}
