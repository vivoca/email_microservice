package email_sender_microservice.server;


import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.table.TableUtils;
import email_sender_microservice.controller.ConnectionHandling;
import email_sender_microservice.controller.email.AbstractConnection;
import email_sender_microservice.controller.email.EmailController;
import email_sender_microservice.model.Client;
import email_sender_microservice.model.Email;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashMap;

import static spark.Spark.*;

public class EmailReceiverServer {
    private static final Logger logger = LoggerFactory.getLogger(EmailReceiverServer.class);
    private static ConnectionHandling configReader = new ConnectionHandling();
    private static HashMap <String, String> properties = configReader.getPropValues();
    private static final int port = Integer.valueOf(properties.getOrDefault("port", "60000"));

    public static void main() throws SQLException {

        logger.debug("Starting " + EmailReceiverServer.class.getName() + "...");

        setup(port);
        TableUtils.createTableIfNotExists(AbstractConnection.getConnectionSource(), Email.class);
        TableUtils.createTableIfNotExists(AbstractConnection.getConnectionSource(), Client.class);


        EmailController controller = new EmailController();

        // --- MAPPINGS ---
        get("/api/status", controller::status);
        post("/api/create", controller::createEmail);
        post("/api/register", (controller::register));

        // --- EXCEPTION HANDLING ---
        exception(URISyntaxException.class, (exception, request, response) -> {
            response.status(500);
            response.body(String.format("URI building error, maybe wrong format? : %s", exception.getMessage()));
            logger.error("Error while processing request", exception);
        });

        exception(Exception.class, (exception, request, response) -> {
            response.status(500);
            response.body(String.format("Unexpected error occurred: %s", exception.getMessage()));
            logger.error("Error while processing request", exception);
        });
    }

    /**
     * <h1>Setting up port</h1>
     * <p>It catch the NumberFormatException if the form of port is not allowed.</p>
     * @author Csibi and David
     * @version final
     * @param port - app args
     *
     */
    private static void setup(int port){
        try {
            port(port);
        } catch (NumberFormatException e){
            logger.error("Invalid port given '{}', it should be number.", port);
            System.exit(-1);
        }
    }


}
