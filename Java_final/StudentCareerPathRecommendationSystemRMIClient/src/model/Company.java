/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author elie
 */
public class Company implements Serializable{


    private static final long serialVersionUID = 1L;

    private int companyId;
    private String companyName;
    private CareerRole careerRole;  // associated career role
    private String location;
    private String description;

    public Company() { }

    public Company(int companyId) {
        this.companyId = companyId;
    }

    public Company(int companyId, String companyName, CareerRole careerRole, String location, String description) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.careerRole = careerRole;
        this.location = location;
        this.description = description;
    }

    // ================= GETTERS & SETTERS =================

    public int getCompanyId() { return companyId; }
    public void setCompanyId(int companyId) { this.companyId = companyId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public CareerRole getCareerRole() { return careerRole; }
    public void setCareerRole(CareerRole careerRole) { this.careerRole = careerRole; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

}
