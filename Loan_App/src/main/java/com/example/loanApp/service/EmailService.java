package com.example.loanApp.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;
    private static final String to = "";
    public void EmailSentService() throws MessagingException {
//        String emailContent = EmailTemplate.emailTemplate;
//        emailContent = emailContent.replace("{{playerName}}", playerName);
//        emailContent = emailContent.replace("{{communityName}}", communityName);
//        emailContent = emailContent.replace("{{winningAmount}}", String.valueOf(winningAmount));
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setFrom(fromEmail);
            messageHelper.setTo(to);
            messageHelper.setSubject("Congratulations on Winning!");
//            messageHelper.setText(emailContent, true);
            javaMailSender.send(mimeMessage);
            System.out.println();
    }
}
