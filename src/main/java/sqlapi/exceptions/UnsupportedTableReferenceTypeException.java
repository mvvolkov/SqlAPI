package sqlapi.exceptions;

import org.jetbrains.annotations.NotNull;
import sqlapi.tables.TableReference;

public final class UnsupportedTableReferenceTypeException extends SqlException {

    @NotNull
    private final TableReference tableReference;

    public UnsupportedTableReferenceTypeException(@NotNull TableReference tableReference) {
        this.tableReference = tableReference;
    }

    @NotNull
    public TableReference getTableReference() {
        return tableReference;
    }

    @Override
    public String getMessage() {
        return "Unsupported table reference type: " + tableReference;
    }
}
