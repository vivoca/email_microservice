package email_sender_microservice;

import java.util.Timer;
import java.util.TimerTask;

public class ScheduledEmailController {

    public void scheduleEmails(final String sender, final String receiver, final String subject, final String message){
        final EmailSender email1 = new EmailSender();
        Timer t = new Timer();
        t.scheduleAtFixedRate(
            new TimerTask(){
                public void run() {
                    email1.send(sender, receiver, subject, message);
                }
            },
            0,
            10000);
    }

}
