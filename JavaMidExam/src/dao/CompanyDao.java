/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import model.Company;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author elie
 */
public class CompanyDao {

    private static final String URL = "jdbc:mysql://localhost:3306/student_career_path_recommendation_system_db";
    private static final String USER = "root";
    private static final String PASS = "password123";

    // Add company
    public static boolean addCompany(Company c) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            String sql = "INSERT INTO company(company_name, career_role_id, location, description) VALUES(?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, c.getCompanyName());
            ps.setInt(2, c.getCareerRoleId());
            ps.setString(3, c.getLocation());
            ps.setString(4, c.getDescription());

            int inserted = ps.executeUpdate();
            ps.close();
            return inserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update company
    public static boolean updateCompany(Company c) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            String sql = "UPDATE company SET company_name=?, career_role_id=?, location=?, description=? WHERE company_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, c.getCompanyName());
            ps.setInt(2, c.getCareerRoleId());
            ps.setString(3, c.getLocation());
            ps.setString(4, c.getDescription());
            ps.setInt(5, c.getCompanyId());

            int updated = ps.executeUpdate();
            ps.close();
            return updated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete company
    public static boolean deleteCompany(int companyId) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            String sql = "DELETE FROM company WHERE company_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, companyId);

            int deleted = ps.executeUpdate();
            ps.close();
            return deleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get company by ID
    public static Company getCompanyById(int companyId) {
        Company c = null;
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            String sql = "SELECT * FROM company WHERE company_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, companyId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                c = new Company(
                        rs.getInt("company_id"),
                        rs.getString("company_name"),
                        rs.getInt("career_role_id"),
                        rs.getString("location"),
                        rs.getString("description")
                );
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    // Get all companies
    public static List<Company> getAllCompanies() {
        List<Company> companies = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            String sql = "SELECT * FROM company";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Company c = new Company(
                        rs.getInt("company_id"),
                        rs.getString("company_name"),
                        rs.getInt("career_role_id"),
                        rs.getString("location"),
                        rs.getString("description")
                );
                companies.add(c);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return companies;
    }
    
    // in dao/CompanyDao.java
public static List<Company> getCompaniesByRoleId(int roleId) {
    List<Company> companies = new ArrayList<>();
    String sql = "SELECT c.company_id, c.company_name, c.career_role_id, c.location, c.description "
               + "FROM company c "
               + "JOIN company_career_role ccr ON c.company_id = ccr.company_id "
               + "WHERE ccr.role_id = ?";
    try (Connection con = DriverManager.getConnection(URL, USER, PASS);
         PreparedStatement ps = con.prepareStatement(sql)) {
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
                companies.add(c);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return companies;
}

  // Check if a company already has this career role (for ADD)
    public static boolean existsByNameAndRole(String companyName, int careerRoleId) {
        String sql = "SELECT COUNT(*) FROM company WHERE company_name = ? AND career_role_id = ?";
        try (Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/student_career_path_recommendation_system_db",
                    "root", "password123");
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, companyName);
            ps.setInt(2, careerRoleId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Check if a company already has this career role (for UPDATE – excluding current record)
    public static boolean existsByNameAndRoleExceptId(String companyName, int careerRoleId, int companyId) {
        String sql = "SELECT COUNT(*) FROM company WHERE company_name = ? AND career_role_id = ? AND company_id <> ?";
        try (Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/student_career_path_recommendation_system_db",
                    "root", "password123");
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, companyName);
            ps.setInt(2, careerRoleId);
            ps.setInt(3, companyId);
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
