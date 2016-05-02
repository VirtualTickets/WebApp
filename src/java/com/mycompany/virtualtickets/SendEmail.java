/*
 * Created by Nicholas Greer on 2016.04.13  * 
 * Copyright © 2016 Nicholas Greer. All rights reserved. * 
 */
package com.mycompany.virtualtickets;

// File Name SendEmail.java
import com.mycompany.managers.PasswordResetManager;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class SendEmail {

    public static void email() {
        // Recipient's email ID needs to be mentioned.
        String to = "abcd@gmail.com";

        // Sender's email ID needs to be mentioned
        String from = "web@gmail.com";

        // Assuming you are sending email from localhost
        String host = "localhost";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("This is the Subject Line!");

            // Now set the actual message
            message.setText("This is actual message");

            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public static boolean sendEmail(String emailTo, String username, String password, String subject, String body) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props,
                    new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
            session = Session.getDefaultInstance(props, null);
            session.setDebug(true);

            Message message1 = new MimeMessage(session);
            message1.setFrom(new InternetAddress("virtualtickets.noreply@gmail.com"));
            message1.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(emailTo));
            message1.setSubject(subject);
            message1.setText(body);

            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", "virtualtickets.noreply@gmail.com", "csd@VT(S16)");
            transport.sendMessage(message1, message1.getAllRecipients());
            transport.close();

            System.out.println("Done");

        } catch (MessagingException e) {
            Logger.getLogger(PasswordResetManager.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
        
        return true;
    }
}
