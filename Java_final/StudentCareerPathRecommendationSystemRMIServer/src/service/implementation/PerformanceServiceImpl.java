/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementation;

import dao.PerformanceDao;
import dto.PerformanceDTO;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import service.PerformanceService;


import service.PerformanceService;

/**
 *
 * @author elie
 */
public class PerformanceServiceImpl extends UnicastRemoteObject implements PerformanceService {


    private final PerformanceDao performanceDao;

    public PerformanceServiceImpl() throws RemoteException {
        super();
        performanceDao = new PerformanceDao();
        System.out.println("PerformanceServiceImpl initialized");
    }

    @Override
    public PerformanceDTO addPerformance(PerformanceDTO performance) throws RemoteException {
        return performanceDao.addPerformance(performance);
    }

    @Override
    public PerformanceDTO updatePerformance(PerformanceDTO performance) throws RemoteException {
        return performanceDao.updatePerformance(performance);
    }

    @Override
    public PerformanceDTO deletePerformance(PerformanceDTO performance) throws RemoteException {
        return performanceDao.deletePerformance(performance);
    }

    @Override
    public PerformanceDTO findPerformanceById(int performanceId) throws RemoteException {
        return performanceDao.findById(performanceId);
    }

    @Override
    public List<PerformanceDTO> retrieveAllPerformances() throws RemoteException {
        return performanceDao.findAll();
    }

    @Override
    public List<PerformanceDTO> getPerformancesByStudentId(int studentId) throws RemoteException {
        return performanceDao.getPerformancesByStudentId(studentId);
    }

}
