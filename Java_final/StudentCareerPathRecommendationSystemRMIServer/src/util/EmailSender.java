/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import java.util.Properties;

/**
 *
 * @author elie
 */
public class EmailSender {


    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int SMTP_PORT = 587; // TLS port
    private static final String FROM_EMAIL = "elienshimyumuremyi72@gmail.com"; // sender Gmail
    private static final String PASSWORD = "rhcaoxdwvwirwuzr";    // Gmail App Password

    public static boolean sendOtpEmail(String toEmail, String otp) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Your OTP for Career Path System");
            message.setText("Hello! Your OTP is: " + otp + "\nIt is valid for 5 minutes.");

            Transport.send(message);
            System.out.println("[SUCCESS] OTP sent to " + toEmail);
            return true;
        } catch (AuthenticationFailedException e) {
            System.err.println("[ERROR] Authentication failed! Check your email and App Password.");
            e.printStackTrace();
            return false;
        } catch (SendFailedException e) {
            System.err.println("[ERROR] Failed to send email! Invalid recipient address: " + toEmail);
            e.printStackTrace();
            return false;
        } catch (MessagingException e) {
            System.err.println("[ERROR] Messaging exception occurred! Possible network or SMTP issue.");
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("[ERROR] Unexpected exception while sending OTP.");
            e.printStackTrace();
            return false;
        }
    }
  
}
