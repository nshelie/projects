/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
/**
 *
 * @author elie
 */

@Entity
public class CareerRole implements Serializable {

    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int roleId;

    @Column(nullable = false, unique = true)
    private String roleName;

    private String description;

    @Column(nullable = false)
    private int requiredMinScore;

    // Many-to-Many relationship with Subject
    @ManyToMany
    private Set<Subject> requiredSubjects;

    public CareerRole() {
    }

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

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRequiredMinScore() {
        return requiredMinScore;
    }

    public void setRequiredMinScore(int requiredMinScore) {
        this.requiredMinScore = requiredMinScore;
    }

    public Set<Subject> getRequiredSubjects() {
        return requiredSubjects;
    }

    public void setRequiredSubjects(Set<Subject> requiredSubjects) {
        this.requiredSubjects = requiredSubjects;
    }

}
