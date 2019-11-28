package sqlapi.exceptions;

import sqlapi.tables.TableReference;

public class UnsupportedTableReferenceTypeException extends SqlException {

    private final TableReference tableReference;


    public UnsupportedTableReferenceTypeException(TableReference tableReference) {
        this.tableReference = tableReference;
    }

    public TableReference getTableReference() {
        return tableReference;
    }

    @Override
    public String getMessage() {
        return "Unsupported table reference type: " + tableReference;
    }
}
