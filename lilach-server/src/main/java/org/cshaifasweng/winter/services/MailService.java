package org.cshaifasweng.winter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class MailService {
    private JavaMailSender mailSender;

    @Value("${lilach.mail.from}")
    private String mailFrom;

    @Autowired
    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMail(String recipient, String subject, String message) {
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setFrom(mailFrom);
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(message);
        };
        mailSender.send(preparator);
    }
}
