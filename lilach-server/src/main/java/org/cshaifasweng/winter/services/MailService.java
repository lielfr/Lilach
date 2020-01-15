package org.cshaifasweng.winter.services;

import org.cshaifasweng.winter.SpringServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class MailService {
    private JavaMailSender mailSender;
    private MailContentBuilder contentBuilder;

    @Value("${lilach.mail.from}")
    private String mailFrom;

    @Autowired
    public MailService(JavaMailSender mailSender, MailContentBuilder contentBuilder) {
        this.mailSender = mailSender;
        this.contentBuilder = contentBuilder;
    }

    public void sendMail(String recipient, String subject, String message) {
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(mailFrom);
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(contentBuilder.build(subject, message), true);
            helper.addInline("lilach-logo",
                    new File(SpringServer.class.getResource("lilach-logo.png").getFile()));
        };
        mailSender.send(preparator);
    }
}
