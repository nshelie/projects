/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.NotificationDao;
import dao.PaymentDao;
import model.Notification;
import model.Payment;

import java.time.LocalDate;
import java.util.List;
/**
 *
 * @author User-104
 */
public class NotificationService {


    private PaymentDao paymentDAO;
    private NotificationDao notificationDao;

    public NotificationService() {
        this.paymentDAO = new PaymentDao();
        this.notificationDao = new NotificationDao();
    }

    /**
     * This method checks all payments and generates notifications for:
     * - Tenants whose next payment is due soon
     * - Overdue tenants
     */
    public void generatePaymentNotifications() {
        try {
            List<Payment> allPayments = paymentDAO.getAllPayments(); // fetch all payments

            for (Payment payment : allPayments) {
                LocalDate nextDue = payment.getNextDueDate();
                LocalDate today = LocalDate.now();

                // Skip null dates
                if (nextDue == null) continue;

                // Check for upcoming due (e.g., 3 days before)
                if (!nextDue.isBefore(today) && !nextDue.isAfter(today.plusDays(3))) {
                    String message = "Payment due soon for tenant ID: " + payment.getTenantId();
                    Notification n = new Notification(
                            payment.getTenantId(),
                            payment.getApartmentId(),
                            message,
                            "payment_due",
                            "unread",
                            nextDue
                    );
                    notificationDao.insertNotification(n);
                }

                // Check for overdue
                if (nextDue.isBefore(today)) {
                    String message = "Payment OVERDUE for tenant ID: " + payment.getTenantId();
                    Notification n = new Notification(
                            payment.getTenantId(),
                            payment.getApartmentId(),
                            message,
                            "overdue",
                            "unread",
                            nextDue
                    );
                    notificationDao.insertNotification(n);
                }
            }
        } catch (Exception ex) {
            System.out.println("Error generating payment notifications: " + ex.getMessage());
        }
    }

}
