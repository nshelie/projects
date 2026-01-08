/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author User-104
 */
public class Tenant {

    private int id;
    private String fullName;
    private String phone;
    private String gender;
    private String moveInDate;
    private int apartmentId;
    private String apartmentNumber; // for UI display
    private String nationalId;

    // Default constructor
    public Tenant() { }

    // Constructor for basic CRUD operations
    public Tenant(int id, String fullName, String phone, String gender, String moveInDate,
                  int apartmentId, String nationalId) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.gender = gender;
        this.moveInDate = moveInDate;
        this.apartmentId = apartmentId;
        this.nationalId = nationalId;
    }

    // Constructor including apartmentNumber for UI display
    public Tenant(int id, String fullName, String phone, String gender, String moveInDate,
                  int apartmentId, String nationalId, String apartmentNumber) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.gender = gender;
        this.moveInDate = moveInDate;
        this.apartmentId = apartmentId;
        this.nationalId = nationalId;
        this.apartmentNumber = apartmentNumber;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getMoveInDate() { return moveInDate; }
    public void setMoveInDate(String moveInDate) { this.moveInDate = moveInDate; }

    public int getApartmentId() { return apartmentId; }
    public void setApartmentId(int apartmentId) { this.apartmentId = apartmentId; }

    public String getApartmentNumber() { return apartmentNumber; }
    public void setApartmentNumber(String apartmentNumber) { this.apartmentNumber = apartmentNumber; }

    public String getNationalId() { return nationalId; }
    public void setNationalId(String nationalId) { this.nationalId = nationalId; }

    @Override
    public String toString() {
        return fullName + " - " + apartmentNumber;
    }
}
