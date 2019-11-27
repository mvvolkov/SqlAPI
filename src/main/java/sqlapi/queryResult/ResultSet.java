package sqlapi.queryResult;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ResultSet {

    @NotNull List<String> getHeaders();

    @NotNull List<ResultRow> getRows();
}