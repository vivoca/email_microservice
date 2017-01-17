package email_sender_microservice.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@DatabaseTable(tableName = "client")
public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    @DatabaseField(id = true)
    private String APIKey;
    @DatabaseField
    private String name;
    @DatabaseField
    private String header;
    @DatabaseField
    private String footer;
    @DatabaseField
    private String textRGB;

    public Client() {

        // ORMLite needs a no-arg constructor

    }

    public Client(String name, String header, String footer, String textRGB) {
        logger.info("creating new client");
        this.name = name;
        this.APIKey = generateAPIKey();
        this.header = header;
        this.footer = footer;
        this.textRGB = textRGB;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAPIKey() {
        return APIKey;
    }

    public void setAPIKey(String APIKey) {
        this.APIKey = APIKey;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getTextColor() {
        return textRGB;
    }

    public void setTextColor(String textColor) {
        this.textRGB = textColor;
    }

    /**
     * <h1>It generate a UUID for the new client (as an APIKey)</h1>
     * @return client's uuid
     */
    public String generateAPIKey() {
        final String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        logger.info("generated uuid: " + uuid);
        return uuid;
    }
}

