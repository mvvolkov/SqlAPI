package api;

import java.util.List;

public interface ResultSet {

    List<String> getColumns();

    List<ResultRow> getRows();

}
