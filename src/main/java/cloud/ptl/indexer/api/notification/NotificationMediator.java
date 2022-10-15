package cloud.ptl.indexer.api.notification;

import cloud.ptl.indexer.api.firebase.FirebaseFactory;
import cloud.ptl.indexer.api.mail.TemplateFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationMediator {
    private final TemplateFactory templateFactory;
    private final FirebaseFactory firebaseFactory;
    public void sendNotification(TemplateEnum templateEnum, MediaEnum mediaEnum) throws Exception {
        switch(mediaEnum){
            case MAIL -> templateFactory.getNotification(templateEnum, new HashMap<>());
            case FIREBASE -> firebaseFactory.getNotification(templateEnum, new HashMap<>());
        }

    }
    public void sendNotification(TemplateEnum templateEnum, MediaEnum mediaEnum, HashMap params) throws Exception {
        switch(mediaEnum){
            case MAIL -> templateFactory.getNotification(templateEnum, params);
            case FIREBASE -> firebaseFactory.getNotification(templateEnum, params);
        }

    }
}
