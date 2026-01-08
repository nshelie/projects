/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dto.CompanyDTO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.CareerRole;
import model.Company;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author elie
 */
public class CompanyDao {


    private CompanyDTO toDTO(Company c) {
        if (c == null) return null;

        CompanyDTO dto = new CompanyDTO();
        dto.setCompanyId(c.getCompanyId());
        dto.setCompanyName(c.getCompanyName());
        dto.setLocation(c.getLocation());
        dto.setDescription(c.getDescription());

        if (c.getCareerRole() != null) {
            dto.setCareerRoleId(c.getCareerRole().getRoleId());
            dto.setCareerRoleName(c.getCareerRole().getRoleName());
        }
        return dto;
    }

    // ------------------ ADD COMPANY ------------------
    public CompanyDTO addCompany(CompanyDTO dto) {
        Transaction tr = null;
        Session ss = null;

        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            tr = ss.beginTransaction();

            CareerRole role = (CareerRole) ss.get(CareerRole.class, dto.getCareerRoleId());
            if (role == null) {
                tr.rollback();
                return null;
            }

            Company c = new Company();
            c.setCompanyName(dto.getCompanyName());
            c.setLocation(dto.getLocation());
            c.setDescription(dto.getDescription());
            c.setCareerRole(role);

            ss.save(c);

            tr.commit();
            return toDTO(c);

        } catch (Exception ex) {
            if (tr != null) tr.rollback();
            ex.printStackTrace();
            return null;
        } finally {
            if (ss != null) ss.close();
        }
    }

    // ------------------ UPDATE COMPANY ------------------
    public CompanyDTO updateCompany(CompanyDTO dto) {
        Transaction tr = null;
        Session ss = null;

        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            tr = ss.beginTransaction();

            Company existing = (Company) ss.get(Company.class, dto.getCompanyId());
            if (existing == null) {
                tr.rollback();
                return null;
            }

            CareerRole role = (CareerRole) ss.get(CareerRole.class, dto.getCareerRoleId());
            if (role == null) {
                tr.rollback();
                return null;
            }

            existing.setCompanyName(dto.getCompanyName());
            existing.setLocation(dto.getLocation());
            existing.setDescription(dto.getDescription());
            existing.setCareerRole(role);

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

    // ------------------ DELETE COMPANY ------------------
    public CompanyDTO deleteCompany(CompanyDTO dto) {
        Transaction tr = null;
        Session ss = null;

        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            tr = ss.beginTransaction();

            Company existing = (Company) ss.get(Company.class, dto.getCompanyId());
            if (existing == null) {
                tr.rollback();
                return null;
            }

            CompanyDTO out = toDTO(existing);
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

    // ------------------ FIND BY ID ------------------
    public CompanyDTO findById(int companyId) {
        Session ss = null;
        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            Company c = (Company) ss.get(Company.class, companyId);
            return toDTO(c);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (ss != null) ss.close();
        }
    }

    // ------------------ FIND ALL COMPANIES ------------------
    public List<CompanyDTO> findAll() {
        Transaction tr = null;
        Session ss = null;

        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            tr = ss.beginTransaction();

            @SuppressWarnings("unchecked")
            List<Company> list = ss.createQuery("FROM Company").list();

            tr.commit();

            if (list == null) return Collections.emptyList();
            List<CompanyDTO> out = new ArrayList<>();
            for (Company c : list) out.add(toDTO(c));
            return out;

        } catch (Exception ex) {
            if (tr != null) tr.rollback();
            ex.printStackTrace();
            return Collections.emptyList();
        } finally {
            if (ss != null) ss.close();
        }
    }

    // ------------------ GET COMPANIES BY ROLE ID ------------------
    public List<CompanyDTO> getCompaniesByRoleId(int roleId) {
        Transaction tr = null;
        Session ss = null;

        try {
            ss = HibernateUtil.getSessionFactory().openSession();
            tr = ss.beginTransaction();

            // FIX: your field is careerRole (not careerRoles)
            String hql = "FROM Company c WHERE c.careerRole.roleId = :roleId";

            @SuppressWarnings("unchecked")
            List<Company> list = ss.createQuery(hql)
                    .setParameter("roleId", roleId)
                    .list();

            tr.commit();

            if (list == null) return Collections.emptyList();
            List<CompanyDTO> out = new ArrayList<>();
            for (Company c : list) out.add(toDTO(c));
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
