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
public class User {
    private int userId;
    private String fullName;
    private String email;
    private String password;
    private String role;
    private String department;  // ADD THIS

    public User() {}

    public User(int userId, String fullName, String email, String password, String role, String department) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.department = department;
    }

    // existing getters and setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    // NEW getter and setter for department
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
   
}
