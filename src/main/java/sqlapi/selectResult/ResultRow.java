package sqlapi.selectResult;

import java.util.List;

public interface ResultRow {


    Object getObject(int index);

    List<Object> getValues();
}
