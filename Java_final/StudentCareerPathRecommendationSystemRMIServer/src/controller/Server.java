/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import dao.HibernateUtil;
import service.implementation.CareerRoleServiceImpl;
import service.implementation.CompanyServiceImpl;
import service.implementation.PerformanceServiceImpl;
import service.implementation.StudentServiceImpl;
import service.implementation.SubjectServiceImpl;
import service.implementation.UserServiceImpl;


/**
 *
 * @author elie
 */
public class Server {
    
        public static void main(String[] args) {
        try {
            // =============================
            // FORCE HIBERNATE TO INITIALIZE
            // =============================
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();
            s.close();
            System.out.println("Hibernate initialized! Tables should now be created.");
            
            // =============================
            // START RMI SERVER
            // =============================
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");

            Registry registry = LocateRegistry.createRegistry(3700);
            registry.rebind("user", new UserServiceImpl());
            registry.rebind("student", new StudentServiceImpl());
            registry.rebind("subject", new SubjectServiceImpl());
            registry.rebind("performance", new PerformanceServiceImpl());
            registry.rebind("careerRole", new CareerRoleServiceImpl());
            registry.rebind("company", new CompanyServiceImpl());

            System.out.println("Server is running on port 3700");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
