/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;
import dto.CareerRoleDTO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import model.CareerRole;
import model.Subject;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author elie
 */
public class CareerRoleDao {

    private CareerRoleDTO toDTO(CareerRole cr) {
        if (cr == null) return null;

        CareerRoleDTO dto = new CareerRoleDTO();
        dto.setRoleId(cr.getRoleId());
        dto.setRoleName(cr.getRoleName());
        dto.setDescription(cr.getDescription());
        dto.setRequiredMinScore(cr.getRequiredMinScore());

        List<Integer> ids = new ArrayList<>();
        List<String> names = new ArrayList<>();

        if (cr.getRequiredSubjects() != null) {
            for (Subject s : cr.getRequiredSubjects()) {
                if (s != null) {
                    ids.add(s.getSubjectId());
                    names.add(s.getSubjectName());
                }
            }
        }

        dto.setRequiredSubjectIds(ids);
        dto.setRequiredSubjectNames(names);

        return dto;
    }

    private Set<Subject> toSubjectSet(Session ss, List<Integer> subjectIds) {
        Set<Subject> set = new HashSet<>();
        if (subjectIds == null) return set;

        for (Integer id : subjectIds) {
            if (id == null) continue;
            Subject managed = (Subject) ss.get(Subject.class, id);
            if (managed != null) set.add(managed);
        }
        return set;
    }

    public CareerRoleDTO addCareerRole(CareerRoleDTO dto) {
        Transaction tr = null;
        Session ss = null;

        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            tr = ss.beginTransaction();

            CareerRole cr = new CareerRole();
            cr.setRoleName(dto.getRoleName());
            cr.setDescription(dto.getDescription());
            cr.setRequiredMinScore(dto.getRequiredMinScore());
            cr.setRequiredSubjects(toSubjectSet(ss, dto.getRequiredSubjectIds()));

            ss.save(cr);

            tr.commit();
            return toDTO(cr);

        } catch (Exception ex) {
            if (tr != null) tr.rollback();
            ex.printStackTrace();
            return null;
        } finally {
            if (ss != null) ss.close();
        }
    }

    public CareerRoleDTO updateCareerRole(CareerRoleDTO dto) {
        Transaction tr = null;
        Session ss = null;

        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            tr = ss.beginTransaction();

            CareerRole existing = (CareerRole) ss.get(CareerRole.class, dto.getRoleId());
            if (existing == null) {
                tr.rollback();
                return null;
            }

            existing.setRoleName(dto.getRoleName());
            existing.setDescription(dto.getDescription());
            existing.setRequiredMinScore(dto.getRequiredMinScore());
            existing.setRequiredSubjects(toSubjectSet(ss, dto.getRequiredSubjectIds()));

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

    public CareerRoleDTO deleteCareerRole(CareerRoleDTO dto) {
        Transaction tr = null;
        Session ss = null;

        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            tr = ss.beginTransaction();

            CareerRole existing = (CareerRole) ss.get(CareerRole.class, dto.getRoleId());
            if (existing == null) {
                tr.rollback();
                return null;
            }

            CareerRoleDTO out = toDTO(existing);
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

    public CareerRoleDTO findById(int id) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            CareerRole cr = (CareerRole) ss.get(CareerRole.class, id);
            return toDTO(cr);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (ss != null) ss.close();
        }
    }

    public List<CareerRoleDTO> getAll() {
        Transaction tr = null;
        Session ss = null;

        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            tr = ss.beginTransaction();

            @SuppressWarnings("unchecked")
            List<CareerRole> list = ss.createQuery("FROM CareerRole").list();

            tr.commit();

            if (list == null) return Collections.emptyList();
            List<CareerRoleDTO> out = new ArrayList<>();
            for (CareerRole cr : list) out.add(toDTO(cr));
            return out;

        } catch (Exception ex) {
            if (tr != null) tr.rollback();
            ex.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (ss != null) ss.close();
        }
    }

    public List<CareerRoleDTO> getRolesBySubjectId(int subjectId) {
        Transaction tr = null;
        Session ss = null;

        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            tr = ss.beginTransaction();

            String hql = "SELECT DISTINCT cr FROM CareerRole cr JOIN cr.requiredSubjects s WHERE s.subjectId = :subjectId";
            @SuppressWarnings("unchecked")
            List<CareerRole> list = ss.createQuery(hql)
                    .setParameter("subjectId", subjectId)
                    .list();

            tr.commit();

            if (list == null) return Collections.emptyList();
            List<CareerRoleDTO> out = new ArrayList<>();
            for (CareerRole cr : list) out.add(toDTO(cr));
            return out;

        } catch (Exception ex) {
            if (tr != null) tr.rollback();
            ex.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (ss != null) ss.close();
        }
    }

    public List<CareerRoleDTO> getRecommendationsForStudent(int studentId) {
        Transaction tr = null;
        Session ss = null;

        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            tr = ss.beginTransaction();

            @SuppressWarnings("unchecked")
            List<CareerRole> roles = ss.createQuery("FROM CareerRole").list();

            if (roles == null) {
                tr.commit();
                return Collections.emptyList();
            }

            // For each career role: student is recommended only if they have a performance
            // record for ALL required subjects, and each score >= requiredMinScore.
            List<CareerRoleDTO> out = new ArrayList<>();
            for (CareerRole cr : roles) {
                if (cr == null) continue;
                if (cr.getRequiredSubjects() == null || cr.getRequiredSubjects().isEmpty()) continue;

                List<Integer> subjectIds = new ArrayList<>();
                for (Subject s : cr.getRequiredSubjects()) {
                    if (s != null) subjectIds.add(s.getSubjectId());
                }
                if (subjectIds.isEmpty()) continue;

                Long cnt = (Long) ss.createQuery(
                        "SELECT COUNT(DISTINCT p.subject.subjectId) "
                        + "FROM Performance p "
                        + "WHERE p.student.studentId = :studentId "
                        + "AND p.subject.subjectId IN (:subjectIds) "
                        + "AND p.score >= :minScore")
                        .setParameter("studentId", studentId)
                        .setParameterList("subjectIds", subjectIds)
                        .setParameter("minScore", (double) cr.getRequiredMinScore())
                        .uniqueResult();

                if (cnt != null && cnt.intValue() == subjectIds.size()) {
                    out.add(toDTO(cr));
                }
            }

            tr.commit();

            // Keep deterministic ordering
            Collections.sort(out, (a, b) -> Integer.compare(a.getRoleId(), b.getRoleId()));
            return out;

        } catch (Exception ex) {
            if (tr != null) tr.rollback();
            ex.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (ss != null) ss.close();
        }
    }

    public List<dto.RecommendedStudentDTO> getRecommendedStudentsForCareer(int roleId) {
        Transaction tr = null;
        Session ss = null;

        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            tr = ss.beginTransaction();

            CareerRole cr = (CareerRole) ss.get(CareerRole.class, roleId);
            if (cr == null) {
                tr.commit();
                return Collections.emptyList();
            }

            if (cr.getRequiredSubjects() == null || cr.getRequiredSubjects().isEmpty()) {
                tr.commit();
                return Collections.emptyList();
            }

            List<Integer> subjectIds = new ArrayList<>();
            for (Subject s : cr.getRequiredSubjects()) {
                if (s != null) subjectIds.add(s.getSubjectId());
            }
            if (subjectIds.isEmpty()) {
                tr.commit();
                return Collections.emptyList();
            }

            int subjectCount = subjectIds.size();
            double minScore = (double) cr.getRequiredMinScore();

            // Return qualified students ordered by their average score across the required subjects.
            // HAVING COUNT(DISTINCT subject) == subjectCount ensures the student has marks for ALL required subjects.
            String hql = "SELECT p.student.studentId, p.student.fullName, p.student.email, p.student.department, AVG(p.score) "
                    + "FROM Performance p "
                    + "WHERE p.subject.subjectId IN (:subjectIds) "
                    + "AND p.score >= :minScore "
                    + "GROUP BY p.student.studentId, p.student.fullName, p.student.email, p.student.department "
                    + "HAVING COUNT(DISTINCT p.subject.subjectId) = :subjectCount "
                    + "ORDER BY AVG(p.score) DESC";

            @SuppressWarnings("unchecked")
            List<Object[]> rows = ss.createQuery(hql)
                    .setParameterList("subjectIds", subjectIds)
                    .setParameter("minScore", minScore)
                    .setParameter("subjectCount", (long) subjectCount)
                    .list();

            tr.commit();

            if (rows == null) return Collections.emptyList();

            List<dto.RecommendedStudentDTO> out = new ArrayList<>();
            for (Object[] r : rows) {
                int studentId = (Integer) r[0];
                String fullName = (String) r[1];
                String email = (String) r[2];
                String dept = (String) r[3];
                double avg = r[4] == null ? 0.0 : ((Double) r[4]);
                out.add(new dto.RecommendedStudentDTO(studentId, fullName, email, dept, avg));
            }
            return out;

        } catch (Exception ex) {
            if (tr != null) tr.rollback();
            ex.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (ss != null) ss.close();
        }
    }
}
