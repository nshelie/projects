/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;
import model.User;
/**
 *
 * @author elie
 */
public class Session {

    public static int currentUserId;
    public static String currentUserName;
    public static String currentUserEmail;
    public static String currentUserRole;
    public static String currentUserDepartment;

    public static int currentStudentId;  // 👈 NEW: the id from students table

    public static User currentUser; // store the full user object

}
