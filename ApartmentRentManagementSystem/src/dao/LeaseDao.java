/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import model.Lease;
import java.sql.*;
import java.util.ArrayList;
/**
 *
 * @author User-104
 */
public class LeaseDao {

    // Insert a new lease
    public boolean insertLease(Lease lease) {
        String sql = "INSERT INTO lease (tenant_id, apartment_id, rent_type, start_date, end_date, rent_amount, is_active) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, lease.getTenantId());
            ps.setInt(2, lease.getApartmentId());
            ps.setString(3, lease.getRentType());
            ps.setString(4, lease.getStartDate());
            ps.setString(5, lease.getEndDate());
            ps.setDouble(6, lease.getRentAmount());
           ps.setBoolean(7, lease.isIsActive());

            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            System.out.println("Insert Lease Error: " + ex.getMessage());
            return false;
        }
    }

    // Update lease
    public boolean updateLease(Lease lease) {
        String sql = "UPDATE lease SET tenant_id=?, apartment_id=?, rent_type=?, start_date=?, end_date=?, rent_amount=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, lease.getTenantId());
            ps.setInt(2, lease.getApartmentId());
            ps.setString(3, lease.getRentType());
            ps.setString(4, lease.getStartDate());
            ps.setString(5, lease.getEndDate());
            ps.setDouble(6, lease.getRentAmount());
            ps.setInt(7, lease.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            System.out.println("Update Lease Error: " + ex.getMessage());
            return false;
        }
    }

    // Delete lease
    public boolean deleteLease(int leaseId) {
        String sql = "DELETE FROM lease WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, leaseId);
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            System.out.println("Delete Lease Error: " + ex.getMessage());
            return false;
        }
    }

    // End lease (set is_active = false)
    public boolean endLease(int leaseId) {
        String sql = "UPDATE lease SET is_active=false WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, leaseId);
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            System.out.println("End Lease Error: " + ex.getMessage());
            return false;
        }
    }

    // Get all leases
    public ArrayList<Lease> getAllLeases() {
        ArrayList<Lease> list = new ArrayList<>();
        String sql = "SELECT l.id, t.full_name, a.apartment_number, l.rent_type, l.start_date, l.end_date, l.rent_amount " +
                     "FROM lease l " +
                     "JOIN tenant t ON l.tenant_id = t.id " +
                     "JOIN apartment a ON l.apartment_id = a.id " +
                     "ORDER BY l.id DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Lease l = new Lease();
                l.setId(rs.getInt(1));
                l.setTenantName(rs.getString(2));
                l.setApartmentNumber(rs.getString(3));
                l.setRentType(rs.getString(4));
                l.setStartDate(rs.getString(5));
                l.setEndDate(rs.getString(6));
                l.setRentAmount(rs.getDouble(7));
                list.add(l);
            }
        } catch (Exception ex) {
            System.out.println("Get All Leases Error: " + ex.getMessage());
        }
        return list;
    }

    // Search leases by tenant name or apartment number
    public ArrayList<Lease> searchLeases(String keyword) {
        ArrayList<Lease> list = new ArrayList<>();
        String sql = "SELECT l.id, t.full_name, a.apartment_number, l.rent_type, l.start_date, l.end_date, l.rent_amount " +
                     "FROM lease l " +
                     "JOIN tenant t ON l.tenant_id = t.id " +
                     "JOIN apartment a ON l.apartment_id = a.id " +
                     "WHERE t.full_name LIKE ? OR a.apartment_number LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Lease l = new Lease();
                l.setId(rs.getInt(1));
                l.setTenantName(rs.getString(2));
                l.setApartmentNumber(rs.getString(3));
                l.setRentType(rs.getString(4));
                l.setStartDate(rs.getString(5));
                l.setEndDate(rs.getString(6));
                l.setRentAmount(rs.getDouble(7));
                list.add(l);
            }
        } catch (Exception ex) {
            System.out.println("Search Lease Error: " + ex.getMessage());
        }
        return list;
    }
}
