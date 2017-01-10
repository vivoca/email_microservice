package email_sender_microservice.controller.email;


import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import email_sender_microservice.model.Email;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.SQLException;
import java.util.*;

public class EmailController extends AbstractConnection{

    public String status(Request request, Response response) {
        return "ok";
    }

    public boolean createEmail(Request request, Response response) throws SQLException {
        Dao<Email, String> emailDao = DaoManager.createDao(getConnectionSource(), Email.class);

        JSONObject emailDetail = new JSONObject(request.body());
        ArrayList<String> keyList = new ArrayList<>(Arrays.asList("to", "from", "subject", "message"));
        for (String key : keyList) if (emailDetail.isNull(key)) return false;

        if (isValidEmailAddress(emailDetail.getString("to")) && isValidEmailAddress(emailDetail.getString("from"))) {
            Email email = new Email(emailDetail.getString("to"),
                                    emailDetail.getString("from"),
                                    emailDetail.getString("subject"),
                                    emailDetail.getString("message"));

            try {
                emailDao.create(email);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public boolean register(Request request, Response response) {
        return true;
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    public static void send(String clientEmail, String userEmail, String subject, String messagetext) throws SQLException {
        Dao<Email, String> emailDao = DaoManager.createDao(getConnectionSource(), Email.class);
        try {
            System.out.println("TLSEmail Start");
            Properties props = new Properties();
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

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

    public void scheduleEmails(final String sender, final String receiver, final String subject, final String message){
        Timer t = new Timer();
        t.scheduleAtFixedRate(
                new TimerTask(){
                    public void run() {
                        try {
                            send(sender, receiver, subject, message);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                },
                0,
                10000);
    }
}
