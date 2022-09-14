package cloud.ptl.indexer.api.mail;

import cloud.ptl.indexer.api.item.ItemService;
import cloud.ptl.indexer.model.ItemEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.context.MessageSource;
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
    private final ItemService itemService;
    private final MessageSource messageSource;
    @Value("${notification.days-num}")
    private int DAYS_NUM;

    final String SOON_EXPIRED_MESSAGE = "indexer - list of products which will be expired in less then";
    final String EXPIRED_MESSAGE = "indexer - list of expired products";

    final String DAYS = "days";

    public MailService(@Value("${mail.smtp.auth}")
                       String mailSmtpAuth, @Value("${mail.smtp.starttls.enable}")
                       String mailSmtpStarttlsEnable, @Value("${mail.smtp.host}") Optional<String> mailSmtpHost,
                       @Value("${mail.smtp.default-host}") String mailSmtpDefaultHost,
                       @Value("${mail.smtp.port}")
                       String mailSmtpPort, TemplateEngine templateEngine, ItemService itemService, MessageSource messageSource) {
        this.templateEngine = templateEngine;
        this.itemService = itemService;
        this.messageSource = messageSource;
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

    public void sendEmail(List<ItemEntity> items, String title, String receiver) throws Exception {
        if (address.isEmpty()) {
            throw new Exception(messageSource.getMessage("error.mail.addressNotConfigured", null, Locale.getDefault()));
        }
        if (password.isEmpty()) {
            throw new Exception(messageSource.getMessage("error.mail.passwordNotConfigured", null, Locale.getDefault()));
        }
        final String[] messageContent = {""};
        Message msg = new MimeMessage(createSession());

        items.forEach(item -> messageContent[0] = messageContent[0] + "\n" + item.stringRepresentationWithDueDate());

        msg.setFrom(new InternetAddress(address, alias, "UTF-8"));
        msg.setSubject(title);
        msg.setContent(messageContent[0], "text/html");
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
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
    @Scheduled(cron = "0 30 3 * * *")
    void sendEmailWithAllExpiredProducts() throws Exception {
        List<ItemEntity> entities = itemService.getAllExpiredProducts();
        tryToSendEmail(entities, messageSource.getMessage("mail.expiredProductsMessage",null, Locale.getDefault()));
    }

    @Scheduled(cron = "0 30 3 * * *")
    void sendEmailWithAllSoonExpiredProducts() throws Exception {
        List<ItemEntity> expiredItems = itemService.getAllSoonExpiredProducts(DAYS_NUM);
        tryToSendEmail(expiredItems, messageSource.getMessage("mail.soonExpiredProductsMessage",new Object[] {DAYS_NUM}, Locale.getDefault()));
    }
    public void tryToSendEmail(List<ItemEntity> entities,String mailMessage) throws Exception {
        if(!entities.isEmpty()){
            sendEmail(entities, mailMessage, "krzk426@gmail.com");
            sendEmail(entities, mailMessage, "piotr@ptl.cloud");
        }
    }
}