package cloud.ptl.indexer.api.mail.template;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MailContent {
    private String htmlContent;
    private String subject;
}
