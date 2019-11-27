package sqlapi.tables;

import org.jetbrains.annotations.NotNull;
import sqlapi.assignment.SelectedItem;

public interface DatabaseTableReference extends TableReference, SelectedItem {

    @NotNull String getDatabaseName();

    @NotNull String getTableName();

}
