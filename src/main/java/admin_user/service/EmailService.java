package admin_user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String token) {
    	System.out.println("message checking in mail service");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the link below within 10 minutes:\n"
                + "http://52.52.68.232:8080/reset-password?token=" + token);
        mailSender.send(message);
    }
    
    public void sendPasswordChangeEmail(String to, String token) {
    	System.out.println("message checking in mail service");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the link below within 10 minutes:\n"
                + "http://52.52.68.232:8080/reset-password?token=" + token);
        mailSender.send(message);
    }
    
    
}
