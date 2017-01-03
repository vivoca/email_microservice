package email_sender_microservice.dao;


import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import email_sender_microservice.ConnectionPropertyValues;


import java.sql.SQLException;
import java.util.HashMap;

public abstract class AbstractDBHandler {
    private static ConnectionPropertyValues configReader = new ConnectionPropertyValues();
    protected static HashMap DBprops = configReader.getPropValuesOfEmail();

    private static final String DATABASE = "jdbc:postgresql://" + DBprops.get("url") + "/" + DBprops.get("database");
    private static final String DB_USER = String.valueOf(DBprops.get("user"));
    private static final String DB_PASSWORD = String.valueOf(DBprops.get("password"));
    private static ConnectionSource connection = null;

    public static ConnectionSource getConnection() throws SQLException {
        if (connection == null) {
            connection = new JdbcConnectionSource(DATABASE, DB_USER, DB_PASSWORD);
        }
        return connection;
    }

}
