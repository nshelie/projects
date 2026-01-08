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
public class CareerRole {

    private int roleId;
    private String roleName;
    private String description;
    private int requiredSubjectId;
    private int requiredMinScore;

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

    public int getRequiredSubjectId() {
        return requiredSubjectId;
    }
    public void setRequiredSubjectId(int requiredSubjectId) {
        this.requiredSubjectId = requiredSubjectId;
    }

    public int getRequiredMinScore() {
        return requiredMinScore;
    }
    public void setRequiredMinScore(int requiredMinScore) {
        this.requiredMinScore = requiredMinScore;
    }
}
