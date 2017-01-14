package email_sender_microservice.controller.email;


import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import email_sender_microservice.model.Client;
import email_sender_microservice.model.Email;
import email_sender_microservice.model.enums.EmailStatus;
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
        Dao<Client, String> clientDao = DaoManager.createDao(getConnectionSource(), Client.class);


        JSONObject emailDetail = new JSONObject(request.body());
        ArrayList<String> keyList = new ArrayList<>(Arrays.asList("to", "from", "subject", "message"));
        for (String key : keyList) if (emailDetail.isNull(key)) return false;

        // This is how you write a query in ORMLite 'SELECT * FROM client WHERE APIKey == APIKey(from request body)'
        PreparedQuery<Client> prepClientQuery = clientDao.queryBuilder().where().eq("APIKey", emailDetail.getString("APIKey")).prepare();
        Client client = clientDao.queryForFirst(prepClientQuery);

        if (isValidEmailAddress(emailDetail.getString("to")) && isValidEmailAddress(emailDetail.getString("from"))) {
            Email email = new Email(emailDetail.getString("to"),
                                    emailDetail.getString("from"),
                                    emailDetail.getString("subject"),
                                    emailDetail.getString("message"),
                                    client);

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

    private static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

    private static boolean send(Email email)  {

        String messagetext = "<body style='text-align: justify; color: " +
                email.getClient().getTextColor() + "'><img src=" +
                email.getClient().getHeader() + " style='width: 100%'>" +
                email.getMessage() + "<img src=" +
                email.getClient().getFooter() +
                "></body>";
        try {
            logger.info("TLSEmail procedure started...");
            Properties props = new Properties();
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            logger.info("Authenticating...");
            //create Authenticator object to pass in Session.getInstance argument
            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
                }
            };
            Session session = Session.getInstance(props, auth);

            logger.info("Authentication done.");

            logger.info("Setting email properties...");

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email.getFrom()));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email.getTo()));
            message.setReplyTo(new Address[]{new InternetAddress(email.getFrom())});


            message.setSubject(email.getSubject());
            message.setContent(messagetext, "text/html");

            logger.info("Email properties set.");

            logger.info("Sending email...");

            Transport.send(message);
            logger.info("Email successfully sent. Client: " + email.getClient().getName() + ".");
            return true;
        } catch (Exception ex) {
            logger.error("Failed to send email.");
            return false;
        }
    }

    public void scheduleEmails() throws SQLException {
        Dao<Email, String> emailDao = DaoManager.createDao(getConnectionSource(), Email.class);
        Dao<Client, String> clientDao = DaoManager.createDao(getConnectionSource(), Client.class);
        Timer t = new Timer();
        t.scheduleAtFixedRate(
                new TimerTask(){
                    public void run() {
                        try {
                            List<Email> emails = emailDao.queryBuilder().where().eq("status", EmailStatus.NEW).query();
                            for (Email email : emails) {
                                clientDao.refresh(email.getClient());
                                   if(send(email)){
                                       email.setStatus(EmailStatus.SENT);
                                       emailDao.update(email);
                                   }

                            }
                        }catch (SQLException e) {e.printStackTrace();}
                    }
                },
                0,
                10000);
    }
}
