/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Subject;

/**
 *
 * @author elie
 */
public interface SubjectService extends Remote{

    // ------------------ CREATE / REGISTER SUBJECT ------------------
    Subject saveSubject(Subject subject) throws RemoteException;

    // ------------------ UPDATE SUBJECT ------------------
    Subject updateSubject(Subject subject) throws RemoteException;

    // ------------------ DELETE SUBJECT ------------------
    Subject deleteSubject(Subject subject) throws RemoteException;

    // ------------------ FIND SUBJECT BY ID ------------------
    Subject searchSubjectById(int subjectId) throws RemoteException;

    // ------------------ RETRIEVE ALL SUBJECTS ------------------
    List<Subject> retrieveAllSubjects() throws RemoteException;

}
