package api;

import api.exceptions.NoSuchColumnException;

import java.util.List;

public interface ResultRow {

    List<Object> getValues();

    Integer getInteger(String columnName) throws NoSuchColumnException;

    Integer getInteger(int index);

    String getString(String columnName) throws NoSuchColumnException;

    String getString(int index);

    Object getObject(String columnName) throws NoSuchColumnException;

    Object getObject(int index);

    boolean isNull(String columnName) throws NoSuchColumnException;

    boolean isNull(int index);
}
