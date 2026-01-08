/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import model.PendingMaintenance;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User-104
 */
public class PendingMaintenanceDAO {

    // ------------------------------------------------
    // Insert a new maintenance record
    // ------------------------------------------------
    public boolean insertMaintenance(PendingMaintenance maintenance) {
        String sql = "INSERT INTO pending_maintenance " +
                     "(apartment_id, description, vendor, estimated_cost, scheduled_date, status, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maintenance.getApartmentId());
            ps.setString(2, maintenance.getDescription());
            ps.setString(3, maintenance.getVendor());
            ps.setDouble(4, maintenance.getEstimatedCost());
            ps.setDate(5, Date.valueOf(maintenance.getScheduledDate()));
            ps.setString(6, maintenance.getStatus());
            ps.setTimestamp(7, Timestamp.valueOf(maintenance.getCreatedAt()));

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Insert Maintenance Error: " + e.getMessage());
            return false;
        }
    }

    // ------------------------------------------------
    // Update an existing maintenance record
    // ------------------------------------------------
    public boolean updateMaintenance(PendingMaintenance maintenance) {
        String sql = "UPDATE pending_maintenance SET " +
                     "apartment_id=?, description=?, vendor=?, estimated_cost=?, " +
                     "scheduled_date=?, status=? WHERE work_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maintenance.getApartmentId());
            ps.setString(2, maintenance.getDescription());
            ps.setString(3, maintenance.getVendor());
            ps.setDouble(4, maintenance.getEstimatedCost());
            ps.setDate(5, Date.valueOf(maintenance.getScheduledDate()));
            ps.setString(6, maintenance.getStatus());
            ps.setInt(7, maintenance.getWorkId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Update Maintenance Error: " + e.getMessage());
            return false;
        }
    }

    // ------------------------------------------------
    // Delete a maintenance record
    // ------------------------------------------------
    public boolean deleteMaintenance(int workId) {
        String sql = "DELETE FROM pending_maintenance WHERE work_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, workId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Delete Maintenance Error: " + e.getMessage());
            return false;
        }
    }

    // ------------------------------------------------
    // Fetch a maintenance record by ID
    // ------------------------------------------------
    public PendingMaintenance getMaintenanceById(int workId) {
        String sql = "SELECT * FROM pending_maintenance WHERE work_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, workId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToMaintenance(rs);
            }

        } catch (SQLException e) {
            System.out.println("Get Maintenance Error: " + e.getMessage());
        }
        return null;
    }

    // ------------------------------------------------
    // Fetch all maintenance records
    // ------------------------------------------------
    public List<PendingMaintenance> getAllMaintenance() {
        List<PendingMaintenance> list = new ArrayList<>();
        String sql = "SELECT * FROM pending_maintenance ORDER BY scheduled_date ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToMaintenance(rs));
            }

        } catch (SQLException e) {
            System.out.println("Get All Maintenance Error: " + e.getMessage());
        }

        return list;
    }

    // ------------------------------------------------
    // Helper method to map ResultSet to PendingMaintenance
    // ------------------------------------------------
    private PendingMaintenance mapResultSetToMaintenance(ResultSet rs) throws SQLException {
        int workId = rs.getInt("work_id");
        int apartmentId = rs.getInt("apartment_id");
        String description = rs.getString("description");
        String vendor = rs.getString("vendor");
        double estimatedCost = rs.getDouble("estimated_cost");
        LocalDate scheduledDate = rs.getDate("scheduled_date").toLocalDate();
        String status = rs.getString("status");
        LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

        return new PendingMaintenance(workId, apartmentId, description, vendor,
                                      estimatedCost, scheduledDate, status, createdAt);
    }

public String getApartmentNumberById(int apartmentId) {
    String sql = "SELECT apartment_number FROM apartment WHERE id = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, apartmentId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getString("apartment_number");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return ""; // return empty string if not found
}

// Search by Work ID
public PendingMaintenance searchMaintenanceById(int workId) {
    String sql = "SELECT * FROM pending_maintenance WHERE work_id = ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, workId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return mapResultSetToMaintenance(rs);
        }
    } catch (SQLException ex) {
        System.out.println("Search Maintenance by ID Error: " + ex.getMessage());
    }
    return null;
}

// Search by keyword (apartment number, description, vendor)
public List<PendingMaintenance> searchMaintenanceByKeyword(String keyword) {
    List<PendingMaintenance> list = new ArrayList<>();
    String sql = "SELECT pm.* FROM pending_maintenance pm "
               + "JOIN apartment a ON pm.apartment_id = a.id "
               + "WHERE a.apartment_number LIKE ? OR pm.description LIKE ? OR pm.vendor LIKE ?";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
        String kw = "%" + keyword + "%";
        ps.setString(1, kw);
        ps.setString(2, kw);
        ps.setString(3, kw);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(mapResultSetToMaintenance(rs));
        }
    } catch (SQLException ex) {
        System.out.println("Search Maintenance by keyword Error: " + ex.getMessage());
    }
    return list;
}

}
