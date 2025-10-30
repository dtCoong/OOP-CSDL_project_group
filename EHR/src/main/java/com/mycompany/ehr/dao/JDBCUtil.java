package com.mycompany.ehr.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {
    
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/personal_health_management";
    private static final String USER = "root";
    private static final String PASSWORD = "Admin@123";

    public static Connection getConnection(){
        Connection connection  = null;
        try {
             Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
    public static void printInfo(Connection c){
        try{
            if(c!=null){
                DatabaseMetaData mtdt = c.getMetaData();
                System.out.println(mtdt.getDatabaseProductName()+ "\n" + mtdt.getDatabaseProductVersion());
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public static void closeConnection(Connection c){
        try{
            if(c!=null){
                c.close();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

}
