/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dto.PerformanceDTO;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author elie
 */
public interface PerformanceService extends Remote{

    PerformanceDTO addPerformance(PerformanceDTO performance) throws RemoteException;

    PerformanceDTO updatePerformance(PerformanceDTO performance) throws RemoteException;

    PerformanceDTO deletePerformance(PerformanceDTO performance) throws RemoteException;

    PerformanceDTO findPerformanceById(int performanceId) throws RemoteException;

    List<PerformanceDTO> retrieveAllPerformances() throws RemoteException;

    List<PerformanceDTO> getPerformancesByStudentId(int studentId) throws RemoteException;

}
