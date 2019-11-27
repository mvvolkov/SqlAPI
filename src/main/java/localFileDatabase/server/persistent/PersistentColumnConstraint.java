package localFileDatabase.server.persistent;

import sqlapi.metadata.ColumnConstraint;
import sqlapi.metadata.ColumnConstraintType;

import java.io.Serializable;
import java.util.List;

public class PersistentColumnConstraint implements ColumnConstraint, Serializable {

    public static final long serialVersionUID = 2486445454968848262L;

    private final ColumnConstraintType type;

    private final List<Object> parameters;

    public PersistentColumnConstraint(ColumnConstraintType type,
                                      List<Object> parameters) {
        this.type = type;
        this.parameters = parameters;
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
