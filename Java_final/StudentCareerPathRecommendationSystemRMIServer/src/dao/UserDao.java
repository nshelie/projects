/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;


/**
 *
 * @author elie
 */
public class UserDao {


    // ------------------ REGISTER / ADD USER ------------------
    public User registerUser(User userObj) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.save(userObj);
            tr.commit();
            ss.close();
            return userObj;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // ------------------ UPDATE USER ------------------
    public User updateUser(User userObj) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.update(userObj);
            tr.commit();
            ss.close();
            return userObj;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // ------------------ DELETE USER ------------------
    public User deleteUser(User userObj) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.delete(userObj);
            tr.commit();
            ss.close();
            return userObj;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // ------------------ FIND BY ID ------------------
    public User findUserById(int userId) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            User theUser = (User) ss.get(User.class, userId);
            ss.close();
            return theUser;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // ------------------ FIND ALL USERS ------------------
public List<User> findAllUsers() {
    try {
        Session ss = HibernateUtil.getSessionFactory().openSession();
        Transaction tr = ss.beginTransaction();

        // Older Hibernate: no Class<T> argument
        List<User> users = ss.createQuery("FROM User")  // returns raw Query
                             .list();                  // convert to List
        tr.commit();
        ss.close();
        return users;
    } catch (Exception ex) {
        ex.printStackTrace();
        return null;
    }
}

// ------------------ LOGIN ------------------
public User login(String email, String password) {
    try {
        Session ss = HibernateUtil.getSessionFactory().openSession();
        Transaction tr = ss.beginTransaction();

        String hql = "FROM User WHERE email = :email AND password = :password";
        User user = (User) ss.createQuery(hql)  // removed User.class
                             .setParameter("email", email)
                             .setParameter("password", password)
                             .uniqueResult(); // returns Object, cast to User

        tr.commit();
        ss.close();
        return user; // null if not found
    } catch (Exception ex) {
        ex.printStackTrace();
        return null;
    }
}

 // ------------------ CHECK IF EMAIL EXISTS ------------------
public boolean isEmailTaken(String email) {
    try {
        Session ss = HibernateUtil.getSessionFactory().openSession();
        Transaction tr = ss.beginTransaction();
        String hql = "SELECT 1 FROM User WHERE email = :email";
        List<?> list = ss.createQuery(hql)
                         .setParameter("email", email)
                         .list(); // <-- change here
        tr.commit();
        ss.close();
        return !list.isEmpty();
    } catch (Exception ex) {
        ex.printStackTrace();
        return true; // treat exception as taken
    }
}

// Save OTP for user
public boolean saveOtp(User user, String otp) {
    try {
        Session ss = HibernateUtil.getSessionFactory().openSession();
        Transaction tr = ss.beginTransaction();

        user.setOtp(otp); // make sure User model has otp field
        user.setOtpGeneratedTime(LocalDateTime.now()); // make sure User model has otpGeneratedTime field
        ss.update(user);

        tr.commit();
        ss.close();
        return true;
    } catch (Exception ex) {
        ex.printStackTrace();
        return false;
    }
}


// Verify OTP (valid for 5 minutes)
public boolean verifyOtp(String email, String otp) {
    try {
        Session ss = HibernateUtil.getSessionFactory().openSession();
        Transaction tr = ss.beginTransaction();

        String hql = "FROM User WHERE email = :email";
        User user = (User) ss.createQuery(hql)
                             .setParameter("email", email)
                             .uniqueResult();

        tr.commit();
        ss.close();

        if (user == null || user.getOtp() == null) return false;

        Duration duration = Duration.between(user.getOtpGeneratedTime(), LocalDateTime.now());
        if (user.getOtp().equals(otp) && duration.toMinutes() <= 5) {
            clearOtp(user); // clear OTP after success
            return true;
        }

        return false;
    } catch (Exception ex) {
        ex.printStackTrace();
        return false;
    }
}


// Clear OTP after use
public boolean clearOtp(User user) {
    try {
        Session ss = HibernateUtil.getSessionFactory().openSession();
        Transaction tr = ss.beginTransaction();

        user.setOtp(null);
        user.setOtpGeneratedTime(null);
        ss.update(user);

        tr.commit();
        ss.close();
        return true;
    } catch (Exception ex) {
        ex.printStackTrace();
        return false;
    }
}

// In UserDao
public String generateAndSaveOtp(User user) {
    try {
        String otp = util.OTPGenerator.generateOtp(); // generate OTP
        saveOtp(user, otp);                           // reuse existing saveOtp method
        return otp;                                   // return OTP
    } catch (Exception ex) {
        ex.printStackTrace();
        return null;
    }
}

 // In UserDao or UserServiceImpl
public boolean generateAndSendOtp(User user) {
    try {
        // 1. Generate OTP
        String otp = util.OTPGenerator.generateOtp();

        // 2. Save OTP in DB
        saveOtp(user, otp);

        // 3. Send OTP via email
        return util.EmailSender.sendOtpEmail(user.getEmail(), otp);

    } catch (Exception ex) {
        ex.printStackTrace();
        return false;
    }
}


}
