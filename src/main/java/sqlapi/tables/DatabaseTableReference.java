package sqlapi.tables;

import org.jetbrains.annotations.NotNull;
import sqlapi.misc.SelectedItem;

import java.util.List;

public interface DatabaseTableReference extends TableReference, SelectedItem {

    @NotNull String getDatabaseName();

    @NotNull String getTableName();

    @Override default void setParameters(List<Object> parameters) {
    }
}
