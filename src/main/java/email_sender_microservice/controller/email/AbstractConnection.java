package email_sender_microservice.controller.email;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import email_sender_microservice.controller.ConnectionHandling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.HashMap;


public abstract class AbstractConnection {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractConnection.class);

    private static ConnectionHandling configReader = new ConnectionHandling();
    private static HashMap Properties = configReader.getPropValues();

    // DB Properties
    private static final String DATABASE = "jdbc:postgresql://" +
                                            Properties.get("url") +
                                            "/" +
                                            Properties.get("database");

    private static final String DB_USER = Properties.get("user").toString();
    private static final String DB_PASSWORD = Properties.get("password").toString();

    // Email Properties
     static final String SENDER_EMAIL = Properties.get("sender_email").toString();
     static final String SENDER_PASSWORD = Properties.get("sender_password").toString();
     static final String SMTP_HOST = Properties.get("smtp_host").toString();
     static final String SMTP_PORT = Properties.get("smtp_port").toString();

     private static ConnectionSource connectionSource  = null;


    public static ConnectionSource getConnectionSource() {
        logger.info("getConnection method is called.");

        if (connectionSource == null) {
            logger.info("create connection");
            try {
                connectionSource = new JdbcConnectionSource(DATABASE, DB_USER, DB_PASSWORD);
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
        return connectionSource;
    }




}
