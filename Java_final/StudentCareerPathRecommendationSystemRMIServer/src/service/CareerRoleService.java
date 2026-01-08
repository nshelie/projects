/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dto.CareerRoleDTO;
import dto.RecommendedStudentDTO;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author elie
 */
public interface CareerRoleService extends Remote {

    CareerRoleDTO addCareerRole(CareerRoleDTO careerRole) throws RemoteException;

    CareerRoleDTO updateCareerRole(CareerRoleDTO careerRole) throws RemoteException;

    CareerRoleDTO deleteCareerRole(CareerRoleDTO careerRole) throws RemoteException;

    CareerRoleDTO findCareerRoleById(int roleId) throws RemoteException;

    List<CareerRoleDTO> retrieveAllCareerRoles() throws RemoteException;

    List<CareerRoleDTO> getRolesBySubjectId(int subjectId) throws RemoteException;

    // Student-facing recommendations
    List<CareerRoleDTO> getRecommendationsForStudent(int studentId) throws RemoteException;

    // Admin-facing: for a selected career role, return qualified students ordered by score DESC.
    List<RecommendedStudentDTO> getRecommendedStudentsForCareer(int roleId) throws RemoteException;
}
