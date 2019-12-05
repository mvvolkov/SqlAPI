package clientImpl.columnExpr;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ParametrizedInputValue;

import java.util.List;

public class ParametrizedInputValueImpl extends InputValueImpl
        implements ParametrizedInputValue {

    private boolean hasValue;

    ParametrizedInputValueImpl(@NotNull String alias) {
        super(null, alias);
    }

    @Override
    public boolean hasValue() {
        return hasValue;
    }

    @Override public void setParameters(List<Object> parameters) {
        if (parameters.isEmpty()) {
            return;
        }
        value = parameters.get(0);
        parameters.remove(0);
        hasValue = true;
    }

    @Override
    public String toString() {
        if (hasValue) {
            return super.toString();
        }
        return "??";
    }
}
