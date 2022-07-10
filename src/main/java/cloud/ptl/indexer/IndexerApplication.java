package cloud.ptl.indexer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class IndexerApplication {

    public static void main(String[] args) {
        SpringApplication.run(IndexerApplication.class, args);
    }
}
