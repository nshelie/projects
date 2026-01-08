/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.User;

/**
 *
 * @author elie
 */
public interface UserService extends Remote {

    // Register / Add a new user
    User saveUser(User user) throws RemoteException;

    // Update existing user
    User updateUser(User user) throws RemoteException;

    // Delete a user
    User deleteUser(User user) throws RemoteException;

    // Find user by ID
    User searchUserById(int userId) throws RemoteException;

    // Get all users
    List<User> retrieveAllUsers() throws RemoteException;

    // Additional for login
    User login(String email, String password) throws RemoteException;

    // Check if email is already taken
    boolean isEmailTaken(String email) throws RemoteException;

    // ================= OTP METHODS =================
    boolean saveOtp(User user, String otp) throws RemoteException;

    boolean verifyOtp(String email, String otp) throws RemoteException;
    
     
     String generateOtpForUser(User user) throws RemoteException;
     
     boolean generateAndSendOtp(User user) throws RemoteException;




}
