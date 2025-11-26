package com.sahan.adyapana.model;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {

    private static String subject;
    private static String text;

    public static void sendEmail(String recepient, String mailSubject, String mailTest) throws Exception {

        subject = mailSubject;
        text = mailTest;

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        String username = "adyapanainstitute2000@gmail.com";
        String password = "sdluxnwkzgujudvi";

        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = prepareMessage(session, username, recepient);

        Transport.send(message);

    }

    private static Message prepareMessage(Session session, String username, String recepient) throws Exception {
        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(username));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
        message.setSubject(subject);
        message.setText(text);

        return message;
    }
}
