/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import model.Subject;
import model.Subject;
import org.hibernate.Session;
import org.hibernate.Transaction;
/**
 *
 * @author elie
 */
public class SubjectDao {


    // ------------------ CREATE SUBJECT ------------------
    public Subject registerSubject(Subject subjectObj) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();

            ss.save(subjectObj);

            tr.commit();
            ss.close();
            return subjectObj;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // ------------------ UPDATE SUBJECT ------------------
    public Subject updateSubject(Subject subjectObj) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();

            ss.update(subjectObj);

            tr.commit();
            ss.close();
            return subjectObj;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // ------------------ DELETE SUBJECT ------------------
    public Subject deleteSubject(Subject subjectObj) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();

            ss.delete(subjectObj);

            tr.commit();
            ss.close();
            return subjectObj;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // ------------------ FIND SUBJECT BY ID ------------------
    public Subject findSubjectById(int id) {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Subject s = (Subject) ss.get(Subject.class, id);
            ss.close();
            return s;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // ------------------ FIND ALL SUBJECTS ------------------
    public List<Subject> findAllSubjects() {
        try {
            Session ss = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = ss.beginTransaction();

            List<Subject> subjects = ss.createQuery("FROM Subject").list();

            tr.commit();
            ss.close();
            return subjects;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
   
}
