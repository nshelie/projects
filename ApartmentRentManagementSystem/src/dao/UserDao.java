/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;
import model.User;
import java.sql.*;

/**
 *
 * @author User-104
 */
public class UserDao {

    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/apartment_rent_management_system_db";
    private static final String USERNAME = "root"; // your DB username
    private static final String PASSWORD = "nadege123";     // your DB password

    // Method to get DB connection
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    /**
     * Validate login credentials
     * @param username Entered username
     * @param password Entered password
     * @return User object if valid, null if invalid
     */
    public User validateLogin(String username, String password) {
        String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password); // In production, use hashed password comparison

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setRole(rs.getString("role"));
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // invalid login
    }

}
