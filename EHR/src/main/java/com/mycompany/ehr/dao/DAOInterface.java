package com.mycompany.ehr.dao;

import java.util.ArrayList;

public interface DAOInterface<T> {
    public int insert(T t);
    public T selectById(T t);
    public int update(T t);
    public int delete(T t); 
    public ArrayList<T> selectAll();
    public ArrayList<T> selectByCondition(String condition);
    public boolean exists(String condition);
}