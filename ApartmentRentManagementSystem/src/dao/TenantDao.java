/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;
import java.sql.*;
import java.util.ArrayList;

import model.Tenant;

/**
 *
 * @author User-104
 */
public class TenantDao {

    // INSERT TENANT
    public boolean insertTenant(Tenant t) {
        String sql = "INSERT INTO tenant(full_name, gender, phone, national_id, move_in_date, apartment_id) VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, t.getFullName());
            pst.setString(2, t.getGender());
            pst.setString(3, t.getPhone());
            pst.setString(4, t.getNationalId());
            pst.setString(5, t.getMoveInDate());
            pst.setInt(6, t.getApartmentId());

            if (pst.executeUpdate() > 0) {
                updateApartmentStatus(t.getApartmentId(), true);
                return true;
            }

        } catch (Exception ex) {
            System.out.println("Insert Tenant Error: " + ex.getMessage());
        }
        return false;
    }

    // UPDATE TENANT
    public boolean updateTenant(Tenant t, int oldApartmentId) {
        String sql = "UPDATE tenant SET full_name=?, gender=?, phone=?, national_id=?, move_in_date=?, apartment_id=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, t.getFullName());
            pst.setString(2, t.getGender());
            pst.setString(3, t.getPhone());
            pst.setString(4, t.getNationalId());
            pst.setString(5, t.getMoveInDate());
            pst.setInt(6, t.getApartmentId());
            pst.setInt(7, t.getId());

            if (pst.executeUpdate() > 0) {

                // free old apartment if changed
                if (oldApartmentId != t.getApartmentId()) {
                    updateApartmentStatus(oldApartmentId, false);
                    updateApartmentStatus(t.getApartmentId(), true);
                }

                return true;
            }

        } catch (Exception ex) {
            System.out.println("Update Tenant Error: " + ex.getMessage());
        }

        return false;
    }

    // DELETE TENANT
   public boolean deleteTenant(int tenantId, int apartmentId) {

    String deletePayment = "DELETE FROM payment WHERE tenant_id=?";
    String deleteNotification = "DELETE FROM notification WHERE tenant_id=?";
    String deleteTenant = "DELETE FROM tenant WHERE id=?";

    try (Connection conn = DBConnection.getConnection()) {

        conn.setAutoCommit(false); // transaction start

        try (PreparedStatement pst1 = conn.prepareStatement(deletePayment);
             PreparedStatement pst2 = conn.prepareStatement(deleteNotification);
             PreparedStatement pst3 = conn.prepareStatement(deleteTenant)) {

            // Delete payments
            pst1.setInt(1, tenantId);
            pst1.executeUpdate();

            // Delete notifications
            pst2.setInt(1, tenantId);
            pst2.executeUpdate();

            // Delete tenant
            pst3.setInt(1, tenantId);
            int rows = pst3.executeUpdate();

            // Free apartment
            updateApartmentStatus(apartmentId, false);

            conn.commit(); // transaction commit
            return rows > 0;

        } catch (Exception ex) {
            conn.rollback(); // rollback if something fails
            System.out.println("Delete Tenant Error: " + ex.getMessage());
        } finally {
            conn.setAutoCommit(true);
        }

    } catch (Exception ex) {
        System.out.println("Delete Tenant Error: " + ex.getMessage());
    }

    return false;
}


    // GET ALL TENANTS + APARTMENT NUMBER
    public ArrayList<Tenant> getAllTenants() {

        ArrayList<Tenant> list = new ArrayList<>();

        String sql = "SELECT t.*, a.apartment_number FROM tenant t "
                   + "LEFT JOIN apartment a ON t.apartment_id = a.id";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Tenant t = new Tenant();

                t.setId(rs.getInt("id"));
                t.setFullName(rs.getString("full_name"));
                t.setGender(rs.getString("gender"));
                t.setPhone(rs.getString("phone"));
                t.setNationalId(rs.getString("national_id"));
                t.setMoveInDate(rs.getString("move_in_date"));
                t.setApartmentId(rs.getInt("apartment_id"));
                t.setApartmentNumber(rs.getString("apartment_number"));

                list.add(t);
            }

        } catch (Exception ex) {
            System.out.println("Get All Tenant Error: " + ex.getMessage());
        }

        return list;
    }

    // SEARCH TENANTS
    public ArrayList<Tenant> searchTenants(String keyword) {

        ArrayList<Tenant> list = new ArrayList<>();

        String sql = "SELECT t.*, a.apartment_number FROM tenant t "
                   + "LEFT JOIN apartment a ON t.apartment_id = a.id "
                   + "WHERE t.full_name LIKE ? OR t.phone LIKE ? OR t.national_id LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, "%" + keyword + "%");
            pst.setString(2, "%" + keyword + "%");
            pst.setString(3, "%" + keyword + "%");

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Tenant t = new Tenant();

                t.setId(rs.getInt("id"));
                t.setFullName(rs.getString("full_name"));
                t.setGender(rs.getString("gender"));
                t.setPhone(rs.getString("phone"));
                t.setNationalId(rs.getString("national_id"));
                t.setMoveInDate(rs.getString("move_in_date"));
                t.setApartmentId(rs.getInt("apartment_id"));
                t.setApartmentNumber(rs.getString("apartment_number"));

                list.add(t);
            }

        } catch (Exception ex) {
            System.out.println("Search Tenant Error: " + ex.getMessage());
        }

        return list;
    }

    // INTERNAL METHOD: UPDATE APARTMENT OCCUPANCY
    private void updateApartmentStatus(int apartmentId, boolean occupied) {
        String sql = "UPDATE apartment SET is_occupied=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setBoolean(1, occupied);
            pst.setInt(2, apartmentId);
            pst.executeUpdate();

        } catch (Exception ex) {
            System.out.println("Update Apartment Occupancy Error: " + ex.getMessage());
        }
    }
    
   public Tenant getTenantById(int id) {
    Tenant t = null;
    String sql = "SELECT t.*, a.apartment_number FROM tenant t "
               + "LEFT JOIN apartment a ON t.apartment_id = a.id "
               + "WHERE t.id = ?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement pst = con.prepareStatement(sql)) {

        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            t = new Tenant();
            t.setId(rs.getInt("id"));
            t.setFullName(rs.getString("full_name"));
            t.setGender(rs.getString("gender"));
            t.setPhone(rs.getString("phone"));
            t.setMoveInDate(rs.getString("move_in_date"));
            t.setApartmentId(rs.getInt("apartment_id"));
            t.setNationalId(rs.getString("national_id"));
            t.setApartmentNumber(rs.getString("apartment_number")); // ✅ Add this
        }

    } catch (Exception e) {
        System.out.println("Get Tenant By ID Error: " + e.getMessage());
    }

    return t;
}



}
