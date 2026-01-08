/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.time.LocalDateTime;
import java.time.LocalDate;

/**
 *
 * @author User-104
 */
public class Notification {

    private int notificationId;
    private Integer tenantId;        // can be null
    private Integer apartmentId;     // can be null

    private String message;
    private String type;             // payment_due, overdue, maintenance...

    private String status;           // unread, read, dismissed

    private LocalDateTime createdAt;
    private LocalDate dueDate;       // only used for payment alerts

    // ------------------------------------------------
    // Constructor without ID (for inserting new notification)
    // ------------------------------------------------
    public Notification(Integer tenantId, Integer apartmentId,
                        String message, String type,
                        String status, LocalDate dueDate) {
        this.tenantId = tenantId;
        this.apartmentId = apartmentId;
        this.message = message;
        this.type = type;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.dueDate = dueDate;
    }

    // ------------------------------------------------
    // Full constructor (for loading from database)
    // ------------------------------------------------
    public Notification(int notificationId, Integer tenantId, Integer apartmentId,
                        String message, String type, String status,
                        LocalDateTime createdAt, LocalDate dueDate) {
        this.notificationId = notificationId;
        this.tenantId = tenantId;
        this.apartmentId = apartmentId;
        this.message = message;
        this.type = type;
        this.status = status;
        this.createdAt = createdAt;
        this.dueDate = dueDate;
    }

    // ------------------------------------------------
    // Getters and Setters
    // ------------------------------------------------
    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public Integer getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(Integer apartmentId) {
        this.apartmentId = apartmentId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
