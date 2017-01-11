package email_sender_microservice.controller.CreateClient;


import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import email_sender_microservice.controller.email.AbstractConnection;
import email_sender_microservice.model.Client;

import java.sql.SQLException;


public class ClientMaker extends AbstractConnection{
    public static void main(String[] args) {
        try {
            Dao<Client, String> clientDao =  DaoManager.createDao(getConnectionSource(), Client.class);
            Client newClient = new Client("name", "password", "headerURL", "FooterURL", "textRGBColor");
            clientDao.create(newClient);
            logger.info("Client Successfully saved.");
        } catch (SQLException e) {
            logger.error("Client creation failed. Check constructor parameters.");
            e.printStackTrace();
        }
    }

}
