package email_sender_microservice.controller.CreateClient;


import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import email_sender_microservice.controller.email.AbstractConnection;
import email_sender_microservice.controller.email.EmailController;
import email_sender_microservice.model.Client;
import email_sender_microservice.model.Email;

import java.sql.SQLException;


public class ClientMaker extends AbstractConnection{
    public static void main(String[] args) {
        try {
            Dao<Client, String> clientDao =  DaoManager.createDao(getConnectionSource(), Client.class);
            Client newClient = new Client("PindurPandurok", "password", "http://cartoonbros.com/powerpuff-girls/powerpuff-girls-6/", "http://cartoonbros.com/powerpuff-girls/powerpuff-girls-6/", "blue");
            clientDao.create(newClient);
            logger.info("Client Successfully saved.");
            //TODO: Send email to the new Client about his/her APIKey
        } catch (SQLException e) {
            logger.error("Client creation failed. Check constructor parameters.");
            e.printStackTrace();
        }
    }

}
