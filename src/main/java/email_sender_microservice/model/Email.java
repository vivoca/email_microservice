package email_sender_microservice.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

    @DatabaseTable(tableName = "email")
    public class Email {

        @DatabaseField(id = true)
        private String id;

        @DatabaseField
        private String status;

        @DatabaseField
        private String to;

        @DatabaseField
        private String from;

        @DatabaseField
        private String subject;

        @DatabaseField
        private String message;

        public Email() {
            // ORMLite needs a no-arg constructor
        }

        public Email(String id, String status, String to, String from, String subject, String message) {
            this.id = id;
            this.status = status;
            this.to = to;
            this.from = from;
            this.subject = subject;
            this.message = message;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
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

    }
