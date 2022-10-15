package cloud.ptl.indexer.api.mail;

import cloud.ptl.indexer.api.mail.template.MailContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import org.thymeleaf.TemplateEngine;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

@Component
@Slf4j
public class MailService {
    private final Properties props;
    @Value("${mail.password}")
    private String password;
    @Value("${mail.address}")
    private String address;
    @Value("${mail.alias}")
    private String alias;
    private final MessageSource messageSource;

    public MailService(@Value("${mail.smtp.auth}")
                       String mailSmtpAuth, @Value("${mail.smtp.starttls.enable}")
                       String mailSmtpStarttlsEnable, @Value("${mail.smtp.host}") Optional<String> mailSmtpHost,
                       @Value("${mail.smtp.default-host}") String mailSmtpDefaultHost,
                       @Value("${mail.smtp.port}")
                       String mailSmtpPort, MessageSource messageSource) {
        this.messageSource = messageSource;
        props = new Properties();
        props.put("mail.smtp.auth", mailSmtpAuth);
        props.put("mail.smtp.starttls.enable", mailSmtpStarttlsEnable);
        props.put("mail.smtp.host", mailSmtpHost.orElse(mailSmtpDefaultHost));
        props.put("mail.smtp.port", mailSmtpPort);
    }

    private Session createSession() {
        return Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(address, password);
            }
        });
    }

    public void sendEmail(MailContent mailContent, String receiver) throws Exception {
        if (address.isEmpty()) {
            throw new Exception(messageSource.getMessage("error.mail.addressNotConfigured", null, Locale.getDefault()));
        }
        if (password.isEmpty()) {
            throw new Exception(messageSource.getMessage("error.mail.passwordNotConfigured", null, Locale.getDefault()));
        }
        final String[] messageContent = {""};
        Message msg = new MimeMessage(createSession());

        msg.setFrom(new InternetAddress(address, alias, "UTF-8"));
        msg.setContent(messageContent[0], "text/html");
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
        msg.setSentDate(new Date());
        msg.setSubject(mailContent.getSubject());

        msg.setContent(mailContent.getHtmlContent(), MimeTypeUtils.TEXT_HTML.toString());

        Transport.send(msg);
    }

    public void sendEmailToAllReceivers(MailContent mailContent) throws Exception {
        sendEmail(mailContent, "krzk426@gmail.com");
        sendEmail(mailContent, "piotr@ptl.cloud");
    }
}