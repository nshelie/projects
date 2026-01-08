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
public class Apartment {


    private int id;                  // Primary key
    private String apartmentNumber;  // Unique apartment number
    private String location;         // Location or block
    private double rentAmount;       // Rent amount
    private boolean isOccupied;      // True if occupied, false if available
    private String rent_type;
    // Default constructor
    public Apartment() {}

    // Constructor for displaying in table or combo
    public Apartment(int id, String apartmentNumber, boolean isOccupied) {
        this.id = id;
        this.apartmentNumber = apartmentNumber;
        this.isOccupied = isOccupied;
    }

    // Full constructor
    public Apartment(int id, String apartmentNumber, String location, double rentAmount, boolean isOccupied, String rent_type) {
        this.id = id;
        this.apartmentNumber = apartmentNumber;
        this.location = location;
        this.rentAmount = rentAmount;
        this.isOccupied = isOccupied;
        this.rent_type = rent_type;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getApartmentNumber() { return apartmentNumber; }
    public void setApartmentNumber(String apartmentNumber) { this.apartmentNumber = apartmentNumber; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public double getRentAmount() { return rentAmount; }
    public void setRentAmount(double rentAmount) { this.rentAmount = rentAmount; }

    public boolean isOccupied() { return isOccupied; }
    public void setOccupied(boolean occupied) { isOccupied = occupied; }

    public String getRent_type() {
        return rent_type;
    }

    public void setRent_type(String rent_type) {
        this.rent_type = rent_type;
    }
    
    

    @Override
    public String toString() {
        return apartmentNumber + (isOccupied ? " (Occupied)" : " (Available)");
    }
}
