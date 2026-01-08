/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;


/**
 *
 * @author User-104
 */
public class Lease {

    private int id;
    private int tenantId;
    private int apartmentId;
    private String tenantName;       // optional, for display in table
    private String apartmentNumber;  // optional, for display in table
    private String rentType;
    private double rentAmount;
    private String startDate;
    private String endDate;
    private boolean isActive;

    public Lease() {
    }

    // Constructor for full initialization
    public Lease(int id, int tenantId, int apartmentId, String rentType, double rentAmount, String startDate, String endDate, boolean isActive) {
        this.id = id;
        this.tenantId = tenantId;
        this.apartmentId = apartmentId;
        this.rentType = rentType;
        this.rentAmount = rentAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getTenantId() {
        return tenantId;
    }
    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public int getApartmentId() {
        return apartmentId;
    }
    public void setApartmentId(int apartmentId) {
        this.apartmentId = apartmentId;
    }

    public String getTenantName() {
        return tenantName;
    }
    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }
    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public String getRentType() {
        return rentType;
    }
    public void setRentType(String rentType) {
        this.rentType = rentType;
    }

    public double getRentAmount() {
        return rentAmount;
    }
    public void setRentAmount(double rentAmount) {
        this.rentAmount = rentAmount;
    }

    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isIsActive() {
        return isActive;
    }
    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
