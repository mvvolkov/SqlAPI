package api.exceptions;

import api.TableDescription;

public class TableAlreadyExistsException extends SqlException {

    private final TableDescription newTableDescription;

    private TableDescription existingTableDescription;

    public TableAlreadyExistsException(TableDescription newTableDescription, TableDescription existingTableDescription) {
        this.newTableDescription = newTableDescription;
        this.existingTableDescription = existingTableDescription;
        assert (newTableDescription.getName().equals(existingTableDescription.getName()));
    }

    public TableDescription getNewTableDescription() {
        return newTableDescription;
    }

    public TableDescription getExistingTableDescription() {
        return existingTableDescription;
    }

    @Override
    public String getMessage() {
        return "Can not create a new table. The table with the name " + existingTableDescription.getName() + " already exists.";
    }


}
