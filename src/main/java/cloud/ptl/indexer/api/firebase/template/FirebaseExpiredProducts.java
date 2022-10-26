package cloud.ptl.indexer.api.firebase.template;

import cloud.ptl.indexer.api.item.ItemService;
import cloud.ptl.indexer.model.ItemEntity;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class FirebaseExpiredProducts extends FirebaseTemplate{
    public FirebaseExpiredProducts(ItemService itemService, MessageSource messageSource) {
        super(itemService, messageSource);
    }

    @Override
    String chooseTitle(HashMap params) {
        return messageSource.getMessage("mail.expiredProductsSubject", null, Locale.getDefault());
    }

    @Override
   String chooseItems(HashMap params) {
        List<ItemEntity> items = itemService.getAllExpiredProducts();
        return items
                .stream()
                .map(item -> messageSource
                        .getMessage("firebase.notification.productsExpiredRepresentation",
                                new Object[]{item.getName(), Period.between(item.getDueDate(), LocalDate.now()).getDays()},
                                Locale.getDefault()))
                .collect(Collectors.joining("\n"));
    }
}
