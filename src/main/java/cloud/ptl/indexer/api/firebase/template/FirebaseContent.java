package cloud.ptl.indexer.api.firebase.template;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FirebaseContent {
    private String title;
    private String content;
}
