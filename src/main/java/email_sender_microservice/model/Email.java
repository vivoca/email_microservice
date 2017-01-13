package email_sender_microservice.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import email_sender_microservice.model.enums.EmailStatus;

@DatabaseTable(tableName = "email")
    public class Email {

        @DatabaseField(generatedId = true)
        private Integer id;

        @DatabaseField
        private EmailStatus status;

        @DatabaseField
        private String to;

        @DatabaseField
        private String from;

        @DatabaseField
        private String subject;

        @DatabaseField
        private String message;

        @DatabaseField(canBeNull = false, foreign = true, columnName = "APIKey")
        private Client client;

        public Email() {
            // ORMLite needs a no-arg constructor
        }



        public Email(String to, String from, String subject, String message, Client client) {
            this.status = EmailStatus.NEW;
            this.to = to;
            this.from = from;
            this.subject = subject;
            this.message = message;
            this.client = client;

        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public EmailStatus getStatus() {
            return status;
        }

        public void setStatus(EmailStatus status) {
            this.status = status;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Client getClient() {
            return client;
        }
    }
