/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import model.Student;
import org.hibernate.Session;
import org.hibernate.Transaction;
import dao.HibernateUtil;

/**
 *
 * @author elie
 */
public class StudentDao {
 
    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 */

    // ------------------ CREATE ------------------
    public Student saveStudent(Student student) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.save(student);
            tr.commit();
            ss.close();
            return student;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // ------------------ UPDATE ------------------
    public Student updateStudent(Student student) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.update(student);
            tr.commit();
            ss.close();
            return student;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // ------------------ DELETE ------------------
    public Student deleteStudent(Student student) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();
            ss.delete(student);
            tr.commit();
            ss.close();
            return student;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // ------------------ FIND BY ID ------------------
    public Student findStudentById(int studentId) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Student student = (Student) ss.get(Student.class, studentId);
            ss.close();
            return student;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

// ------------------ FIND ALL ------------------
@SuppressWarnings("unchecked")
public List<Student> findAllStudents() {
    List<Student> students = null;
    try {
        Session ss = HibernateUtil.getSessionFactory().openSession();
        students = ss.createQuery("FROM Student").list(); // no second parameter
        ss.close();
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return students;
}

// ------------------ FIND BY USER ID ------------------
public Student findStudentByUserId(int userId) {
    Student student = null;
    try {
        Session ss = HibernateUtil.getSessionFactory().openSession();
        String hql = "FROM Student WHERE userId = :userId";
        List<Student> result = ss.createQuery(hql)
                                 .setParameter("userId", userId)
                                 .list(); // use list() instead of uniqueResult()
        if (!result.isEmpty()) {
            student = result.get(0);
        }
        ss.close();
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return student;
}


    // ------------------ GET DISTINCT DEPARTMENTS ------------------
 @SuppressWarnings("unchecked")
public List<String> getAllDepartments() {
    List<String> departments = null;
    try {
        Session ss = HibernateUtil.getSessionFactory().openSession();
        departments = ss.createQuery("SELECT DISTINCT department FROM Student").list();
        ss.close();
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return departments;
}


}
