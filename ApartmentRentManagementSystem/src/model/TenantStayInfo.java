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
public class TenantStayInfo {

    private String tenantName;
    private String apartmentNumber;
    private LocalDate nextDueDate;
    private String status;

    public TenantStayInfo(String tenantName, String apartmentNumber, LocalDate nextDueDate, String status) {
        this.tenantName = tenantName;
        this.apartmentNumber = apartmentNumber;
        this.nextDueDate = nextDueDate;
        this.status = status;
    }

    public String getTenantName() { return tenantName; }
    public String getApartmentNumber() { return apartmentNumber; }
    public LocalDate getNextDueDate() { return nextDueDate; }
    public String getStatus() { return status; }    
}
