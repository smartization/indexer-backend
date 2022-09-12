package cloud.ptl.indexer.api.messages;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class MessageConfiguration {
    @Bean
    public MessageSource messageSource() {
        var source = new ResourceBundleMessageSource();
        source.setBasenames("messages/messages");
        source.setUseCodeAsDefaultMessage(true);
        return source;
    }
}