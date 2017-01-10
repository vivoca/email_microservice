package email_sender_microservice.server;


import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.table.TableUtils;
import email_sender_microservice.controller.email.AbstractConnection;
import email_sender_microservice.controller.email.EmailController;
import email_sender_microservice.model.Client;
import email_sender_microservice.model.Email;

import java.net.URISyntaxException;
import java.sql.SQLException;

import static spark.Spark.*;

public class EmailReceiverServer {
    private static final Logger logger = LoggerFactory.getLogger(EmailReceiverServer.class);

    public static void main(String[] args) throws SQLException {
        logger.debug("Starting " + EmailReceiverServer.class.getName() + "...");

        setup(args);
        TableUtils.createTableIfNotExists(AbstractConnection.getConnectionSource(), Email.class);
        TableUtils.createTableIfNotExists(AbstractConnection.getConnectionSource(), Client.class);

        Dao<Email, String> emailDao = DaoManager.createDao(AbstractConnection.getConnectionSource(), Email.class);
        Dao<Client, String> clientDao = DaoManager.createDao(AbstractConnection.getConnectionSource(), Client.class);

        EmailController controller = new EmailController();

        // --- MAPPINGS ---
        get("/api/status", controller::status);
        post("/api/create", (request, response) -> controller.createEmail(request, response));
        post("/api/register", ((request, response) -> controller.register(request, response)));

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

//    /**
//     * Setting up port
//     * @param args - app args
//     */
    private static void setup(String[] args){
        if(args == null || args.length == 0){
            logger.error("Port must be given as the first argument.");
            System.exit(-1);
        }

        try {
            port(Integer.parseInt(args[0]));
        } catch (NumberFormatException e){
            logger.error("Invalid port given '{}', it should be number.", args[0]);
            System.exit(-1);
        }
    }


}
