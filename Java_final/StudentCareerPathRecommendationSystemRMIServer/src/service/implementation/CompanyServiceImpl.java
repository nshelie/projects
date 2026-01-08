/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementation;

import dao.CompanyDao;
import dto.CompanyDTO;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import service.CompanyService;

/**
 *
 * @author elie
 */
public class CompanyServiceImpl extends UnicastRemoteObject implements CompanyService{

    private final CompanyDao companyDao;

    public CompanyServiceImpl() throws RemoteException {
        super();
        companyDao = new CompanyDao();
    }

    @Override
    public CompanyDTO saveCompany(CompanyDTO company) throws RemoteException {
        return companyDao.addCompany(company);
    }

    @Override
    public CompanyDTO updateCompany(CompanyDTO company) throws RemoteException {
        return companyDao.updateCompany(company);
    }

    @Override
    public CompanyDTO deleteCompany(CompanyDTO company) throws RemoteException {
        return companyDao.deleteCompany(company);
    }

    @Override
    public CompanyDTO searchCompanyById(int companyId) throws RemoteException {
        return companyDao.findById(companyId);
    }

    @Override
    public List<CompanyDTO> retrieveAllCompanies() throws RemoteException {
        return companyDao.findAll();
    }

    @Override
    public List<CompanyDTO> getCompaniesByRoleId(int roleId) throws RemoteException {
        return companyDao.getCompaniesByRoleId(roleId);
    }

  
}
