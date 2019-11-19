package sqlapi.selectResult;

import java.util.List;

public interface ResultSet {

    List<String> getHeaders();

    List<ResultRow> getRows();
}