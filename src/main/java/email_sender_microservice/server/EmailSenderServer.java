package email_sender_microservice.server;

import email_sender_microservice.controller.email.EmailController;

import java.sql.SQLException;

public class EmailSenderServer {

    /**
     * It runs the scheduleEmails() method or catch the SQLException.
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
