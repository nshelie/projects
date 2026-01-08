/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementation;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import dao.StudentDao;
import model.Student;
import service.StudentService;

/**
 *
 * @author elie
 */
public class StudentServiceImpl extends UnicastRemoteObject implements StudentService  {


    private StudentDao studentDao;

    public StudentServiceImpl() throws RemoteException {
        super();
        studentDao = new StudentDao();
    }

    @Override
    public Student saveStudent(Student student) throws RemoteException {
        return studentDao.saveStudent(student);
    }

    @Override
    public Student updateStudent(Student student) throws RemoteException {
        return studentDao.updateStudent(student);
    }

    @Override
    public boolean deleteStudent(int studentId) throws RemoteException {
        Student s = studentDao.findStudentById(studentId);
        if (s != null) {
            studentDao.deleteStudent(s);
            return true;
        }
        return false;
    }

    @Override
    public Student findStudentById(int studentId) throws RemoteException {
        return studentDao.findStudentById(studentId);
    }

    @Override
    public Student findStudentByUserId(int userId) throws RemoteException {
        return studentDao.findStudentByUserId(userId);
    }

    @Override
    public List<Student> retrieveAllStudents() throws RemoteException {
        return studentDao.findAllStudents();
    }

    @Override
    public List<String> getAllDepartments() throws RemoteException {
        return studentDao.getAllDepartments();
    }

}
