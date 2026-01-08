/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Subject;

/**
 *
 * @author elie
 */
public class SubjectDao {

    private String jdbcUrl = "jdbc:mysql://localhost:3306/student_career_path_recommendation_system_db";
    private String dbUsername = "root";
    private String dbPassword = "password123";

    // CREATE (Add Subject)
    public int createSubject(Subject s) {
        try {
            Connection con = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
            String sql = "INSERT INTO subjects (subject_name, description, level, credits) VALUES (?,?,?,?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, s.getSubjectName());
            pst.setString(2, s.getDescription());
            pst.setString(3, s.getLevel());
            pst.setInt(4, s.getCredits());

            int row = pst.executeUpdate();
            con.close();
            return row;

        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    // READ ALL SUBJECTS
    public List<Subject> findAllSubjects() {
        List<Subject> subjects = new ArrayList<>();

        try {
            Connection con = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
            String sql = "SELECT * FROM subjects";
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Subject s = new Subject();
                s.setSubjectId(rs.getInt("subject_id"));
                s.setSubjectName(rs.getString("subject_name"));
                s.setDescription(rs.getString("description"));
                s.setLevel(rs.getString("level"));
                s.setCredits(rs.getInt("credits"));
                subjects.add(s);
            }

            con.close();
            return subjects;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // READ SUBJECT BY ID
    public Subject findSubjectById(int id) {
        try {
            Connection con = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
            String sql = "SELECT * FROM subjects WHERE subject_id=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                Subject s = new Subject();
                s.setSubjectId(rs.getInt("subject_id"));
                s.setSubjectName(rs.getString("subject_name"));
                s.setDescription(rs.getString("description"));
                s.setLevel(rs.getString("level"));
                s.setCredits(rs.getInt("credits"));

                con.close();
                return s;
            }

            con.close();
            return null;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // UPDATE SUBJECT
    public int updateSubject(Subject s) {
        try {
            Connection con = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
            String sql = "UPDATE subjects SET subject_name=?, description=?, level=?, credits=? WHERE subject_id=?";
            PreparedStatement pst = con.prepareStatement(sql);

            pst.setString(1, s.getSubjectName());
            pst.setString(2, s.getDescription());
            pst.setString(3, s.getLevel());
            pst.setInt(4, s.getCredits());
            pst.setInt(5, s.getSubjectId());

            int row = pst.executeUpdate();
            con.close();
            return row;

        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    // DELETE SUBJECT
    public int deleteSubject(int id) {
        try {
            Connection con = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
            String sql = "DELETE FROM subjects WHERE subject_id=?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setInt(1, id);

            int row = pst.executeUpdate();
            con.close();
            return row;

        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }   
    
    public Subject findById(int subjectId) {
    Subject subject = null;
    String sql = "SELECT * FROM subjects WHERE subject_id = ?";
    
       try (Connection con = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
             PreparedStatement ps = con.prepareStatement(sql)) {
        
        ps.setInt(1, subjectId);  // set the subject ID in the query
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            subject = new Subject();
            subject.setSubjectId(rs.getInt("subject_id"));
            subject.setSubjectName(rs.getString("subject_name"));
            // add other fields if needed
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return subject; // will return null if not found
}

    public boolean subjectNameExists(String subjectName) {
    String sql = "SELECT COUNT(*) FROM subjects WHERE subject_name = ?";
    try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/student_career_path_recommendation_system_db",
                "root", "password123");
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, subjectName);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}

    public boolean subjectNameExistsForOtherId(String subjectName, int subjectId) {
    String sql = "SELECT COUNT(*) FROM subjects WHERE subject_name = ? AND subject_id <> ?";
    try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/student_career_path_recommendation_system_db",
                "root", "password123");
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, subjectName);
        ps.setInt(2, subjectId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}

}
