package email_sender_microservice.controller;


import spark.Request;
import spark.Response;

public class EmailAPIServiceController {

    public String status(Request request, Response response) {
        return "ok";
    }
}
