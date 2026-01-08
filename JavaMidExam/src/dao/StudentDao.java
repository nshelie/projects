/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Student;

/**
 *
 * @author elie
 */
public class StudentDao {
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

    private String jdbcUrl = "jdbc:mysql://localhost:3306/student_career_path_recommendation_system_db";
    private String dbUsername = "root";
    private String dbPassword = "password123";

    // CREATE
    public int createStudent(Student student) {
    int row = 0;
    String sql = "INSERT INTO students (full_name, email, password, department, user_id) VALUES (?,?,?,?,?)";

    try (Connection con = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
         PreparedStatement pst = con.prepareStatement(sql)) {

        pst.setString(1, student.getFullName());
        pst.setString(2, student.getEmail());
        pst.setString(3, student.getPassword());
        pst.setString(4, student.getDepartment());
        pst.setInt(5, student.getUserId());   // 👈 important

        row = pst.executeUpdate();

    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return row;
}


    // READ ALL
    public List<Student> findAllStudents() {
        try {
            Connection con = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
            String sql = "SELECT * FROM students";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            List<Student> students = new ArrayList<>();
            while(rs.next()) {
                Student s = new Student();
                s.setStudentId(rs.getInt("student_id"));
                s.setFullName(rs.getString("full_name"));
                s.setEmail(rs.getString("email"));
                s.setPassword(rs.getString("password"));
                s.setDepartment(rs.getString("department"));
                students.add(s);
            }
            con.close();
            return students;
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // READ BY ID
    public Student findStudentById(int id) {
        try {
            Connection con = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
            String sql = "SELECT * FROM students WHERE student_id=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if(rs.next()) {
                Student s = new Student();
                s.setStudentId(rs.getInt("student_id"));
                s.setFullName(rs.getString("full_name"));
                s.setEmail(rs.getString("email"));
                s.setPassword(rs.getString("password"));
                s.setDepartment(rs.getString("department"));
                con.close();
                return s;
            }
            con.close();
            return null;
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // UPDATE

public int updateStudent(Student student) {
    try {
        Connection con = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
        // Do not update email to avoid duplicate entry error
        String sql = "UPDATE students SET full_name=?, password=?, department=? WHERE student_id=?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, student.getFullName());
        pst.setString(2, student.getPassword());
        pst.setString(3, student.getDepartment());
        pst.setInt(4, student.getStudentId());
        int row = pst.executeUpdate();
        con.close();
        return row;
    } catch(Exception ex) {
        ex.printStackTrace();
        return 0;
    }
}



    // DELETE
    public int deleteStudent(int id) {
        try {
            Connection con = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
            String sql = "DELETE FROM students WHERE student_id=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            int row = pst.executeUpdate();
            con.close();
            return row;
        } catch(Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }
    public List<String> getAllDepartments() {
    List<String> departments = new ArrayList<>();
    try (Connection con = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
         Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT DISTINCT department FROM students")) {
        while (rs.next()) {
            departments.add(rs.getString("department"));
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return departments;
}
     public Integer getStudentIdByUserId(int userId) {
        String sql = "SELECT student_id FROM students WHERE user_id = ?";
        try (Connection con = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("student_id");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // not a student / record missing
    }

     public Student findById(int studentId) {
    Student student = null;
    String sql = "SELECT * FROM students WHERE student_id = ?";
       try (Connection con = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
             PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, studentId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            student = new Student();
            student.setStudentId(rs.getInt("student_id"));
            student.setFullName(rs.getString("full_name"));
            student.setEmail(rs.getString("email"));
            student.setDepartment(rs.getString("department"));
            // Add other fields if needed
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return student;
}


}
