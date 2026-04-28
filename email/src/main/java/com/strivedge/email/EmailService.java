package com.strivedge.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String fromEmail;
    public void winningAmountEmailSentService(Community community) throws MessagingException{
        String to = community.getPlayerEmail() != null ? community.getPlayerEmail() : community.getCommunityName().isEmpty() ? community.getPlayerEmail() : null;
        String playerName = community.getPlayerName();
        String communityName = community.getCommunityName();
        BigDecimal winningAmount = community.getWinningAmount();

        String emailContent = EmailTemplate.emailTemplate;
        emailContent = emailContent.replace("{{playerName}}", playerName);
        emailContent = emailContent.replace("{{communityName}}", communityName);
        emailContent = emailContent.replace("{{winningAmount}}", String.valueOf(winningAmount));
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
        messageHelper.setFrom(fromEmail);
        if(to != null){
            messageHelper.setTo(to);
            messageHelper.setSubject("Congratulations on Winning!");
            messageHelper.setText(emailContent, true);
            javaMailSender.send(mimeMessage);
            System.out.println();
        } else {
            throw new MessagingException("email not found");
        }

    }
}
