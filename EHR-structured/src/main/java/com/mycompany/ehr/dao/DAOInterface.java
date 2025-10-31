package com.mycompany.ehr.dao;

import java.util.ArrayList;

/**
 * Interface chuẩn cho tất cả các DAO
 * Định nghĩa các phương thức CRUD cơ bản
 */
public interface DAOInterface<T> {
    public int insert(T t);
    public T selectById(T t);
    public int update(T t);
    public int delete(T t); 
    public ArrayList<T> selectAll();
    public ArrayList<T> selectByCondition(String condition);
    public boolean exists(String condition);
}
