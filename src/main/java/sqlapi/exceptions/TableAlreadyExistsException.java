package sqlapi.exceptions;

import sqlapi.TableMetadata;

public class TableAlreadyExistsException extends SqlException {

    private final TableMetadata newTableMetadata;

    private TableMetadata existingTableMetadata;

    public TableAlreadyExistsException(TableMetadata newTableMetadata, TableMetadata existingTableMetadata) {
        this.newTableMetadata = newTableMetadata;
        this.existingTableMetadata = existingTableMetadata;
        assert (newTableMetadata.getName().equals(existingTableMetadata.getName()));
    }

    public TableMetadata getNewTableMetadata() {
        return newTableMetadata;
    }

    public TableMetadata getExistingTableMetadata() {
        return existingTableMetadata;
    }

    @Override
    public String getMessage() {
        return "Can not create a new table. The table with the name " + existingTableMetadata.getName() + " already exists.";
    }


}
