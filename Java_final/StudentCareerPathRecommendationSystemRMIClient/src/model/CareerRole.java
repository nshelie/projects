/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author elie
 */
public class CareerRole implements Serializable  {

    private static final long serialVersionUID = 1L;

    private int roleId;
    private String roleName;
    private String description;
    private int requiredMinScore;
    private Set<Subject> requiredSubjects; // associated subjects

    public CareerRole() { }

    public CareerRole(int roleId) {
        this.roleId = roleId;
    }

    public CareerRole(int roleId, String roleName, String description, int requiredMinScore, Set<Subject> requiredSubjects) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.description = description;
        this.requiredMinScore = requiredMinScore;
        this.requiredSubjects = requiredSubjects;
    }

    // ================= GETTERS & SETTERS =================

    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getRequiredMinScore() { return requiredMinScore; }
    public void setRequiredMinScore(int requiredMinScore) { this.requiredMinScore = requiredMinScore; }

    public Set<Subject> getRequiredSubjects() { return requiredSubjects; }
    public void setRequiredSubjects(Set<Subject> requiredSubjects) { this.requiredSubjects = requiredSubjects; }

}
