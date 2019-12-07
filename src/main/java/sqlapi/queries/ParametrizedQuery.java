package sqlapi.queries;

public interface ParametrizedQuery {

    void setParameters(Object... values);
}
