package sqlapi.exceptions;

import sqlapi.queries.SqlQuery;

public class UnsupportedQueryTypeException extends SqlException {

    private final SqlQuery query;

    public UnsupportedQueryTypeException(SqlQuery query) {
        this.query = query;
    }

    public SqlQuery getQuery() {
        return query;
    }

    @Override
    public String getMessage() {
        return "Unsupported query type: " + query;
    }
}
