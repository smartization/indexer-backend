package cloud.ptl.indexer.api.firebase;

import cloud.ptl.indexer.model.FirebaseToken;
import cloud.ptl.indexer.repositories.FirebaseRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseService {
    private final FirebaseRepository firebaseRepository;
    private final FirebaseMessaging firebaseMessaging;

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


    public void sendNotificationMessageToAll(String title, String content) {
        if (!content.isEmpty()) {
            List<FirebaseToken> tokens = findAll();
            tokens.forEach(token -> sendNotificationMessage(title, content, token.getToken()));
        }
    }

    public void sendDataMessageToAll(Map<String, String> data) {
        List<FirebaseToken> tokens = findAll();
        tokens.forEach(token -> sendDataMessage(data, token.getToken()));
    }

    /**
     * Send notification to single device, notification is displayed in notification bar
     *
     * @param title   title of notification
     * @param content content of notification
     * @param token   token of device
     */
    public void sendNotificationMessage(String title, String content, String token) {
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

        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Send data message to single device, notification is not displayed in notification bar
     *
     * @param token token of device
     */
    public void sendDataMessage(Map<String, String> data, String token) {
        Message message = Message
                .builder()
                .setToken(token)
                .putAllData(data)
                .build();
        try {
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
