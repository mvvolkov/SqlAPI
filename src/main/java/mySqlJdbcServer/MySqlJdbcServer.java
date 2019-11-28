package mySqlJdbcServer;

import org.jetbrains.annotations.NotNull;
import sqlapi.exceptions.NoSuchDatabaseException;
import sqlapi.exceptions.SqlException;
import sqlapi.exceptions.WrappedException;
import sqlapi.metadata.TableMetadata;
import sqlapi.queries.*;
import sqlapi.queryResult.ResultSet;
import sqlapi.server.SqlServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MySqlJdbcServer implements SqlServer {

    private Connection connection;

    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/logiweb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Moscow";

    //  Database credentials
    private static final String USER = "root";
    private static final String PASS = "mpsjetbrains2019";

    public static void main(String[] args) {
        MySqlJdbcServer server = new MySqlJdbcServer();
    }

    public MySqlJdbcServer() {

        //STEP 2: Register JDBC driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //STEP 3: Open a connection
        System.out.println("Connecting to database...");
        try {
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void executeQuery(@NotNull SqlQuery query) throws SqlException {

        System.out.println(query);

        if (query instanceof CreateTableQuery || query instanceof DropTableQuery) {
            Statement statement = null;
            try {
                statement = connection.createStatement();
                statement.execute(query.toString());
            } catch (SQLException e) {
                throw new WrappedException(e);
            }
        }

        if (query instanceof InsertQuery || query instanceof DeleteQuery ||
                query instanceof UpdateQuery || query instanceof InsertFromSelectQuery) {
            Statement statement = null;
            try {
                statement = connection.createStatement();
                statement.executeUpdate(query.toString());
            } catch (SQLException e) {
                throw new WrappedException(e);
            }
        }


    }

    @Override
    public @NotNull ResultSet getQueryResult(@NotNull SelectQuery selectQuery)
            throws SqlException {

        System.out.println(selectQuery);
        try {
            Statement statement = connection.createStatement();
            java.sql.ResultSet resultSet = statement.executeQuery(selectQuery.toString());
        } catch (SQLException e) {
            throw new WrappedException(e);
        }

        return null;
    }

    @Override
    public @NotNull Collection<String> getDatabases() throws SqlException {
        List<String> databases = new ArrayList<>();

        try {
            //Creating a Statement object
            Statement stmt = connection.createStatement();
            //Retrieving the data
            java.sql.ResultSet rs = stmt.executeQuery("Show Databases");
            while (rs.next()) {
                databases.add(rs.getString(1));
            }
        } catch (SQLException ex) {
            throw new WrappedException(ex);
        }
        return databases;
    }

    @Override
    public @NotNull Collection<TableMetadata> getTables(@NotNull String databaseName)
            throws NoSuchDatabaseException {
        return Collections.emptyList();
    }
}
