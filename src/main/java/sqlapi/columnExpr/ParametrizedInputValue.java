package sqlapi.columnExpr;

public interface ParametrizedInputValue extends InputValue {

    void setValue(Object value);

    boolean hasValue();
}
