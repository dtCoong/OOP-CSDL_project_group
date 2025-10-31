package com.mycompany.ehr.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class để quản lý kết nối JDBC
 * Cung cấp các phương thức static để lấy, đóng kết nối
 */
public class JDBCUtil {
    
    private static final String URL = "jdbc:mysql://localhost:3306/ehr?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=utf8";
    private static final String USER = "ehruser";
    private static final String PASSWORD = "Quangthu2005@";

    /**
     * Lấy connection đến database
     */
    public static Connection getConnection(){
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
    
    /**
     * In thông tin database
     */
    public static void printInfo(Connection c){
        try{
            if(c != null){
                DatabaseMetaData mtdt = c.getMetaData();
                System.out.println(mtdt.getDatabaseProductName() + "\n" + mtdt.getDatabaseProductVersion());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    
    /**
     * Đóng connection
     */
    public static void closeConnection(Connection c){
        try{
            if(c != null){
                c.close();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
