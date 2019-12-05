package clientImpl.columnExpr;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ParametrizedInputValue;

public class ParametrizedInputValueImpl extends InputValueImpl implements ParametrizedInputValue {

    private boolean hasValue;

    ParametrizedInputValueImpl(@NotNull String alias) {
        super(null, alias);
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public boolean hasValue() {
        return hasValue;
    }

    @Override
    public String toString() {
        if (hasValue) {
            return super.toString();
        }
        return "??";
    }
}
