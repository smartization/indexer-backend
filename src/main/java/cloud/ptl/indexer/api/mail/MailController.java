package cloud.ptl.indexer.api.mail;

import cloud.ptl.indexer.api.notification.MediaEnum;
import cloud.ptl.indexer.api.notification.TemplateEnum;
import cloud.ptl.indexer.api.notification.NotificationMediator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping(value = "/mail")
@RequiredArgsConstructor
public class MailController {
    private final NotificationMediator notificationMediator;

    @GetMapping("/notify/expired-products")
    public void sendEmailWithExpiredProducts() throws Exception {
            notificationMediator.sendNotification(TemplateEnum.EXPIRED_PRODUCTS, MediaEnum.MAIL);
    }

    @GetMapping("/notify/soon-expired-products/{daysNum}")
    public void sendEmailWithExpiredProducts(@PathVariable("daysNum") int daysNum) throws Exception {
            HashMap<Object, Object> params = new HashMap<>();
            params.put("daysNum", daysNum);
            notificationMediator.sendNotification(TemplateEnum.SOON_EXPIRED_PRODUCTS, MediaEnum.MAIL, params);
    }

    @GetMapping("/notify/low-quantity-products")
    public void sendEmailWithLowQuantityProducts() throws Exception {
            notificationMediator.sendNotification(TemplateEnum.QUANTITY, MediaEnum.MAIL);
    }
}
