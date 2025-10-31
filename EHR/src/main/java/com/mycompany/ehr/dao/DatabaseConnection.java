package com.mycompany.ehr.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData; 
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/EHR_DB";
    
    private static final String DB_USER = "root";

    private static final String DB_PASSWORD = "admin";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối SQL! Vui lòng kiểm tra URL, Username, Password và trạng thái MySQL server.");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy MySQL JDBC Driver! Bạn đã thêm dependency vào pom.xml chưa?");
            e.printStackTrace();
        }
        return connection;
    }

    public static void printInfo(Connection c) {
        try {
            if (c != null && !c.isClosed()) {
                DatabaseMetaData mtdt = c.getMetaData();
                System.out.println(mtdt.getDatabaseProductName());
                System.out.println(mtdt.getDatabaseProductVersion());
            } 
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection(Connection c) {
        try {
            if (c != null && !c.isClosed()) {
                c.close();
            
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi đóng kết nối CSDL:");
            e.printStackTrace();
        }
    }
}