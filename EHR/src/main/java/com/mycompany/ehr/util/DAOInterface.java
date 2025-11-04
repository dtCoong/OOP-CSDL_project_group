package com.mycompany.ehr.util;

import java.sql.SQLException;
import java.util.ArrayList;

public interface DAOInterface<T> {
    public int insert(T t) throws SQLException;
    public T selectById(T t) throws SQLException;
    public int update(T t) throws SQLException;
    public int delete(T t) throws SQLException; 
    public ArrayList<T> selectAll() throws SQLException;
    public ArrayList<T> selectByCondition(String condition) throws SQLException;
    public boolean exists(String condition) throws SQLException;
}