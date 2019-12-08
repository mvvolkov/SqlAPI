package testLocalFileDbAndJDBC;

import clientImpl.metadata.MetadataFactory;
import clientImpl.queries.QueryFactory;
import localFileDatabase.server.LocalFileDbServer;
import org.junit.Test;
import sqlapi.exceptions.FailedDatabaseValidationException;
import sqlapi.exceptions.SqlException;
import sqlapi.metadata.*;
import sqlapi.server.SqlServer;

import static org.junit.Assert.*;

/**
 * Before each test we have two tables:
 * <p>
 * CREATE TABLE DB1.table1(column1 INTEGER PRIMARY KEY,
 * column2 INTEGER DEFAULT 15, column3 VARCHAR(20) NOT NULL, column4 VARCHAR(5));
 * <p>
 * CREATE TABLE DB1.table2(column5 INTEGER PRIMARY KEY, column3 VARCHAR(15));
 */
public class MetadataTest extends AbstractTestRunner {


    public MetadataTest(SqlServer sqlServer, String database) {
        super(sqlServer, database);
    }

    @Test
    public void testReadDatabaseWrongNumberOfTables() {

        if (!(sqlServer instanceof LocalFileDbServer)) {
            return;
        }

        try {
            sqlServer.executeQuery(QueryFactory.validateDatabase(databaseName, tm1));
        } catch (FailedDatabaseValidationException fe) {
            System.out.println(fe.getMessage());
            return;
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }
        fail();
    }

    @Test
    public void testReadDatabaseWrongTableName() {

        if (!(sqlServer instanceof LocalFileDbServer)) {
            return;
        }

        TableMetadata tm2 = MetadataFactory.table("table3",
                MetadataFactory.integer("column5", MetadataFactory.primaryKey()),
                MetadataFactory.varchar("column3", 15));

        try {
            sqlServer.executeQuery(QueryFactory.validateDatabase(databaseName, tm1, tm2));
        } catch (FailedDatabaseValidationException fe) {
            System.out.println(fe.getMessage());
            return;
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }
        fail();
    }

    @Test
    public void testReadDatabaseWrongColumnsNumber() {

        if (!(sqlServer instanceof LocalFileDbServer)) {
            return;
        }

        TableMetadata tm1 = MetadataFactory.table("table1",
                MetadataFactory.integer("column1", MetadataFactory.primaryKey()),
                MetadataFactory.integer("column2", MetadataFactory.defaultVal(15)),
                MetadataFactory.varchar("column4", 5));

        try {
            sqlServer.executeQuery(QueryFactory.validateDatabase(databaseName, tm1, tm2));
        } catch (FailedDatabaseValidationException fe) {
            System.out.println(fe.getMessage());
            return;
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }
        fail();
    }

    @Test
    public void testReadDatabaseWrongColumnName() {

        if (!(sqlServer instanceof LocalFileDbServer)) {
            return;
        }

        TableMetadata tm1 = MetadataFactory.table("table1",
                MetadataFactory.integer("column1", MetadataFactory.primaryKey()),
                MetadataFactory.integer("column2", MetadataFactory.defaultVal(15)),
                MetadataFactory.varchar("column13", 20, MetadataFactory.notNull()),
                MetadataFactory.varchar("column4", 5));

        try {
            sqlServer.executeQuery(QueryFactory.validateDatabase(databaseName, tm1, tm2));
        } catch (FailedDatabaseValidationException fe) {
            System.out.println(fe.getMessage());
            return;
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }
        fail();
    }

    @Test
    public void testReadDatabaseWrongColumnConstraintsNumber() {

        if (!(sqlServer instanceof LocalFileDbServer)) {
            return;
        }

        TableMetadata tm1 = MetadataFactory.table("table1",
                MetadataFactory.integer("column1", MetadataFactory.primaryKey()),
                MetadataFactory.integer("column2", MetadataFactory.defaultVal(15)),
                MetadataFactory.varchar("column3", 20),
                MetadataFactory.varchar("column4", 5));

        try {
            sqlServer.executeQuery(QueryFactory.validateDatabase(databaseName, tm1, tm2));
        } catch (FailedDatabaseValidationException fe) {
            System.out.println(fe.getMessage());
            return;
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }
        fail();
    }

    @Test
    public void testReadDatabaseWrongColumnDefaultValue() {

        if (!(sqlServer instanceof LocalFileDbServer)) {
            return;
        }

        TableMetadata tm1 = MetadataFactory.table("table1",
                MetadataFactory.integer("column1", MetadataFactory.primaryKey()),
                MetadataFactory.integer("column2", MetadataFactory.defaultVal(16)),
                MetadataFactory.varchar("column3", 20, MetadataFactory.notNull()),
                MetadataFactory.varchar("column4", 5));

        try {
            sqlServer.executeQuery(QueryFactory.validateDatabase(databaseName, tm1, tm2));
        } catch (FailedDatabaseValidationException fe) {
            System.out.println(fe.getMessage());
            return;
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }
        fail();
    }

    @Test
    public void testReadDatabaseWrongColumnMaxSize() {

        if (!(sqlServer instanceof LocalFileDbServer)) {
            return;
        }

        TableMetadata tm1 = MetadataFactory.table("table1",
                MetadataFactory.integer("column1", MetadataFactory.primaryKey()),
                MetadataFactory.integer("column2", MetadataFactory.defaultVal(15)),
                MetadataFactory.varchar("column3", 20, MetadataFactory.notNull()),
                MetadataFactory.varchar("column4", 6));

        try {
            sqlServer.executeQuery(QueryFactory.validateDatabase(databaseName, tm1, tm2));
        } catch (FailedDatabaseValidationException fe) {
            System.out.println(fe.getMessage());
            return;
        } catch (SqlException e) {
            System.out.println(e.getMessage());
        }
        fail();
    }


    @Test
    public void testReadDatabaseSuccess() {

        if (!(sqlServer instanceof LocalFileDbServer)) {
            return;
        }

        try {
            sqlServer.executeQuery(QueryFactory.validateDatabase(databaseName, tm1, tm2));
        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }
}
