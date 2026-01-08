/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.time.LocalDate;
/**
 *
 * @author User-104
 */
public class Payment {
    
    private int paymentId;
    private int tenantId;
    private int apartmentId;
    private String rentType;         // daily, weekly, monthly, yearly
    private double rentAmount;       // price per period
    private double amountPaid;       // money tenant paid
    private double paymentForDuration; // number of periods (calculated automatically)
    private LocalDate datePaid;
    private LocalDate nextDueDate;   // calculated automatically

    // -------------------------------
    // Constructors
    // -------------------------------

    // Empty constructor
    public Payment() {
    }

    // Constructor without paymentId (for inserting new payments)
    public Payment(int tenantId, int apartmentId, String rentType, double rentAmount,
                   double amountPaid, double paymentForDuration, LocalDate datePaid, LocalDate nextDueDate) {
        this.tenantId = tenantId;
        this.apartmentId = apartmentId;
        this.rentType = rentType;
        this.rentAmount = rentAmount;
        this.amountPaid = amountPaid;
        this.paymentForDuration = paymentForDuration;
        this.datePaid = datePaid;
        this.nextDueDate = nextDueDate;
    }

    // Constructor with paymentId (for retrieving from DB)
    public Payment(int paymentId, int tenantId, int apartmentId, String rentType, double rentAmount,
                   double amountPaid, double paymentForDuration, LocalDate datePaid, LocalDate nextDueDate) {
        this.paymentId = paymentId;
        this.tenantId = tenantId;
        this.apartmentId = apartmentId;
        this.rentType = rentType;
        this.rentAmount = rentAmount;
        this.amountPaid = amountPaid;
        this.paymentForDuration = paymentForDuration;
        this.datePaid = datePaid;
        this.nextDueDate = nextDueDate;
    }

    // -------------------------------
    // Getters and Setters
    // -------------------------------

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
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

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public double getPaymentForDuration() {
        return paymentForDuration;
    }

    public void setPaymentForDuration(double paymentForDuration) {
        this.paymentForDuration = paymentForDuration;
    }

    public LocalDate getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(LocalDate datePaid) {
        this.datePaid = datePaid;
    }

    public LocalDate getNextDueDate() {
        return nextDueDate;
    }

    public void setNextDueDate(LocalDate nextDueDate) {
        this.nextDueDate = nextDueDate;
    }

    // -------------------------------
    // Helper Method: Calculate Duration
    // -------------------------------
    public void calculatePaymentDuration() {
        this.paymentForDuration = this.amountPaid / this.rentAmount;
    }

    // -------------------------------
    // Helper Method: Calculate Next Due Date
    // -------------------------------
    public void calculateNextDueDate() {
        switch (rentType.toLowerCase()) {
            case "daily":
                this.nextDueDate = this.datePaid.plusDays((long) paymentForDuration);
                break;
            case "weekly":
                this.nextDueDate = this.datePaid.plusWeeks((long) paymentForDuration);
                break;
            case "monthly":
                this.nextDueDate = this.datePaid.plusMonths((long) paymentForDuration);
                break;
            case "yearly":
                this.nextDueDate = this.datePaid.plusYears((long) paymentForDuration);
                break;
            default:
                throw new IllegalArgumentException("Invalid rent type: " + rentType);
        }
    }    
}
