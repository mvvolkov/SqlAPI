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
}
