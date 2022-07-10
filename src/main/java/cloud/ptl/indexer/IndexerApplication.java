package cloud.ptl.indexer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableJpaRepositories
@Slf4j
public class IndexerApplication {

    public static void main(String[] args) {
        SpringApplication.run(IndexerApplication.class, args);
    }

    @Configuration
    public static class WebConfig implements WebMvcConfigurer {
        @Override
        // by default accepting all basic content types for post and put send via Postman
        public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
            WebMvcConfigurer.super.configureContentNegotiation(configurer);
            configurer.defaultContentType(MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_FORM_URLENCODED,
                            MediaType.APPLICATION_JSON)
                    .parameterName("mediaType")
                    .ignoreAcceptHeader(true);
        }
    }
}
