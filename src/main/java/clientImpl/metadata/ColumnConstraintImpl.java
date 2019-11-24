package clientImpl.metadata;

import sqlapi.metadata.ColumnConstraint;
import sqlapi.metadata.ColumnConstraintType;

import java.util.Collections;
import java.util.List;

public class ColumnConstraintImpl implements ColumnConstraint {

    private final ColumnConstraintType type;

    private final List<Object> parameters;

    public ColumnConstraintImpl(ColumnConstraintType type, List<Object> parameters) {
        this.type = type;
        this.parameters = parameters;
    }

    public ColumnConstraintImpl(ColumnConstraintType type) {
        this.type = type;
        this.parameters = Collections.emptyList();
    }

    @Override
    public ColumnConstraintType getConstraintType() {
        return type;
    }

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
