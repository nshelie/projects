/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author User-104
 */
public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/apartment_rent_management_system_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "nadege123";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (Exception ex) {
            System.out.println("DB Connection Error: " + ex.getMessage());
            return null;
        }
    }

}
