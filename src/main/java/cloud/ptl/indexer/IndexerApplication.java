package cloud.ptl.indexer;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
@OpenAPIDefinition(
        info = @Info(
                title = "Smartization indexer API",
                version = "1.0.0",
                description = "API serving infromation for indexer frontend"
        )
)
public class IndexerApplication {

    public static void main(String[] args) {
        SpringApplication.run(IndexerApplication.class, args);
    }
}
