package cloud.ptl.indexer.api.firebase;

import cloud.ptl.indexer.model.FirebaseToken;
import cloud.ptl.indexer.repositories.FirebaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseService {
    private final FirebaseRepository firebaseRepository;

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
}
