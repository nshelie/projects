/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Random;

/**
 *
 * @author elie
 */
public class OTPProducer {


    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String QUEUE_NAME = "otpQueue";

    public static String sendOTP(String recipient) {
        String otp = generateOTP();

        try {
            // 1️⃣ Create ConnectionFactory
            ConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);

            // 2️⃣ Create Connection
            Connection connection = factory.createConnection();
            connection.start();

            // 3️⃣ Create Session
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // 4️⃣ Create Queue
            Queue queue = session.createQueue(QUEUE_NAME);

            // 5️⃣ Create MessageProducer
            MessageProducer producer = session.createProducer(queue);

            // 6️⃣ Create TextMessage
            TextMessage message = session.createTextMessage("OTP for " + recipient + ": " + otp);

            // 7️⃣ Send message
            producer.send(message);

            System.out.println("OTP sent to queue: " + otp);

            // 8️⃣ Close connection
            producer.close();
            session.close();
            connection.close();

        } catch (JMSException e) {
            e.printStackTrace();
        }

        return otp;
    }

    private static String generateOTP() {
        Random rand = new Random();
        int number = 100000 + rand.nextInt(900000); // 6-digit OTP
        return String.valueOf(number);
    }

    // Test main
    public static void main(String[] args) {
        sendOTP("elie@example.com");
    }

}
