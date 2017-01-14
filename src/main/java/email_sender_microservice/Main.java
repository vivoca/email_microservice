package email_sender_microservice;

import email_sender_microservice.server.EmailReceiverServer;
import email_sender_microservice.server.EmailSenderServer;

import java.sql.SQLException;


public class Main {
    /**
     * <h1>Run EmailReceiverServer.main() and  EmailSenderServer.main() method  to get the email's data and send it.</h1>
     * @param args
     * @version final
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
