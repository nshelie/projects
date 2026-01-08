/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.CareerRole;

/**
 *
 * @author elie
 */
public class CareerRoleDao {
 
    private String jdbcUrl = "jdbc:mysql://localhost:3306/student_career_path_recommendation_system_db";
    private String dbUsername = "root";
    private String dbPassword = "password123";

    private Connection getConnection() throws Exception {
        return DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
    }

    public int insert(CareerRole r) {
        String sql = "INSERT INTO career_role(role_name, description, required_subject_id, required_min_score) VALUES(?,?,?,?)";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, r.getRoleName());
            ps.setString(2, r.getDescription());
            ps.setInt(3, r.getRequiredSubjectId());
            ps.setInt(4, r.getRequiredMinScore());

            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public List<CareerRole> getAll() {
        List<CareerRole> list = new ArrayList<>();

        String sql = "SELECT * FROM career_role";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CareerRole r = new CareerRole();
                r.setRoleId(rs.getInt("role_id"));
                r.setRoleName(rs.getString("role_name"));
                r.setDescription(rs.getString("description"));
                r.setRequiredSubjectId(rs.getInt("required_subject_id"));
                r.setRequiredMinScore(rs.getInt("required_min_score"));
                list.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public CareerRole findById(int id) {
        String sql = "SELECT * FROM career_role WHERE role_id=?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                CareerRole r = new CareerRole();
                r.setRoleId(rs.getInt("role_id"));
                r.setRoleName(rs.getString("role_name"));
                r.setDescription(rs.getString("description"));
                r.setRequiredSubjectId(rs.getInt("required_subject_id"));
                r.setRequiredMinScore(rs.getInt("required_min_score"));
                return r;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int update(CareerRole r) {
        String sql = "UPDATE career_role SET role_name=?, description=?, required_subject_id=?, required_min_score=? WHERE role_id=?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, r.getRoleName());
            ps.setString(2, r.getDescription());
            ps.setInt(3, r.getRequiredSubjectId());
            ps.setInt(4, r.getRequiredMinScore());
            ps.setInt(5, r.getRoleId());

            return ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    // in dao/CareerRoleDao.java
public List<CareerRole> getRolesBySubjectId(int subjectId) {
    List<CareerRole> roles = new ArrayList<>();
    String sql = "SELECT cr.role_id, cr.role_name, cr.description, cr.required_min_score "
               + "FROM career_role cr "
               + "JOIN career_role_subject crs ON cr.role_id = crs.role_id "
               + "WHERE crs.subject_id = ?";
    try (Connection con = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, subjectId);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                CareerRole r = new CareerRole();
                r.setRoleId(rs.getInt("role_id"));
                r.setRoleName(rs.getString("role_name"));
                r.setDescription(rs.getString("description"));
                r.setRequiredMinScore(rs.getInt("required_min_score"));
                roles.add(r);
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return roles;
}

public CareerRole getRoleById(int roleId) {
    CareerRole r = null;
    String sql = "SELECT role_id, role_name, description, required_min_score FROM career_role WHERE role_id = ?";
    try (Connection con = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, roleId);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                r = new CareerRole();
                r.setRoleId(rs.getInt("role_id"));
                r.setRoleName(rs.getString("role_name"));
                r.setDescription(rs.getString("description"));
                r.setRequiredMinScore(rs.getInt("required_min_score"));
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return r;
}

    public int delete(int id) {
        String sql = "DELETE FROM career_role WHERE role_id=?";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
public boolean saveStudentRecommendation(int studentId, String careerName) {
    String sql = "INSERT INTO student_recommendations(student_id, career_name) VALUES(?, ?)";

    // Check duplicate
    if (isRecommendationAlreadySaved(studentId, careerName)) {
        return false; // already saved
    }

    try (Connection con = getConnection(); 
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, studentId);
        ps.setString(2, careerName);

        return ps.executeUpdate() > 0;

    } catch (SQLException e) {  // catch SQL exceptions
        e.printStackTrace();
        return false;
    } catch (Exception e) {  // catch getConnection exceptions
        e.printStackTrace();
        return false;
    }
}

public boolean isRecommendationAlreadySaved(int studentId, String careerName) {
    String sql = "SELECT COUNT(*) FROM student_recommendations WHERE student_id = ? AND career_name = ?";
    try (Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, studentId);
        ps.setString(2, careerName);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    }

    return false;
}

public List<CareerRole> getSavedRecommendationsForStudent(int studentId) {
    List<CareerRole> list = new ArrayList<>();
    String sql = "SELECT sr.career_name, cr.description " +
                 "FROM student_recommendations sr " +
                 "LEFT JOIN career_role cr ON sr.career_name = cr.role_name " +
                 "WHERE sr.student_id = ?";

    try (Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, studentId);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                CareerRole r = new CareerRole();
                r.setRoleName(rs.getString("career_name"));
                r.setDescription(rs.getString("description"));
                list.add(r);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}

public List<CareerRole> getRecommendationsForStudent(int studentId) throws Exception {
    List<CareerRole> list = new ArrayList<>();
    String sql = "SELECT cr.role_id, cr.role_name, cr.description, cr.required_subject_id, cr.required_min_score "
               + "FROM career_role cr "
               + "JOIN student_subject ss ON cr.required_subject_id = ss.subject_id "
               + "JOIN performance p ON ss.student_id = p.student_id AND ss.subject_id = p.subject_id "
               + "WHERE ss.student_id = ? "
               + "AND p.score >= cr.required_min_score";

    try (Connection con = getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, studentId);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                CareerRole r = new CareerRole();
                r.setRoleId(rs.getInt("role_id"));
                r.setRoleName(rs.getString("role_name"));
                r.setDescription(rs.getString("description"));
                r.setRequiredSubjectId(rs.getInt("required_subject_id"));
                r.setRequiredMinScore(rs.getInt("required_min_score"));
                list.add(r);
            }
        }
    }
    return list;
}
}
