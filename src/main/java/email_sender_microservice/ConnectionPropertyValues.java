package email_sender_microservice;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class ConnectionPropertyValues {

    private HashMap<String, String> connectionProperties = new HashMap<>();
    private InputStream inputStream;

    // get the email connection properties from connection/connection.properties config file
    public HashMap<String, String> getPropValuesOfEmail() {
        try {
            Properties prop = new Properties();
            String propFileName = "connection/connection.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            }
            connectionProperties.put("sender_email", prop.getProperty("sender_email"));
            connectionProperties.put("host", prop.getProperty("host"));
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
