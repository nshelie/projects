/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;

/**
 *
 * @author elie
 */
public class CompanyDTO implements Serializable{

    private static final long serialVersionUID = 1L;

    private int companyId;
    private String companyName;
    private int careerRoleId;
    private String careerRoleName;
    private String location;
    private String description;

    public CompanyDTO() {}

    public int getCompanyId() { return companyId; }
    public void setCompanyId(int companyId) { this.companyId = companyId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public int getCareerRoleId() { return careerRoleId; }
    public void setCareerRoleId(int careerRoleId) { this.careerRoleId = careerRoleId; }

    public String getCareerRoleName() { return careerRoleName; }
    public void setCareerRoleName(String careerRoleName) { this.careerRoleName = careerRoleName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
