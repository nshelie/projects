/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.*;
import model.User;

/**
 *
 * @author elie
 */
public class UserDao {
    
    private static final String URL = "jdbc:mysql://localhost:3306/student_career_path_recommendation_system_db";
    private static final String USER = "root";
    private static final String PASS = "password123"; // change if needed
   
    
    // Check if email + password is valid, return User or null
    public static User login(String email, String password) {
        User u = null;
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
           if (rs.next()) {
    u = new User(
        rs.getInt("user_id"),
        rs.getString("full_name"),
        rs.getString("email"),
        rs.getString("password"),
        rs.getString("role"),
        rs.getString("department") // add this
    );
}

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return u;   // null if invalid, User if correct
    } 

 //add user
    public static int addUser(User user) {
    int generatedId = -1;
    String sql = "INSERT INTO users (full_name, email, password, role, department) VALUES (?, ?, ?, ?, ?)";

    try (Connection con = DriverManager.getConnection(URL, USER, PASS);
         PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        ps.setString(1, user.getFullName());
        ps.setString(2, user.getEmail());
        ps.setString(3, user.getPassword());
        ps.setString(4, user.getRole());
        ps.setString(5, user.getDepartment());

        int rowsAffected = ps.executeUpdate();

        if (rowsAffected > 0) {
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getInt(1);   // 👈 this is user_id
                }
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return generatedId;  // return user_id (or -1 if failed)
}


public static boolean updateUser(User user) {
    String sql = "UPDATE users SET full_name=?, password=?, department=? WHERE user_id=?";
    try (Connection con = DriverManager.getConnection(URL, USER, PASS);
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, user.getFullName());
        ps.setString(2, user.getPassword());
        ps.setString(3, user.getDepartment()); // new line
        ps.setInt(4, user.getUserId());
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
public static boolean isEmailTaken(String email) {
    String sql = "SELECT user_id FROM users WHERE email=?";
    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/student_career_path_recommendation_system_db", "root", "password123");
         PreparedStatement pst = con.prepareStatement(sql)) {
        pst.setString(1, email);
        ResultSet rs = pst.executeQuery();
        return rs.next(); // true if exists
    } catch (SQLException e) {
        e.printStackTrace();
        return true; // treat exception as "email taken" to avoid duplicate
    }
}
}
