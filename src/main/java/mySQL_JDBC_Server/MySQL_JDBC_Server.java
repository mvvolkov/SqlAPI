package mySQL_JDBC_Server;

import clientImpl.stringUtil.QueryStringUtil;
import mySQL_JDBC_Server.output.QueryResultImpl;
import mySQL_JDBC_Server.output.QueryResultRowImpl;
import org.jetbrains.annotations.NotNull;
import sqlapi.exceptions.FailedDatabaseValidationException;
import sqlapi.exceptions.SqlException;
import sqlapi.exceptions.WrappedException;
import sqlapi.metadata.TableMetadata;
import sqlapi.queries.*;
import sqlapi.queryResult.QueryResult;
import sqlapi.queryResult.QueryResultRow;
import sqlapi.server.SqlServer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQL_JDBC_Server implements SqlServer {

    private Connection connection;

    private final String url;

    private final String user;

    private final String password;


    public MySQL_JDBC_Server(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public void connect() throws SqlException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new WrappedException(e);
        }
        System.out.println("Connecting to " + url);
        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws SqlException {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new WrappedException(e);
        }
    }


    @Override
    public void executeQuery(@NotNull SqlQuery query, Object... parameters)
            throws SqlException {

        if (query instanceof ParametrizedQuery) {
            ((ParametrizedQuery) query).setParameters(parameters);
        }

        String queryString = QueryStringUtil.getQueryString(query);
        System.out.println(queryString);

        try (Statement statement = connection.createStatement()) {
            if (query instanceof TableQuery) {
                statement.executeUpdate(queryString);
            } else {
                statement.execute(queryString);
            }
        } catch (SQLException e) {
            throw new WrappedException(e);
        }
    }

    @Override
    public @NotNull QueryResult getQueryResult(@NotNull SelectQuery selectQuery,
                                               Object... parameters)
            throws SqlException {

        selectQuery.setParameters(parameters);
        String queryString = QueryStringUtil.getSelectQueryString(selectQuery);
        System.out.println(queryString);

        try (Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(queryString);
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            List<String> columns = new ArrayList<>();
            for (int i = 1; i <= columnsNumber; i++) {
                columns.add(rsmd.getColumnName(i));
            }
            List<QueryResultRow> rows = new ArrayList<>();
            while (resultSet.next()) {
                List<Object> values = new ArrayList<>();
                for (int i = 1; i <= columnsNumber; i++) {
                    values.add(resultSet.getObject(i));
                }
                rows.add(new QueryResultRowImpl(values));
            }
            return new QueryResultImpl(rows, columns);
        } catch (SQLException e) {
            throw new WrappedException(e);
        }
    }

    @Override public void validateMetadata(String databaseName, TableMetadata... tables)
            throws FailedDatabaseValidationException {
        // TODO to be done
    }


}
