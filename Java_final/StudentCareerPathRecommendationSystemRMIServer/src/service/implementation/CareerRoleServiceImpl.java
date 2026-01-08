/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementation;

import dao.CareerRoleDao;
import dto.CareerRoleDTO;
import dto.RecommendedStudentDTO;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import service.CareerRoleService;

/**
 *
 * @author elie
 */
public class CareerRoleServiceImpl extends UnicastRemoteObject implements CareerRoleService{

    private final CareerRoleDao careerRoleDao;

    public CareerRoleServiceImpl() throws RemoteException {
        super();
        careerRoleDao = new CareerRoleDao();
    }

    @Override
    public CareerRoleDTO addCareerRole(CareerRoleDTO careerRole) throws RemoteException {
        return careerRoleDao.addCareerRole(careerRole);
    }

    @Override
    public CareerRoleDTO updateCareerRole(CareerRoleDTO careerRole) throws RemoteException {
        return careerRoleDao.updateCareerRole(careerRole);
    }

    @Override
    public CareerRoleDTO deleteCareerRole(CareerRoleDTO careerRole) throws RemoteException {
        return careerRoleDao.deleteCareerRole(careerRole);
    }

    @Override
    public CareerRoleDTO findCareerRoleById(int roleId) throws RemoteException {
        return careerRoleDao.findById(roleId);
    }

    @Override
    public List<CareerRoleDTO> retrieveAllCareerRoles() throws RemoteException {
        return careerRoleDao.getAll();
    }

    @Override
    public List<CareerRoleDTO> getRolesBySubjectId(int subjectId) throws RemoteException {
        return careerRoleDao.getRolesBySubjectId(subjectId);
    }

    @Override
    public List<CareerRoleDTO> getRecommendationsForStudent(int studentId) throws RemoteException {
        return careerRoleDao.getRecommendationsForStudent(studentId);
    }

    @Override
    public List<RecommendedStudentDTO> getRecommendedStudentsForCareer(int roleId) throws RemoteException {
        return careerRoleDao.getRecommendedStudentsForCareer(roleId);
    }
}
