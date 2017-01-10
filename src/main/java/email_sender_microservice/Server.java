package server;


import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import email_sender_microservice.ConnectionHandling;
import email_sender_microservice.controller.EmailController;
import email_sender_microservice.model.Client;
import email_sender_microservice.model.Email;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.HashMap;

import static spark.Spark.*;

public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private static ConnectionHandling configReader = new ConnectionHandling();
    protected static HashMap DBprops = configReader.getPropValues();

    private static final String DATABASE = "jdbc:postgresql://" + DBprops.get("url") + "/" + DBprops.get("database");
    private static final String DB_USER = String.valueOf(DBprops.get("user"));
    private static final String DB_PASSWORD = String.valueOf(DBprops.get("password"));




    public static void main(String[] args) throws SQLException {
        logger.debug("Starting " + Server.class.getName() + "...");
        ConnectionSource connectionSource = new JdbcConnectionSource(DATABASE, DB_USER, DB_PASSWORD);

        setup(args);
        TableUtils.createTableIfNotExists(connectionSource, Email.class);
        TableUtils.createTableIfNotExists(connectionSource, Client.class);

        Dao<Email, String> emailDao = DaoManager.createDao(connectionSource, Email.class);
        Dao<Client, String> clientDao = DaoManager.createDao(connectionSource, Client.class);

        EmailController controller = new EmailController();

        // --- MAPPINGS ---
        get("/api/status", controller::status);
        post("/api/create", (request, response) -> controller.createEmail(request, response, emailDao));
        post("/api/register", ((request, response) -> controller.register(request, response, clientDao)));

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
     * Setting up port
     * @param args - app args
     */
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
