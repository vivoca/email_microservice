package email_sender_microservice;

import email_sender_microservice.server.EmailReceiverServer;
import email_sender_microservice.server.EmailSenderServer;

import java.sql.SQLException;


public class Main {
    /**
     * Run EmailReceiverServer.main() and  EmailSenderServer.main() method  to get the email's data and send it.
     * @param args
     */
    public static void main(String[] args) {

        // run EmailReceiverServer
        try {
            EmailReceiverServer.main();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // run EmailSenderServer
        EmailSenderServer.main();

    }
}
