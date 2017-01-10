package email_sender_microservice.controller;


import com.j256.ormlite.dao.Dao;
import email_sender_microservice.EmailSender;
import email_sender_microservice.model.Client;
import email_sender_microservice.model.Email;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class EmailController {


    public String status(Request request, Response response) {
        return "ok";
    }

    public boolean createEmail(Request request, Response response, Dao<Email, String> emailDao) {
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

    public boolean register(Request request, Response response, Dao<Client, String> clientDao) {
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


    public void scheduleEmails(final String sender, final String receiver, final String subject, final String message){
        final EmailSender email1 = new EmailSender();
        Timer t = new Timer();
        t.scheduleAtFixedRate(
                new TimerTask(){
                    public void run() {
                        email1.send(sender, receiver, subject, message);
                    }
                },
                0,
                10000);
    }
}
