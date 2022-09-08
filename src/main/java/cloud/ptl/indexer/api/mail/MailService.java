package cloud.ptl.indexer.api.mail;

import cloud.ptl.indexer.model.ItemEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

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
    private final TemplateEngine templateEngine;

    public MailService(@Value("${mail.smtp.auth}")
                       String mailSmtpAuth, @Value("${mail.smtp.starttls.enable}")
                       String mailSmtpStarttlsEnable, @Value("${mail.smtp.host}") Optional<String> mailSmtpHost,
                       @Value("${mail.smtp.default-host}") String mailSmtpDefaultHost,
                       @Value("${mail.smtp.port}")
                       String mailSmtpPort, TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
        props = new Properties();
        props.put("mail.smtp.auth", mailSmtpAuth);
        props.put("mail.smtp.starttls.enable", mailSmtpStarttlsEnable);
        props.put("mail.smtp.host", mailSmtpHost.orElse(mailSmtpDefaultHost));
        props.put("mail.smtp.port", mailSmtpPort);
    }

    private Session createSession() {
        return Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(address, password);
            }
        });
    }

    public void sendmail(List<ItemEntity> items, String title) throws Exception {
        if (address.isEmpty()) {
            throw new Exception("mail address not found in configuration");
        }
        if (password.isEmpty()) {
            throw new Exception("mail password not found in configuration");
        }
        final String[] messageContent = {""};
        Message msg = new MimeMessage(createSession());

        items.forEach(item -> messageContent[0] = messageContent[0] + "\n" + item.stringRepresentationWithDueDate());

        msg.setFrom(new InternetAddress(address, alias, "UTF-8"));
        msg.setSubject(title);
        msg.setContent(messageContent[0], "text/html");
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("krzk426@gmail.com"));
        msg.setSentDate(new Date());

        addBody(msg, items.stream().map(ItemEntity::stringRepresentationWithDueDate).toList(), title);

        Transport.send(msg);
    }

    private void addBody(Message msg, List<String> items, String title) throws MessagingException {
        final Context ctx = new Context(Locale.getDefault());
        ctx.setVariable("items", items);
        ctx.setVariable("title", title);

        final String htmlContent = templateEngine.process("mail/products-expiration.html", ctx);
        msg.setContent(htmlContent, MimeTypeUtils.TEXT_HTML.toString());
    }
}