package email_sender_microservice.controller;


import spark.Request;
import spark.Response;

public class EmailController {

    public String status(Request request, Response response) {
        return "ok";
    }

    public boolean createEmail(Request request, Response response) {
        return true;
    }
}
