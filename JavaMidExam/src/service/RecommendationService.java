/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.sql.*;
import java.util.*;
import model.CareerRole;
import model.Company;

/**
 *
 * @author elie
 */
public class RecommendationService {
    private static final String URL = "jdbc:mysql://localhost:3306/student_career_path_recommendation_system_db";
    private static final String USER = "root";
    private static final String PASS = "password123";   // change if your DB has no password

    // MAIN METHOD: recommend roles + companies for a given student
    public Map<CareerRole, List<Company>> recommendByStudentId(int studentId) {
        Map<CareerRole, List<Company>> results = new LinkedHashMap<>();

        // 1) Get student's scores per subject
        Map<Integer, Double> studentScores = loadStudentScores(studentId);

        if (studentScores.isEmpty()) {
            // no performance records → nothing to recommend
            return results;
        }

        // 2) For each career role, check if the student qualifies
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {

            String rolesSql = "SELECT role_id, role_name, description, required_subject_id, required_min_score "
                            + "FROM career_role";
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(rolesSql)) {

                while (rs.next()) {
                    int roleId = rs.getInt("role_id");
                    String roleName = rs.getString("role_name");
                    String description = rs.getString("description");
                    int requiredSubjectId = rs.getInt("required_subject_id");
                    int requiredMinScore = rs.getInt("required_min_score");

                    // Check student's score on required subject
                    Double studentScore = studentScores.get(requiredSubjectId);

                    if (studentScore != null && studentScore >= requiredMinScore) {
                        // student qualifies for this role
                        CareerRole role = new CareerRole();
                        role.setRoleId(roleId);
                        role.setRoleName(roleName);
                        role.setDescription(description);
                        role.setRequiredSubjectId(requiredSubjectId);
                        role.setRequiredMinScore(requiredMinScore);

                        // Get companies that offer this role
                        List<Company> companies = loadCompaniesForRole(con, roleId);

                        results.put(role, companies);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    // Load performance for a student: subject_id -> score
    private Map<Integer, Double> loadStudentScores(int studentId) {
        Map<Integer, Double> scores = new HashMap<>();

        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            String sql = "SELECT subject_id, score FROM performance WHERE student_id = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, studentId);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int subjectId = rs.getInt("subject_id");
                        double score = rs.getDouble("score");
                        scores.put(subjectId, score);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return scores;
    }

    // Load companies that have this career role
    private List<Company> loadCompaniesForRole(Connection con, int roleId) {
        List<Company> list = new ArrayList<>();

        String sql = "SELECT company_id, company_name, career_role_id, location, description "
                   + "FROM company WHERE career_role_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, roleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Company c = new Company(
                            rs.getInt("company_id"),
                            rs.getString("company_name"),
                            rs.getInt("career_role_id"),
                            rs.getString("location"),
                            rs.getString("description")
                    );
                    list.add(c);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
