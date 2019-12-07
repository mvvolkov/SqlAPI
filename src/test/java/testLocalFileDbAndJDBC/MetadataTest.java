package testLocalFileDbAndJDBC;

import clientImpl.metadata.MetadataFactory;
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
 * SELECT * FROM DB1.table1;
 * <p>
 * column1, column2, column3, column4
 * 10, 30, test1, t21
 * 11, 31, test2, null
 * 12, 32, test3, t43
 * 13, 33, test2, t653
 * 15, 34, test1, null
 * 16, null, test1, null
 * <p>
 * <p>
 * SELECT * FROM DB1.table2;
 * <p>
 * column5, column3
 * 22, test3
 * 23, test2
 * 25, test4
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

        TableMetadata tm1 = MetadataFactory.table("table1",
                MetadataFactory.integer("column1", MetadataFactory.primaryKey()),
                MetadataFactory.integer("column2", MetadataFactory.defaultVal(15)),
                MetadataFactory.varchar("column3", 20, MetadataFactory.notNull()),
                MetadataFactory.varchar("column4", 5));

        try {
            sqlServer.validateMetadata(databaseName, tm1);
        } catch (FailedDatabaseValidationException fe) {
            System.out.println(fe.getMessage());
            return;
        }
        fail();
    }

    @Test
    public void testReadDatabaseWrongTableName() {

        if (!(sqlServer instanceof LocalFileDbServer)) {
            return;
        }
        TableMetadata tm1 = MetadataFactory.table("table1",
                MetadataFactory.integer("column1", MetadataFactory.primaryKey()),
                MetadataFactory.integer("column2", MetadataFactory.defaultVal(15)),
                MetadataFactory.varchar("column3", 20, MetadataFactory.notNull()),
                MetadataFactory.varchar("column4", 5));

        TableMetadata tm2 = MetadataFactory.table("table3",
                MetadataFactory.integer("column5", MetadataFactory.primaryKey()),
                MetadataFactory.varchar("column3", 15));

        try {
            sqlServer.validateMetadata(databaseName, tm1, tm2);
        } catch (FailedDatabaseValidationException fe) {
            System.out.println(fe.getMessage());
            return;
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

        TableMetadata tm2 = MetadataFactory.table("table2",
                MetadataFactory.integer("column5", MetadataFactory.primaryKey()),
                MetadataFactory.varchar("column3", 15));

        try {
            sqlServer.validateMetadata(databaseName, tm1, tm2);
        } catch (FailedDatabaseValidationException fe) {
            System.out.println(fe.getMessage());
            return;
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

        TableMetadata tm2 = MetadataFactory.table("table2",
                MetadataFactory.integer("column5", MetadataFactory.primaryKey()),
                MetadataFactory.varchar("column3", 15));

        try {
            sqlServer.validateMetadata(databaseName, tm1, tm2);
        } catch (FailedDatabaseValidationException fe) {
            System.out.println(fe.getMessage());
            return;
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

        TableMetadata tm2 = MetadataFactory.table("table2",
                MetadataFactory.integer("column5", MetadataFactory.primaryKey()),
                MetadataFactory.varchar("column3", 15));

        try {
            sqlServer.validateMetadata(databaseName, tm1, tm2);
        } catch (FailedDatabaseValidationException fe) {
            System.out.println(fe.getMessage());
            return;
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

        TableMetadata tm2 = MetadataFactory.table("table2",
                MetadataFactory.integer("column5", MetadataFactory.primaryKey()),
                MetadataFactory.varchar("column3", 15));

        try {
            sqlServer.validateMetadata(databaseName, tm1, tm2);
        } catch (FailedDatabaseValidationException fe) {
            System.out.println(fe.getMessage());
            return;
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

        TableMetadata tm2 = MetadataFactory.table("table2",
                MetadataFactory.integer("column5", MetadataFactory.primaryKey()),
                MetadataFactory.varchar("column3", 15));

        try {
            sqlServer.validateMetadata(databaseName, tm1, tm2);
        } catch (FailedDatabaseValidationException fe) {
            System.out.println(fe.getMessage());
            return;
        }
        fail();
    }


    @Test
    public void testReadDatabaseSuccess() {

        if (!(sqlServer instanceof LocalFileDbServer)) {
            return;
        }

        TableMetadata tm1 = MetadataFactory.table("table1",
                MetadataFactory.integer("column1", MetadataFactory.primaryKey()),
                MetadataFactory.integer("column2", MetadataFactory.defaultVal(15)),
                MetadataFactory.varchar("column3", 20, MetadataFactory.notNull()),
                MetadataFactory.varchar("column4", 5));

        TableMetadata tm2 = MetadataFactory.table("table2",
                MetadataFactory.integer("column5", MetadataFactory.primaryKey()),
                MetadataFactory.varchar("column3", 15));

        try {
            sqlServer.validateMetadata(databaseName, tm1, tm2);
        } catch (SqlException se) {
            System.out.println(se.getMessage());
            fail();
        }
    }
}
