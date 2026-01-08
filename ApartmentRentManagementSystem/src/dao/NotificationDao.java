/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import model.Notification;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author User-104
 */
public class NotificationDao {
    // ----------------------------
    // Insert notification
    // ----------------------------
    public boolean insertNotification(Notification notification) {
        String sql = "INSERT INTO notification (tenant_id, apartment_id, message, type, status, created_at, due_date) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (notification.getTenantId() != null) {
                ps.setInt(1, notification.getTenantId());
            } else {
                ps.setNull(1, Types.INTEGER);
            }

            if (notification.getApartmentId() != null) {
                ps.setInt(2, notification.getApartmentId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }

            ps.setString(3, notification.getMessage());
            ps.setString(4, notification.getType());
            ps.setString(5, notification.getStatus());
            ps.setTimestamp(6, Timestamp.valueOf(notification.getCreatedAt()));
            if (notification.getDueDate() != null) {
                ps.setDate(7, Date.valueOf(notification.getDueDate()));
            } else {
                ps.setNull(7, Types.DATE);
            }

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.out.println("Insert Notification Error: " + ex.getMessage());
        }
        return false;
    }

    // ----------------------------
    // Get notification by ID
    // ----------------------------
    public Notification getNotificationById(int notificationId) {
        String sql = "SELECT * FROM notification WHERE notification_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, notificationId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Notification(
                    rs.getInt("notification_id"),
                    rs.getObject("tenant_id") != null ? rs.getInt("tenant_id") : null,
                    rs.getObject("apartment_id") != null ? rs.getInt("apartment_id") : null,
                    rs.getString("message"),
                    rs.getString("type"),
                    rs.getString("status"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getDate("due_date") != null ? rs.getDate("due_date").toLocalDate() : null
                );
            }

        } catch (SQLException ex) {
            System.out.println("Get Notification Error: " + ex.getMessage());
        }
        return null;
    }

    // ----------------------------
    // Update notification
    // ----------------------------
    public boolean updateNotification(Notification notification) {
        String sql = "UPDATE notification SET status = ? WHERE notification_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, notification.getStatus());
            ps.setInt(2, notification.getNotificationId());

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.out.println("Update Notification Error: " + ex.getMessage());
        }
        return false;
    }

    // ----------------------------
    // Delete notification
    // ----------------------------
    public boolean deleteNotification(int notificationId) {
        String sql = "DELETE FROM notification WHERE notification_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, notificationId);
            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            System.out.println("Delete Notification Error: " + ex.getMessage());
        }
        return false;
    }

    // ----------------------------
    // Get all notifications
    // ----------------------------
    public List<Notification> getAllNotifications() {
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT * FROM notification ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Notification n = new Notification(
                    rs.getInt("notification_id"),
                    rs.getObject("tenant_id") != null ? rs.getInt("tenant_id") : null,
                    rs.getObject("apartment_id") != null ? rs.getInt("apartment_id") : null,
                    rs.getString("message"),
                    rs.getString("type"),
                    rs.getString("status"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getDate("due_date") != null ? rs.getDate("due_date").toLocalDate() : null
                );
                list.add(n);
            }

        } catch (SQLException ex) {
            System.out.println("Get All Notifications Error: " + ex.getMessage());
        }
        return list;
    }

}
