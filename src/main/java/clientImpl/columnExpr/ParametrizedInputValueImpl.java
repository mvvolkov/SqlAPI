package clientImpl.columnExpr;

import org.jetbrains.annotations.NotNull;
import sqlapi.columnExpr.ParametrizedInputValue;

import java.util.List;

public class ParametrizedInputValueImpl extends InputValueImpl
        implements ParametrizedInputValue {

    private boolean isEmpty = true;

    ParametrizedInputValueImpl(@NotNull String alias) {
        super(null, alias);
    }

    @Override
    public boolean isEmpty() {
        return isEmpty;
    }

    @Override public void setParameters(List<Object> parameters) {
        if (parameters.isEmpty()) {
            return;
        }
        value = parameters.get(0);
        parameters.remove(0);
        isEmpty = false;
    }

    @Override
    public String toString() {
        if (isEmpty) {
            return "??";
        }
        return super.toString();
    }
}
