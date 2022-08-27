package cloud.ptl.indexer.api.profile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileService  {
    private final Environment env;

    public void ifDevProfileRun(Runnable func) {
        String devProfile = "dev";
        if (Arrays.asList(env.getActiveProfiles()).contains(devProfile)) {
            func.run();
        }
    }
}
