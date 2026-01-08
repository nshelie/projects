/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementation;

import dao.UserDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import model.User;
import service.UserService;

/**
 *
 * @author elie
 */
public class UserServiceImpl extends UnicastRemoteObject implements UserService {

    private UserDao userDao;

    // Constructor
    public UserServiceImpl() throws RemoteException {
        super();  // export object for RMI
        userDao = new UserDao();
    }

    // ------------------ SAVE USER ------------------
    @Override
    public User saveUser(User user) throws RemoteException {
        return userDao.registerUser(user);  // returns User object or null
    }

    // ------------------ UPDATE USER ------------------
    @Override
    public User updateUser(User user) throws RemoteException {
        return userDao.updateUser(user);    // returns User object or null
    }

    // ------------------ DELETE USER ------------------
    @Override
    public User deleteUser(User user) throws RemoteException {
        return userDao.deleteUser(user);    // returns User object or null
    }

    // ------------------ SEARCH USER BY ID ------------------
    @Override
    public User searchUserById(int userId) throws RemoteException {
        return userDao.findUserById(userId);
    }

    // ------------------ RETRIEVE ALL USERS ------------------
    @Override
    public List<User> retrieveAllUsers() throws RemoteException {
        return userDao.findAllUsers();
    }

    // ------------------ LOGIN ------------------
    @Override
    public User login(String email, String password) throws RemoteException {
        return userDao.login(email, password);
    }

    // ------------------ CHECK EMAIL ------------------
    @Override
    public boolean isEmailTaken(String email) throws RemoteException {
        return userDao.isEmailTaken(email);
    }
   // ------------------ SAVE OTP ------------------
   @Override
    public boolean saveOtp(User user, String otp) throws RemoteException {
    return userDao.saveOtp(user, otp);
    }

    // ------------------ VERIFY OTP ------------------
   @Override
   public boolean verifyOtp(String email, String otp) throws RemoteException {
    return userDao.verifyOtp(email, otp);
   }
    
  @Override
public String generateOtpForUser(User user) throws RemoteException {
    return userDao.generateAndSaveOtp(user);
}
  
  @Override
public boolean generateAndSendOtp(User user) throws RemoteException {
    return userDao.generateAndSendOtp(user);
}


}
