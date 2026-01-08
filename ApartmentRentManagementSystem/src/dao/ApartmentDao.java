/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Apartment;
/**
 *
 * @author User-104
 */
public class ApartmentDao {

    // INSERT apartment
    public boolean addApartment(Apartment apt) {
        // Ensure apartment number has prefix N-
        if (apt.getApartmentNumber() != null && !apt.getApartmentNumber().startsWith("N-")) {
            apt.setApartmentNumber("N-" + apt.getApartmentNumber());
        }

        // Check uniqueness
        if (existsApartmentNumber(apt.getApartmentNumber())) {
            System.out.println("Apartment number already exists: " + apt.getApartmentNumber());
            return false;
        }

        String sql = "INSERT INTO apartment (apartment_number, location, rent_amount, is_occupied, rent_type) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, apt.getApartmentNumber());
            pst.setString(2, apt.getLocation());
            pst.setDouble(3, apt.getRentAmount());
            pst.setBoolean(4, apt.isOccupied());
            pst.setString(5, apt.getRent_type());

            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Insert Apartment Error: " + e.getMessage());
            return false;
        }
    }

   public boolean updateApartment(Apartment apt) {
    // Ensure apartment number has prefix N-
    if (apt.getApartmentNumber() != null && !apt.getApartmentNumber().startsWith("N-")) {
        apt.setApartmentNumber("N-" + apt.getApartmentNumber());
    }

    // Check uniqueness excluding current id
    if (existsApartmentNumberExceptId(apt.getApartmentNumber(), apt.getId())) {
        System.out.println("Apartment number already used by another apartment: " + apt.getApartmentNumber());
        return false;
    }

    // GET current apartment to enforce business rules
    Apartment current = getApartmentById(apt.getId());
    if (current == null) return false;

    // Prevent illegal change: Occupied → Available
    if (current.isOccupied() && !apt.isOccupied()) {
        System.out.println("Occupied apartment cannot be marked as available manually!");
        return false;
    }

    // Prevent location change
    apt.setLocation(current.getLocation());

    String sql = "UPDATE apartment SET apartment_number=?, location=?, rent_amount=?, is_occupied=?, rent_type=? WHERE id=?";
    try (Connection con = DBConnection.getConnection();
         PreparedStatement pst = con.prepareStatement(sql)) {

        pst.setString(1, apt.getApartmentNumber());
        pst.setString(2, apt.getLocation());
        pst.setDouble(3, apt.getRentAmount());
        pst.setBoolean(4, apt.isOccupied());
        pst.setString(5, apt.getRent_type());
        pst.setInt(6, apt.getId());

        return pst.executeUpdate() > 0;

    } catch (Exception e) {
        System.err.println("Update Apartment Error: " + e.getMessage());
        return false;
    }
}


    // DELETE apartment
    public boolean deleteApartment(int id) {
        // Prevent deleting if occupied
        Apartment a = getApartmentById(id);
        if (a == null) {
            System.out.println("Apartment not found for delete: id=" + id);
            return false;
        }
        if (a.isOccupied()) {
            // Do not delete occupied apartment
            System.out.println("Cannot delete occupied apartment: id=" + id);
            return false;
        }

        String sql = "DELETE FROM apartment WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Delete Apartment Error: " + e.getMessage());
            return false;
        }
    }

    // GET all apartments
    public List<Apartment> getAllApartments() {
        List<Apartment> list = new ArrayList<>();
        String sql = "SELECT * FROM apartment ORDER BY id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Apartment a = new Apartment();
                a.setId(rs.getInt("id"));
                a.setApartmentNumber(rs.getString("apartment_number"));
                a.setLocation(rs.getString("location"));
                a.setRentAmount(rs.getDouble("rent_amount"));
                a.setOccupied(rs.getBoolean("is_occupied"));
                a.setRent_type(rs.getString("rent_type"));

                list.add(a);
            }

        } catch (Exception e) {
            System.err.println("Get All Apartments Error: " + e.getMessage());
        }

        return list;
    }

    // SEARCH apartments
    public List<Apartment> searchApartments(String keyword) {
        List<Apartment> list = new ArrayList<>();
        String sql = "SELECT * FROM apartment WHERE apartment_number LIKE ? OR location LIKE ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            String searchWord = "%" + keyword + "%";
            pst.setString(1, searchWord);
            pst.setString(2, searchWord);

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Apartment a = new Apartment();
                    a.setId(rs.getInt("id"));
                    a.setApartmentNumber(rs.getString("apartment_number"));
                    a.setLocation(rs.getString("location"));
                    a.setRentAmount(rs.getDouble("rent_amount"));
                    a.setOccupied(rs.getBoolean("is_occupied"));
                    a.setRent_type(rs.getString("rent_type"));
                    list.add(a);
                }
            }

        } catch (Exception e) {
            System.err.println("Search Apartment Error: " + e.getMessage());
        }

        return list;
    }

    // GET only available apartments (NOT occupied)
    public List<Apartment> getAllAvailableApartments() {
        List<Apartment> list = new ArrayList<>();
        String sql = "SELECT * FROM apartment WHERE is_occupied = FALSE";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Apartment a = new Apartment();
                a.setId(rs.getInt("id"));
                a.setApartmentNumber(rs.getString("apartment_number"));
                a.setLocation(rs.getString("location"));
                a.setRentAmount(rs.getDouble("rent_amount"));
                a.setOccupied(rs.getBoolean("is_occupied"));
                a.setRent_type(rs.getString("rent_type"));

                list.add(a);
            }

        } catch (Exception e) {
            System.err.println("Get Available Apartments Error: " + e.getMessage());
        }

        return list;
    }

    // UPDATE apartment to occupied/unoccupied
    public boolean updateOccupancy(int apartmentId, boolean occupied) {
        String sql = "UPDATE apartment SET is_occupied=? WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setBoolean(1, occupied);
            pst.setInt(2, apartmentId);

            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            System.err.println("Update Occupancy Error: " + e.getMessage());
            return false;
        }
    }

    public Apartment getApartmentById(int id) {
        Apartment a = null;
        String sql = "SELECT * FROM apartment WHERE id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    a = new Apartment();
                    a.setId(rs.getInt("id"));
                    a.setApartmentNumber(rs.getString("apartment_number"));
                    a.setLocation(rs.getString("location"));
                    a.setRentAmount(rs.getDouble("rent_amount"));
                    a.setOccupied(rs.getBoolean("is_occupied"));
                    a.setRent_type(rs.getString("rent_type"));
                }
            }

        } catch (Exception e) {
            System.err.println("Get Apartment By ID Error: " + e.getMessage());
        }

        return a;
    }

    // Helper: check if apartment number exists
    public boolean existsApartmentNumber(String apartmentNumber) {
        String sql = "SELECT COUNT(*) FROM apartment WHERE apartment_number = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, apartmentNumber);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (Exception e) {
            System.err.println("Exists ApartmentNumber Error: " + e.getMessage());
        }
        return false;
    }

    // Helper: check if apartment number exists for another id (for updates)
    public boolean existsApartmentNumberExceptId(String apartmentNumber, int id) {
        String sql = "SELECT COUNT(*) FROM apartment WHERE apartment_number = ? AND id <> ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, apartmentNumber);
            pst.setInt(2, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (Exception e) {
            System.err.println("Exists Except ID Error: " + e.getMessage());
        }
        return false;
    }

 public boolean isApartmentAvailable(int apartmentId) {
    String sql = "SELECT is_occupied FROM apartment WHERE id=?";
    try (Connection con = DBConnection.getConnection();
         PreparedStatement pst = con.prepareStatement(sql)) {
        pst.setInt(1, apartmentId);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return !rs.getBoolean("is_occupied");
        }
    } catch (Exception e) {
        System.err.println("Check Apartment Availability Error: " + e.getMessage());
    }
    return false;
}

    
}
