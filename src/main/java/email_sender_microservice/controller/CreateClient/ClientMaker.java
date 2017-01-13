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
            Client newClient = new Client("PindurPandurok", "https://cdn0.vox-cdn.com/thumbor/D511aBtg8uxYIH7wpGANzTcWw-M=/800x0/filters:no_upscale()/cdn0.vox-cdn.com/uploads/chorus_asset/file/6333691/powerpuff-girls-key-art-small.0.jpg", "https://cdn0.vox-cdn.com/thumbor/D511aBtg8uxYIH7wpGANzTcWw-M=/800x0/filters:no_upscale()/cdn0.vox-cdn.com/uploads/chorus_asset/file/6333691/powerpuff-girls-key-art-small.0.jpg", "DarkOrange");
            clientDao.create(newClient);
            logger.info("Client Successfully saved.");
        } catch (SQLException e) {
            logger.error("Client creation failed. Check constructor parameters.");
            e.printStackTrace();
        }
    }

}
