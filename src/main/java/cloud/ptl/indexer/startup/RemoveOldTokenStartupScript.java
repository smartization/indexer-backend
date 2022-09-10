package cloud.ptl.indexer.startup;

import cloud.ptl.indexer.api.firebase.FirebaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
public class RemoveOldTokenStartupScript implements ApplicationRunner {
    private final FirebaseService firebaseService;
    @Value("${firebase.token.remove.days}")
    private Duration days;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Removing old tokens");
        firebaseService.removeTokensOlderThan(days);
    }
}
