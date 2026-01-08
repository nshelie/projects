/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dto.CompanyDTO;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author elie
 */
public interface CompanyService extends Remote {

    CompanyDTO saveCompany(CompanyDTO company) throws RemoteException;

    CompanyDTO updateCompany(CompanyDTO company) throws RemoteException;

    CompanyDTO deleteCompany(CompanyDTO company) throws RemoteException;

    CompanyDTO searchCompanyById(int companyId) throws RemoteException;

    List<CompanyDTO> retrieveAllCompanies() throws RemoteException;

    List<CompanyDTO> getCompaniesByRoleId(int roleId) throws RemoteException;
}
