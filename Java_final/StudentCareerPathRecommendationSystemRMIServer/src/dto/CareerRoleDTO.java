/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.io.Serializable;
import java.util.List;
/**
 *
 * @author elie
 */
public class CareerRoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int roleId;
    private String roleName;
    private String description;
    private int requiredMinScore;

    // send only subject IDs (safe), not Subject entities / Hibernate collections
    private List<Integer> requiredSubjectIds;

    // OPTIONAL: subject names for display in UI
    private List<String> requiredSubjectNames;

    public CareerRoleDTO() {}

    public int getRoleId() { return roleId; }
    public void setRoleId(int roleId) { this.roleId = roleId; }

    public String getRoleName() { return roleName; }
    public void setRoleName(String roleName) { this.roleName = roleName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getRequiredMinScore() { return requiredMinScore; }
    public void setRequiredMinScore(int requiredMinScore) { this.requiredMinScore = requiredMinScore; }

    public List<Integer> getRequiredSubjectIds() { return requiredSubjectIds; }
    public void setRequiredSubjectIds(List<Integer> requiredSubjectIds) { this.requiredSubjectIds = requiredSubjectIds; }

    public List<String> getRequiredSubjectNames() { return requiredSubjectNames; }
    public void setRequiredSubjectNames(List<String> requiredSubjectNames) { this.requiredSubjectNames = requiredSubjectNames; }
}
