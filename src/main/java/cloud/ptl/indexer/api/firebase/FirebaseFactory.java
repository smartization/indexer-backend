package cloud.ptl.indexer.api.firebase;

import cloud.ptl.indexer.api.firebase.FirebaseService;
import cloud.ptl.indexer.api.firebase.template.FirebaseContent;
import cloud.ptl.indexer.api.firebase.template.FirebaseExpiredProducts;
import cloud.ptl.indexer.api.firebase.template.FirebaseProductsWithLowQuantity;
import cloud.ptl.indexer.api.firebase.template.FirebaseSoonExpiredProducts;
import cloud.ptl.indexer.api.notification.TemplateEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
@Component
@Slf4j
@RequiredArgsConstructor
public class FirebaseFactory {
    private final FirebaseService firebaseService;
    private final FirebaseExpiredProducts firebaseExpiredProducts;
    private final FirebaseProductsWithLowQuantity firebaseProductsWithLowQuantity;
    private final FirebaseSoonExpiredProducts firebaseSoonExpiredProducts;
    public void getNotification(TemplateEnum templateEnum, HashMap params)  {
        FirebaseContent firebaseContent = chooseFirebaseContent(templateEnum, params);
        if(firebaseContent != null){
           firebaseService.sendNotificationToAll(firebaseContent.getTitle(), firebaseContent.getContent());
        }
    }

    private FirebaseContent chooseFirebaseContent(TemplateEnum templateEnum, HashMap params){
        switch (templateEnum){
            case QUANTITY -> {
                return firebaseProductsWithLowQuantity.getContent(params);
            }
            case EXPIRED_PRODUCTS -> {
                return firebaseExpiredProducts.getContent(params);
            }
            case SOON_EXPIRED_PRODUCTS -> {
                return firebaseSoonExpiredProducts.getContent(params);
            }
        }
        return null;
    }
}
