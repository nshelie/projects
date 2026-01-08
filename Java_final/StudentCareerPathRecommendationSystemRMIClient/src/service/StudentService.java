/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Student;

/**
 *
 * @author elie
 */
public interface StudentService extends Remote{

    Student saveStudent(Student student) throws RemoteException;

    Student updateStudent(Student student) throws RemoteException;

    boolean deleteStudent(int studentId) throws RemoteException;

    Student findStudentById(int studentId) throws RemoteException;

    Student findStudentByUserId(int userId) throws RemoteException;

    List<Student> retrieveAllStudents() throws RemoteException;

    List<String> getAllDepartments() throws RemoteException;
}
