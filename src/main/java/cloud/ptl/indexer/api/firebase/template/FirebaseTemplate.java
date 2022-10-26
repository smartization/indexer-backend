package cloud.ptl.indexer.api.firebase.template;

import cloud.ptl.indexer.api.item.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;

import java.util.HashMap;

@RequiredArgsConstructor
public abstract class FirebaseTemplate {
    protected final ItemService itemService;
    protected final MessageSource messageSource;

    public FirebaseContent getContent(HashMap params){
        String content = chooseItems(params);
        String title = chooseTitle(params);
        return new FirebaseContent(title, content);
    }

    abstract String chooseTitle(HashMap params);
    abstract String chooseItems(HashMap params);
}
