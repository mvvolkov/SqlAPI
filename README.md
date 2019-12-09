# SqlAPI

This project represents an SQL API for Java. It's distinction from the JDBC API is that the queries are represented
 by objects with rich structure rather than simple strings. Therefore, the parser is not needed on the server side
 . The parametrized queries are supported by the API. This allows to reuse the same query with different parameters.
 
A default client implementation for the API is presented. A number of useful factory methods are created to simplify
 the usage of the API. Besides, a separate package for conversion of the API entities to String objects is added.

The server implementation for the API is done using collections in memory. It is possible to save a database to a
 file. Another server implementation is a simple adaptor to JDBC API. The test methods are applied to both server
  implementations and give the same result. A free online MySQL server is used to test the JDBC adaptor implementation.
   There is no guarantee that connection to this server is always possible.
  
 Presented SQL server with collections in memory supports the following SQL parts:
 
 - create database
 - CREATE and DROP table
 - two SQL types: INTEGER and VARCHAR
 - column constrains: PRIMARY KEY, NOT NULL, DEFAULT VALUE, UNIQUE, MAX SIZE
 - INSERT queries: insert values to all columns, insert values to selected columns, insert from select subquery
 - DELETE queries: delete all rows, delete rows matching a predicate
 - supported predicates: IN, IS NULL, IS NOT NULL, =, !=, >, >=, <, <=, AND, OR
 - column expressions can be used in the predicates, INSERT and UPDATE queries  (for example col1 > col2 + 15)
 - UPDATE queries with predicates. column expressions can be used in assignments
 - SELECT queries:
    - selected items:
        - select all columns (SELECT * FROM)
        - select all columns from the table (SELECT table1.* FROM)
        - select column expressions, alias can be used
        - several items can be used in one expression.
        - if the selected column exists in more than one "select from" item then it's name should be used with a table
         prefix
    - "select from" items:
        - tables
        - INNER JOIN, LEFT OUTER JOIN, RIGHT OUTER JOIN, each join operation can have any "select from" items as its
         operands
        - select from SELECT subquery
    - all supported predicates can be used in WHERE clause and in ON clause of join operations  
    - GROUP BY clause is supported
    - aggregate functions COUNT, SUM, MAX, MIN, AVG are supported. they can be use even if GROUP BY clause is not
     presented in a query, COUNT(*) is also supported.
    - aggregate functions can be used as a part of expression (for example SUM(column1) + column2 + 1) 
            
  
 To be done for the server implementation with collections in memory:
 - [ ] add more SQL types
 - [ ] add HAVING clause to SELECT queries
 - [ ] add ORDER BY, DISTINCT to SELECT queries
