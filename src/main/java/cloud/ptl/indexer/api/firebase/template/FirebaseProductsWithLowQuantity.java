package cloud.ptl.indexer.api.firebase.template;

import cloud.ptl.indexer.api.item.ItemService;
import cloud.ptl.indexer.model.ItemEntity;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class FirebaseProductsWithLowQuantity extends FirebaseTemplate{
    public FirebaseProductsWithLowQuantity(ItemService itemService, MessageSource messageSource) {
        super(itemService, messageSource);
    }

    @Override
    String chooseTitle(HashMap params) {
        return messageSource.getMessage("mail.lowQuantityProductsSubject", null, Locale.getDefault());
    }

    @Override
    String chooseItems(HashMap params) {
      List<ItemEntity> items = itemService.getAllLowQuantityProducts();
        return items
                .stream()
                .map(item -> messageSource
                        .getMessage("firebase.notification.productLowQuantityRepresentation",
                                new Object[]{item.getName(), item.getQuantity()},
                                Locale.getDefault()))
                .collect(Collectors.joining("\n"));
    }
}
