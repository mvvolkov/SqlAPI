package api.selectResult;

import api.exceptions.NoSuchColumnException;

public interface ResultRow {


    Integer getInteger(String columnName) throws NoSuchColumnException;


    String getString(String columnName) throws NoSuchColumnException;


    Object getObject(String columnName) throws NoSuchColumnException;


    boolean isNull(String columnName) throws NoSuchColumnException;


}
