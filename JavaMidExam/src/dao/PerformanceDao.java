/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.Performance;

/**
 *
 * @author elie
 */
public class PerformanceDao {

    private String jdbcUrl = "jdbc:mysql://localhost:3306/student_career_path_recommendation_system_db";
    private String dbUsername = "root";
    private String dbPassword = "password123";

    // Centralized connection method
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
    }

   

    public boolean updatePerformance(Performance p) {
        String grade = calculateGrade(p.getScore());
        p.setGrade(grade);

        String sql = "UPDATE performance SET score=?, grade=? WHERE performance_id=?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, p.getScore());
            ps.setString(2, grade);
            ps.setInt(3, p.getPerformanceId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Performance findById(int id) {
        String sql = "SELECT p.performance_id, p.student_id, p.subject_id, s.subject_name, p.score, p.grade " +
                     "FROM performance p " +
                     "JOIN subjects s ON p.subject_id = s.subject_id " +
                     "WHERE p.performance_id=?";

        Performance p = null;

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = new Performance();
                    p.setPerformanceId(rs.getInt("performance_id"));
                    p.setStudentId(rs.getInt("student_id"));
                    p.setSubjectId(rs.getInt("subject_id"));
                    p.setSubjectName(rs.getString("subject_name"));
                    p.setScore(rs.getDouble("score"));
                    p.setGrade(rs.getString("grade"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return p;
    }

    public List<Performance> findAll() {
        List<Performance> list = new ArrayList<>();
        String sql = "SELECT p.performance_id, p.student_id, p.subject_id, s.subject_name, p.score, p.grade " +
                     "FROM performance p " +
                     "JOIN subjects s ON p.subject_id = s.subject_id";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Performance p = new Performance();
                p.setPerformanceId(rs.getInt("performance_id"));
                p.setStudentId(rs.getInt("student_id"));
                p.setSubjectId(rs.getInt("subject_id"));
                p.setSubjectName(rs.getString("subject_name"));
                p.setScore(rs.getDouble("score"));
                p.setGrade(rs.getString("grade"));
                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean deletePerformance(int id) {
        String sql = "DELETE FROM performance WHERE performance_id=?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Performance> getPerformancesByStudentId(int studentId) {
        List<Performance> list = new ArrayList<>();
        String sql = "SELECT p.performance_id, p.student_id, p.subject_id, s.subject_name, p.score, p.grade " +
                     "FROM performance p " +
                     "JOIN subjects s ON p.subject_id = s.subject_id " +
                     "WHERE p.student_id=?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Performance p = new Performance();
                    p.setPerformanceId(rs.getInt("performance_id"));
                    p.setStudentId(rs.getInt("student_id"));
                    p.setSubjectId(rs.getInt("subject_id"));
                    p.setSubjectName(rs.getString("subject_name"));
                    p.setScore(rs.getDouble("score"));
                    p.setGrade(rs.getString("grade"));
                    list.add(p);
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return list;
    }

    public Performance addAndReturnPerformance(Performance p) {
        String grade = calculateGrade(p.getScore());
        p.setGrade(grade);

        String sql = "INSERT INTO performance(student_id, subject_id, score, grade) VALUES (?, ?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, p.getStudentId());
            ps.setInt(2, p.getSubjectId());
            ps.setDouble(3, p.getScore());
            ps.setString(4, grade);

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        int newId = keys.getInt(1);
                        return findById(newId);
                    }
                }
            }

        } catch (SQLIntegrityConstraintViolationException ex) {
            JOptionPane.showMessageDialog(null, "This student already has performance recorded for this subject!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error inserting performance");
        }

        return null;
    }

  public boolean addPerformanceAndStudentSubject(int studentId, int subjectId, int score) {
    String grade = calculateGrade(score);  // calculate grade

    String insertPerformanceSql = "INSERT INTO performance(student_id, subject_id, score, grade) VALUES(?, ?, ?, ?)";
    String insertStudentSubjectSql = "INSERT INTO student_subject(student_id, subject_id) " +
                                     "SELECT ?, ? WHERE NOT EXISTS " +
                                     "(SELECT 1 FROM student_subject WHERE student_id=? AND subject_id=?)";

    try (Connection con = getConnection()) {
        con.setAutoCommit(false);

        try (PreparedStatement ps1 = con.prepareStatement(insertPerformanceSql)) {
            ps1.setInt(1, studentId);
            ps1.setInt(2, subjectId);
            ps1.setInt(3, score);
            ps1.setString(4, grade);  // save grade
            ps1.executeUpdate();
        }

        try (PreparedStatement ps2 = con.prepareStatement(insertStudentSubjectSql)) {
            ps2.setInt(1, studentId);
            ps2.setInt(2, subjectId);
            ps2.setInt(3, studentId);
            ps2.setInt(4, subjectId);
            ps2.executeUpdate();
        }

        con.commit();
        return true;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

  // In PerformanceDao.java
public boolean exists(int studentId, int subjectId) {
    String sql = "SELECT COUNT(*) FROM performance WHERE student_id = ? AND subject_id = ?";
    try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, studentId);
        ps.setInt(2, subjectId);

        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            return rs.getInt(1) > 0; // true if exists
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}


    private String calculateGrade(double score) {
        if (score >= 90) return "A";
        else if (score >= 80) return "B";
        else if (score >= 70) return "C";
        else if (score >= 60) return "D";
        else return "F";
    }

    private String calculateRemarks(double score) {
        if (score >= 90) return "Excellent";
        else if (score >= 75) return "Good";
        else if (score >= 60) return "Average";
        else return "Needs Improvement";
    }
}
