/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.PerformanceDTO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.Performance;
import model.Student;
import model.Subject;
import org.hibernate.Session;
import org.hibernate.Transaction;


/**
 *
 * @author elie
 */
public class PerformanceDao {



    // ---------- Helpers ----------
    private PerformanceDTO toDTO(Performance p) {
        if (p == null) return null;

        PerformanceDTO dto = new PerformanceDTO();
        dto.setPerformanceId(p.getPerformanceId());
        dto.setScore(p.getScore());
        dto.setGrade(p.getGrade());

        if (p.getStudent() != null) dto.setStudentId(p.getStudent().getStudentId());
        if (p.getSubject() != null) dto.setSubjectId(p.getSubject().getSubjectId());

        return dto;
    }

    // ---------- ADD ----------
    public PerformanceDTO addPerformance(PerformanceDTO dto) {
        Transaction tr = null;
        Session ss = null;

        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            tr = ss.beginTransaction();

            Student student = (Student) ss.get(Student.class, dto.getStudentId());
            Subject subject = (Subject) ss.get(Subject.class, dto.getSubjectId());
            if (student == null || subject == null) {
                tr.rollback();
                return null;
            }

            Performance p = new Performance();
            p.setStudent(student);
            p.setSubject(subject);
            p.setScore(dto.getScore());
            p.setGrade(calculateGrade(dto.getScore()));

            ss.save(p);

            tr.commit();
            return toDTO(p);

        } catch (Exception ex) {
            if (tr != null) tr.rollback();
            ex.printStackTrace();
            return null;

        } finally {
            if (ss != null) ss.close();
        }
    }

    // ---------- UPDATE ----------
    public PerformanceDTO updatePerformance(PerformanceDTO dto) {
        Transaction tr = null;
        Session ss = null;

        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            tr = ss.beginTransaction();

            Performance existing = (Performance) ss.get(Performance.class, dto.getPerformanceId());
            if (existing == null) {
                tr.rollback();
                return null;
            }

            Student student = (Student) ss.get(Student.class, dto.getStudentId());
            Subject subject = (Subject) ss.get(Subject.class, dto.getSubjectId());
            if (student == null || subject == null) {
                tr.rollback();
                return null;
            }

            existing.setStudent(student);
            existing.setSubject(subject);
            existing.setScore(dto.getScore());
            existing.setGrade(calculateGrade(dto.getScore()));

            ss.update(existing);

            tr.commit();
            return toDTO(existing);

        } catch (Exception ex) {
            if (tr != null) tr.rollback();
            ex.printStackTrace();
            return null;

        } finally {
            if (ss != null) ss.close();
        }
    }

    // ---------- DELETE ----------
    public PerformanceDTO deletePerformance(PerformanceDTO dto) {
        Transaction tr = null;
        Session ss = null;

        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            tr = ss.beginTransaction();

            Performance existing = (Performance) ss.get(Performance.class, dto.getPerformanceId());
            if (existing == null) {
                tr.rollback();
                return null;
            }

            PerformanceDTO out = toDTO(existing); // capture before delete
            ss.delete(existing);

            tr.commit();
            return out;

        } catch (Exception ex) {
            if (tr != null) tr.rollback();
            ex.printStackTrace();
            return null;

        } finally {
            if (ss != null) ss.close();
        }
    }

    // ---------- FIND BY ID ----------
    public PerformanceDTO findById(int id) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Performance p = (Performance) ss.get(Performance.class, id);
            return toDTO(p);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;

        } finally {
            if (ss != null) ss.close();
        }
    }

    // ---------- FIND ALL ----------
    public List<PerformanceDTO> findAll() {
        Transaction tr = null;
        Session ss = null;

        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            tr = ss.beginTransaction();

            @SuppressWarnings("unchecked")
List<Performance> list = ss.createQuery("FROM Performance").list();

            tr.commit();

            if (list == null) return Collections.emptyList();
            List<PerformanceDTO> out = new ArrayList<>();
            for (Performance p : list) out.add(toDTO(p));
            return out;

        } catch (Exception ex) {
            if (tr != null) tr.rollback();
            ex.printStackTrace();
            return Collections.emptyList();

        } finally {
            if (ss != null) ss.close();
        }
    }

    // ---------- FIND BY STUDENT ----------
    public List<PerformanceDTO> getPerformancesByStudentId(int studentId) {
        Transaction tr = null;
        Session ss = null;

        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            tr = ss.beginTransaction();

            // IMPORTANT FIX: query via relationship (student is an object)
            String hql = "FROM Performance p WHERE p.student.studentId = :studentId";
            @SuppressWarnings("unchecked")
List<Performance> list = ss.createQuery(hql)
        .setParameter("studentId", studentId)
        .list();
                    

            tr.commit();

            if (list == null) return Collections.emptyList();
            List<PerformanceDTO> out = new ArrayList<>();
            for (Performance p : list) out.add(toDTO(p));
            return out;

        } catch (Exception ex) {
            if (tr != null) tr.rollback();
            ex.printStackTrace();
            return Collections.emptyList(); // IMPORTANT: never return null list

        } finally {
            if (ss != null) ss.close();
        }
    }

    // ---------- GRADE ----------
    public String calculateGrade(double score) {
        if (score >= 90) return "A";
        else if (score >= 80) return "B";
        else if (score >= 70) return "C";
        else if (score >= 60) return "D";
        else return "F";
    }


}
