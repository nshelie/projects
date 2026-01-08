/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import model.Payment;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User-104
 */
public class PaymentDao {

    // -------------------------------
    // Insert new payment
    // -------------------------------
    public boolean insertPayment(Payment payment) {
        String sql = "INSERT INTO payment (tenant_id, apartment_id, rent_type, rent_amount, amount_paid, payment_for_duration, date_paid, next_due_date) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            payment.calculatePaymentDuration();
            payment.calculateNextDueDate();

            ps.setInt(1, payment.getTenantId());
            ps.setInt(2, payment.getApartmentId());
            ps.setString(3, payment.getRentType());
            ps.setDouble(4, payment.getRentAmount());
            ps.setDouble(5, payment.getAmountPaid());
            ps.setDouble(6, payment.getPaymentForDuration());
            ps.setDate(7, Date.valueOf(payment.getDatePaid()));
            ps.setDate(8, Date.valueOf(payment.getNextDueDate()));

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.out.println("Insert Payment Error: " + ex.getMessage());
            return false;
        }
    }

    // -------------------------------
    // Update payment
    // -------------------------------
    public boolean updatePayment(Payment payment) {
        String sql = "UPDATE payment SET tenant_id=?, apartment_id=?, rent_type=?, rent_amount=?, amount_paid=?, payment_for_duration=?, date_paid=?, next_due_date=? "
                   + "WHERE payment_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            payment.calculatePaymentDuration();
            payment.calculateNextDueDate();

            ps.setInt(1, payment.getTenantId());
            ps.setInt(2, payment.getApartmentId());
            ps.setString(3, payment.getRentType());
            ps.setDouble(4, payment.getRentAmount());
            ps.setDouble(5, payment.getAmountPaid());
            ps.setDouble(6, payment.getPaymentForDuration());
            ps.setDate(7, Date.valueOf(payment.getDatePaid()));
            ps.setDate(8, Date.valueOf(payment.getNextDueDate()));
            ps.setInt(9, payment.getPaymentId());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.out.println("Update Payment Error: " + ex.getMessage());
            return false;
        }
    }

    // -------------------------------
    // Delete payment
    // -------------------------------
    public boolean deletePayment(int paymentId) {
        String sql = "DELETE FROM payment WHERE payment_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, paymentId);
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.out.println("Delete Payment Error: " + ex.getMessage());
            return false;
        }
    }

    // -------------------------------
    // Search payment by payment ID
    // -------------------------------
    public Payment searchPayment(int paymentId) {
        String sql = "SELECT * FROM payment WHERE payment_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, paymentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapResultSetToPayment(rs);

        } catch (SQLException ex) {
            System.out.println("Search Payment Error: " + ex.getMessage());
        }
        return null;
    }

    // -------------------------------
    // Search payments by keyword (tenant name or apartment number)
    // -------------------------------
    public List<Payment> searchPayments(String keyword) {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT p.* FROM payment p "
                   + "JOIN tenant t ON p.tenant_id = t.id "
                   + "JOIN apartment a ON p.apartment_id = a.id "
                   + "WHERE t.full_name LIKE ? OR a.apartment_number LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToPayment(rs));
            }

        } catch (SQLException ex) {
            System.out.println("Search Payments Error: " + ex.getMessage());
        }
        return list;
    }

    // -------------------------------
    // Get all payments
    // -------------------------------
    public List<Payment> getAllPayments() {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM payment";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapResultSetToPayment(rs));
            }

        } catch (SQLException ex) {
            System.out.println("Get All Payments Error: " + ex.getMessage());
        }
        return list;
    }

    // -------------------------------
    // Get payments by tenant
    // -------------------------------
    public List<Payment> getPaymentsByTenant(int tenantId) {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM payment WHERE tenant_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, tenantId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) list.add(mapResultSetToPayment(rs));

        } catch (SQLException ex) {
            System.out.println("Get Payments by Tenant Error: " + ex.getMessage());
        }
        return list;
    }

    // -------------------------------
    // Get payments by apartment
    // -------------------------------
    public List<Payment> getPaymentsByApartment(int apartmentId) {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM payment WHERE apartment_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, apartmentId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) list.add(mapResultSetToPayment(rs));

        } catch (SQLException ex) {
            System.out.println("Get Payments by Apartment Error: " + ex.getMessage());
        }
        return list;
    }

    // -------------------------------
    // Helper: Map ResultSet to Payment object
    // -------------------------------
    public Payment mapResultSetToPayment(ResultSet rs) throws SQLException {
        return new Payment(
            rs.getInt("payment_id"),
            rs.getInt("tenant_id"),
            rs.getInt("apartment_id"),
            rs.getString("rent_type"),
            rs.getDouble("rent_amount"),
            rs.getDouble("amount_paid"),
            rs.getDouble("payment_for_duration"),
            rs.getDate("date_paid").toLocalDate(),
            rs.getDate("next_due_date").toLocalDate()
        );
    }

}
