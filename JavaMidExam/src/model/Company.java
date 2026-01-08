/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author elie
 */
public class Company {
  
    private int companyId;
    private String companyName;
    private int careerRoleId;
    private String location;
    private String description;

    // Constructor
    public Company(int companyId, String companyName, int careerRoleId, String location, String description) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.careerRoleId = careerRoleId;
        this.location = location;
        this.description = description;
    }

    // Getters and Setters
    public int getCompanyId() { return companyId; }
    public void setCompanyId(int companyId) { this.companyId = companyId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public int getCareerRoleId() { return careerRoleId; }
    public void setCareerRoleId(int careerRoleId) { this.careerRoleId = careerRoleId; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

}
