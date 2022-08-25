package cloud.ptl.indexer.api.mail;

import cloud.ptl.indexer.model.ItemEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Component
@Slf4j
public class MailService {
    private final Properties props;
    //    private final SpringMailConfig springMailConfig;
    @Value("${mail.password}")
    private String password;
    @Value("${mail.address}")
    private String address;
    @Value("${mail.alias}")
    private String alias;


    public MailService(@Value("${mail.smtp.auth}")
                       String mailSmtpAuth, @Value("${mail.smtp.starttls.enable}")
                       String mailSmtpStarttlsEnable, @Value("${mail.smtp.host}") Optional<String> mailSmtpHost,
                       @Value("${mail.smtp.default-host}") String mailSmtpDefaultHost,
                       @Value("${mail.smtp.port}")
                       String mailSmtpPort) {
        props = new Properties();
        props.put("mail.smtp.auth", mailSmtpAuth);
        props.put("mail.smtp.starttls.enable", mailSmtpStarttlsEnable);
        if (mailSmtpHost.isPresent()) {
            props.put("mail.smtp.host", mailSmtpHost.get());
        } else {
            props.put("mail.smtp.host", mailSmtpDefaultHost);
        }
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

        items.forEach(item -> {
            messageContent[0] = messageContent[0] + "\n" + item.stringRepresentationWithDueDate();
        });

        msg.setFrom(new InternetAddress(address, alias, "UTF-8"));
        msg.setSubject(title);
        msg.setContent(messageContent[0], "text/html");
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("krzk426@gmail.com"));
        msg.setSentDate(new Date());

        Transport.send(msg);
    }
}