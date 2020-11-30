//package com.sparta.eng68.traineetracker.utilities;
//
//import java.util.*;
//import javax.mail.*;
//import javax.mail.internet.*;
//import javax.activation.*;
//
//public class EmailSender {
//    String from;
//    String to;
//    String host;
//    Properties properties;
//    Session session;
//
//    public EmailSender() {
//        this.from = "CMalone@spartaglobal.com";
//        this.host="localhost";
//        this.properties = System.getProperties();
//        this.session = Session.getDefaultInstance(properties);
//    }
//
//    public void email(String email, String password) {
//        this.to=email;
//        try{
//            // Create a default MimeMessage object.
//            MimeMessage message = new MimeMessage(session);
//
//            // Set From: header field of the header.
//            message.setFrom(new InternetAddress(from));
//
//            // Set To: header field of the header.
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
//
//            // Set Subject: header field
//            message.setSubject("Your reset password");
//
//            // Now set the actual message
//            message.setText("New Password:" + password);
//
//            // Send message
//            Transport.send(message);
//            System.out.println("Sent message successfully....");
//        } catch (MessagingException mex) {
//            mex.printStackTrace();
//        }
//    }
//}
