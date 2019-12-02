package localFileDatabase.server.persistent;

import sqlapi.metadata.ColumnConstraint;
import sqlapi.metadata.ColumnConstraintType;

import java.io.Serializable;
import java.util.List;

public class PersistentColumnConstraint implements ColumnConstraint, Serializable {

    public static final long serialVersionUID = 2486445454968848262L;

    private final ColumnConstraintType type;

    private final List<Object> parameters;

    public PersistentColumnConstraint(ColumnConstraint constraint) {
        this.type = constraint.getConstraintType();
        this.parameters = constraint.getParameters();
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
