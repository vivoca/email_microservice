package email_sender_microservice.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

//Todo: create model

@DatabaseTable(tableName = "accounts")
public class Client {

    @DatabaseField(id = true)
    private String name;
    @DatabaseField
    private String password;

    public Client() {

        // ORMLite needs a no-arg constructor

    }

    public Client(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

