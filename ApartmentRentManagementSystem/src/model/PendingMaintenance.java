/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 * @author User-104
 */
public class PendingMaintenance {

    private int workId;
    private int apartmentId;
    private String description;
    private String vendor;
    private double estimatedCost;
    private LocalDate scheduledDate;
    private String status;  // pending, completed, cancelled
    private LocalDateTime createdAt;

    // ------------------------------------------------
    // Constructor without ID (for new entries)
    // ------------------------------------------------
    public PendingMaintenance(int apartmentId, String description, String vendor,
                              double estimatedCost, LocalDate scheduledDate, String status) {
        this.apartmentId = apartmentId;
        this.description = description;
        this.vendor = vendor;
        this.estimatedCost = estimatedCost;
        this.scheduledDate = scheduledDate;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    // ------------------------------------------------
    // Full constructor (for loading from database)
    // ------------------------------------------------
    public PendingMaintenance(int workId, int apartmentId, String description, String vendor,
                              double estimatedCost, LocalDate scheduledDate, String status,
                              LocalDateTime createdAt) {
        this.workId = workId;
        this.apartmentId = apartmentId;
        this.description = description;
        this.vendor = vendor;
        this.estimatedCost = estimatedCost;
        this.scheduledDate = scheduledDate;
        this.status = status;
        this.createdAt = createdAt;
    }

    // ------------------------------------------------
    // Getters and Setters
    // ------------------------------------------------
    public int getWorkId() {
        return workId;
    }

    public void setWorkId(int workId) {
        this.workId = workId;
    }

    public int getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(int apartmentId) {
        this.apartmentId = apartmentId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public double getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(double estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDate scheduledDate) {
        this.scheduledDate = scheduledDate;
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
}
