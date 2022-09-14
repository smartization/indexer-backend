package cloud.ptl.indexer.api.firebase;

import cloud.ptl.indexer.api.item.ItemService;
import cloud.ptl.indexer.model.FirebaseToken;
import cloud.ptl.indexer.model.ItemEntity;
import cloud.ptl.indexer.repositories.FirebaseRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseService {
    private final FirebaseRepository firebaseRepository;
    private final FirebaseMessaging firebaseMessaging;
    private final ItemService itemService;
    private final MessageSource messageSource;
    @Value("${notification.days-num}")
    private int DAYS_NUM;

    public void updateToken(String oldToken, String newToken) {
        firebaseRepository.findByToken(oldToken).ifPresentOrElse(firebaseToken -> {
            log.info("Updating token from {} to {}", oldToken, newToken);
            firebaseToken.setToken(newToken);
            firebaseRepository.save(firebaseToken);
        }, () -> {
            log.info("Creating new token {}", newToken);
            firebaseRepository.save(
                    FirebaseToken.builder().token(newToken).build()
            );
        });
    }

    public void removeTokensOlderThan(Duration days) {
        List<FirebaseToken> firebaseTokens = findAll();
        Timestamp borderDate = Timestamp.valueOf(LocalDate.now().minusDays(days.toDays()).atStartOfDay());
        firebaseTokens.forEach(firebaseToken -> {
            if (firebaseToken.getCreatedAt().before(borderDate)) {
                log.info("Removing token {}", firebaseToken.getToken());
                firebaseRepository.delete(firebaseToken);
            }
        });
        log.info("No old tokens remain");
    }

    public List<FirebaseToken> findAll() {
        return (List<FirebaseToken>) firebaseRepository.findAll();
    }


    public void sendNotificationToAll(String title, List<ItemEntity> items){
        if(!items.isEmpty()){
            List<FirebaseToken> tokens = findAll();
            tokens.forEach(token -> {
                try {
                    sendNotification(title, items, token.getToken());
                } catch (FirebaseMessagingException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
    public String sendNotification(String title, List<ItemEntity> items, String token) throws FirebaseMessagingException {

        String content = items
                .stream()
                .map(item -> messageSource
                        .getMessage("firebase.notification.productRepresentation",
                                new Object[]{item.getName(), item.getDueDate()},
                                Locale.getDefault()))
                .collect(Collectors.joining("\n"));

        Notification notification = Notification
                .builder()
                .setTitle(title)
                .setBody(content)
                .build();

        Message message = Message
                .builder()
                .setToken(token)
                .setNotification(notification)
                .build();

        return firebaseMessaging.send(message);
    }
    @Scheduled(cron = "0 30 3 * * *")
    void sendScheduledNotificationWithExpiredProducts(){
        List<ItemEntity> expiredItems = itemService.getAllExpiredProducts();
        sendNotificationToAll(messageSource.getMessage("firebase.notification.expiredProductsTitle", null, Locale.getDefault()), expiredItems);
    }
    @Scheduled(cron = "0 30 3 * * *")
    void sendScheduledNotificationWithSoonExpiredProducts(){
        List<ItemEntity> soonExpiredItems = itemService.getAllSoonExpiredProducts(DAYS_NUM);
        sendNotificationToAll(messageSource.getMessage("firebase.notification.soonExpiredProductsTitle", new Object[] {DAYS_NUM}, Locale.getDefault()), soonExpiredItems);
    }

    void sendNotificationWithExpiredProducts(){
        List<ItemEntity> expiredItems = itemService.getAllExpiredProducts();
        sendNotificationToAll(messageSource.getMessage("firebase.notification.expiredProductsTitle", null, Locale.getDefault()), expiredItems);
    }
    void sendNotificationWithSoonExpiredProducts(int daysNum){
        List<ItemEntity> soonExpiredItems = itemService.getAllSoonExpiredProducts(daysNum);
        sendNotificationToAll(messageSource.getMessage("firebase.notification.soonExpiredProductsTitle", new Object[] {daysNum}, Locale.getDefault()), soonExpiredItems);
    }
}
