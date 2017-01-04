package email_sender_microservice;


import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import email_sender_microservice.controller.EmailController;

import java.net.URISyntaxException;

import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.port;

public class EmailAPIService {
    private static final Logger logger = LoggerFactory.getLogger(EmailAPIService.class);

    private EmailController controller;

    public static void main(String[] args) {
        logger.debug("Starting " + EmailAPIService.class.getName() + "...");

        setup(args);

        EmailController controller = new EmailController();


        // --- MAPPINGS ---
        get("/api/status", controller::status);

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
