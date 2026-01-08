/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service.implementation;

import dao.SubjectDao;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import model.Subject;
import service.SubjectService;

/**
 *
 * @author elie
 */
public class SubjectServiceImpl extends UnicastRemoteObject implements SubjectService{

    private SubjectDao subjectDao;

    // Constructor
    public SubjectServiceImpl() throws RemoteException {
        super(); // export object for RMI
        subjectDao = new SubjectDao();
    }

    // ------------------ SAVE SUBJECT ------------------
    @Override
    public Subject saveSubject(Subject subject) throws RemoteException {
        return subjectDao.registerSubject(subject);
    }

    // ------------------ UPDATE SUBJECT ------------------
    @Override
    public Subject updateSubject(Subject subject) throws RemoteException {
        return subjectDao.updateSubject(subject);
    }

    // ------------------ DELETE SUBJECT ------------------
    @Override
    public Subject deleteSubject(Subject subject) throws RemoteException {
        return subjectDao.deleteSubject(subject);
    }

    // ------------------ SEARCH SUBJECT BY ID ------------------
    @Override
    public Subject searchSubjectById(int subjectId) throws RemoteException {
        return subjectDao.findSubjectById(subjectId);
    }

    // ------------------ RETRIEVE ALL SUBJECTS ------------------
    @Override
    public List<Subject> retrieveAllSubjects() throws RemoteException {
        return subjectDao.findAllSubjects();
    }
}
