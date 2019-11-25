package clientImpl.metadata;

import org.jetbrains.annotations.NotNull;
import sqlapi.metadata.ColumnConstraint;
import sqlapi.metadata.ColumnConstraintType;

import java.util.Collections;
import java.util.List;

class ColumnConstraintImpl implements ColumnConstraint {

    @NotNull
    private final ColumnConstraintType type;

    @NotNull
    private final List<Object> parameters;

    ColumnConstraintImpl(@NotNull ColumnConstraintType type, @NotNull List<Object> parameters) {
        this.type = type;
        this.parameters = parameters;
    }

    ColumnConstraintImpl(@NotNull ColumnConstraintType type) {
        this.type = type;
        this.parameters = Collections.emptyList();
    }

    @NotNull
    @Override
    public ColumnConstraintType getConstraintType() {
        return type;
    }

    @NotNull
    @Override
    public List<Object> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        switch (type) {
            case MAX_SIZE:
                return "MAX SIZE (" + parameters.get(0) + ")";
            case NOT_NULL:
                return "NOT NULL";
            case PRIMARY_KEY:
                return "PRIMARY KEY";
            case DEFAULT_VALUE:
                return "DEFAULT " + parameters.get(0);
            default:
                return "";
        }
    }
}
