package sqlapi.tables;

import org.jetbrains.annotations.NotNull;
import sqlapi.misc.SelectedItem;

public interface DatabaseTableReference extends TableReference, SelectedItem {

    @NotNull String getDatabaseName();

    @NotNull String getTableName();

}
