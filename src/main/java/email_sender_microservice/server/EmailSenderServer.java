package email_sender_microservice.server;

import email_sender_microservice.controller.email.EmailController;

import java.sql.SQLException;

public class EmailSenderServer {

    /**
     * <h1>It runs the scheduleEmails() method or catch the SQLException.</h1>
     * @author Vivi and Moni
     * @version final
     */
    public static void main() {

        EmailController emailController = new EmailController();
        new Thread(() -> {
            try {
                emailController.scheduleEmails();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();

    }
}
