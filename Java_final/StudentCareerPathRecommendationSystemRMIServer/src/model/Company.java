/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author elie
 */

@Entity
public class Company implements Serializable{
  
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int companyId;

    @Column(nullable = false, unique = true)
    private String companyName;

    private String location;

    private String description;

    // Many-to-One relationship: Many companies can have the same CareerRole
    @ManyToOne
    @JoinColumn(name = "career_role_id", nullable = false)
    private CareerRole careerRole;

    public Company() {
    }

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

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CareerRole getCareerRole() {
        return careerRole;
    }

    public void setCareerRole(CareerRole careerRole) {
        this.careerRole = careerRole;
    }

}
