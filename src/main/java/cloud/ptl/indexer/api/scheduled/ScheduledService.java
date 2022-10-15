package cloud.ptl.indexer.api.scheduled;

import cloud.ptl.indexer.api.notification.MediaEnum;
import cloud.ptl.indexer.api.notification.TemplateEnum;
import cloud.ptl.indexer.api.notification.NotificationMediator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class ScheduledService {
    private final NotificationMediator notificationMediator;
    @Value("${notification.days-num}")
    private int daysNum;

    @Scheduled(cron = "${notification.time}")
    void sendEmailWithAllExpiredProducts() throws Exception {
        notificationMediator.sendNotification(TemplateEnum.EXPIRED_PRODUCTS, MediaEnum.MAIL);
    }
    @Scheduled(cron = "${notification.time}")
    void sendEmailWithAllSoonExpiredProducts() throws Exception {
        notificationMediator.sendNotification(TemplateEnum.SOON_EXPIRED_PRODUCTS, MediaEnum.MAIL);
    }
    @Scheduled(cron = "${notification.time}")
    void sendEmailWithProductsWithLowQuantity() throws Exception {
        notificationMediator.sendNotification(TemplateEnum.QUANTITY, MediaEnum.MAIL);
    }
    @Scheduled(cron = "${notification.time}")
    void sendScheduledNotificationWithExpiredProducts() throws Exception {
        notificationMediator.sendNotification(TemplateEnum.EXPIRED_PRODUCTS, MediaEnum.FIREBASE);
    }
    @Scheduled(cron = "${notification.time}")
    void sendScheduledNotificationWithSoonExpiredProducts() throws Exception {
        HashMap<Object, Object> params = new HashMap<>();
        params.put("daysNum", daysNum);
        notificationMediator.sendNotification(TemplateEnum.SOON_EXPIRED_PRODUCTS, MediaEnum.FIREBASE, params);
    }
    @Scheduled(cron = "${notification.time}")
    public void sendEmailWithLowQuantityProducts() throws Exception {
        notificationMediator.sendNotification(TemplateEnum.QUANTITY, MediaEnum.FIREBASE);
    }
}
