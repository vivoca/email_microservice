package email_sender_microservice.controller.email;

import email_sender_microservice.controller.ConnectionHandling;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Properties;

public class EmailSender {
    static ConnectionHandling configReader = new ConnectionHandling();
    static HashMap EmailProperties = configReader.getPropValues();

    public static String SENDER_EMAIL = EmailProperties.get("sender_email").toString();
    public static String SENDER_PASSWORD = EmailProperties.get("sender_password").toString();
    public static String HOST = EmailProperties.get("host").toString();
    public static String SMTP_HOST = "smtp.gmail.com";
    public static String SMTP_PORT = "587";


    public static void send(String clientEmail, String userEmail, String subject, String messagetext) {

        try {
            System.out.println("TLSEmail Start");
            Properties props = new Properties();
            props.put("mail.smtp.host", SMTP_HOST); //SMTP Host
            props.put("mail.smtp.port", SMTP_PORT); //TLS Port
            props.put("mail.smtp.auth", "true"); //enable authentication
            props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

            //create Authenticator object to pass in Session.getInstance argument
            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
                }
            };
            Session session = Session.getInstance(props, auth);

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
            message.setReplyTo(new Address[]{new InternetAddress(clientEmail)});

            System.out.println("Mail Check 2");

            message.setSubject(subject);
            message.setContent(messagetext, "text/html");

            System.out.println("Mail Check 3");

            Transport.send(message);
            System.out.println("Mail Sent");
        } catch (Exception ex) {
            System.out.println("Mail fail");
            System.out.println(ex);
        }
    }
}
