package cloud.ptl.indexer.api.mail;

import cloud.ptl.indexer.api.mail.MailService;
import cloud.ptl.indexer.api.mail.template.MailContent;
import cloud.ptl.indexer.api.mail.template.MailExpiredProducts;
import cloud.ptl.indexer.api.mail.template.MailProductsWithLowQuantity;
import cloud.ptl.indexer.api.mail.template.MailSoonExpiredProducts;
import cloud.ptl.indexer.api.notification.TemplateEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
@Service
@RequiredArgsConstructor
public class TemplateFactory {
    private final MailService mailService;
    private final MailProductsWithLowQuantity mailProductsWithLowQuantity;
    private final MailExpiredProducts mailExpiredProducts;
    private final MailSoonExpiredProducts mailSoonExpiredProducts;
    public void getNotification(TemplateEnum templateEnum, HashMap params) throws Exception {
        MailContent mailContent;
        mailContent = chooseHtmlContent(templateEnum, params);
        if(mailContent != null){
            mailService.sendEmailToAllReceivers(mailContent);
        }
    }
    private MailContent chooseHtmlContent(TemplateEnum templateEnum, HashMap params){
        switch (templateEnum){
            case QUANTITY -> {
                return mailProductsWithLowQuantity.getContent(params);
            }
            case EXPIRED_PRODUCTS -> {
                return mailExpiredProducts.getContent(params);
            }
            case SOON_EXPIRED_PRODUCTS -> {
                return mailSoonExpiredProducts.getContent(params);
            }
        }
        return null;
    }
}
