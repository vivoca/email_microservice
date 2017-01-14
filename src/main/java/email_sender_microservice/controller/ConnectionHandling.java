package email_sender_microservice.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class ConnectionHandling {

    private HashMap<String, String> connectionProperties = new HashMap<>();
    private InputStream inputStream;

    // get the email connection properties from connection/connection.properties config file
    public HashMap<String, String> getPropValues() {
        try {
            Properties prop = new Properties();
            String propFileName = "connection/connection.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            }

            // Database properties
            connectionProperties.put("url", prop.getProperty("url"));
            connectionProperties.put("database", prop.getProperty("database"));
            connectionProperties.put("user", prop.getProperty("user"));
            connectionProperties.put("password", prop.getProperty("password"));

            // EmailReceiver properties
            connectionProperties.put("port", prop.getProperty("port"));

            // Email properties
            connectionProperties.put("sender_email", prop.getProperty("sender_email"));
            connectionProperties.put("smtp_host", prop.getProperty("smtp_host"));
            connectionProperties.put("smtp_port", prop.getProperty("smtp_port"));
            connectionProperties.put("sender_password", prop.getProperty("sender_password"));

        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return connectionProperties;
    }
}
