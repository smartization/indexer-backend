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
public class FirebaseSoonExpiredProducts extends FirebaseTemplate{
    public FirebaseSoonExpiredProducts(ItemService itemService, MessageSource messageSource) {
        super(itemService, messageSource);
    }

    @Override
    String chooseTitle(HashMap params) {
        return messageSource.getMessage("mail.soonExpiredProductsSubject", new Object[]{params.get("daysNum")}, Locale.getDefault());
    }

    @Override
    String chooseItems(HashMap params) {
        List<ItemEntity> items = itemService.getAllSoonExpiredProducts((Integer) params.get("daysNum"));
        return items
                .stream()
                .map(item -> messageSource
                        .getMessage("firebase.notification.productsSoonExpiredRepresentation",
                                new Object[]{item.getName(), Period.between(LocalDate.now(), item.getDueDate()).getDays()},
                                Locale.getDefault()))
                .collect(Collectors.joining("\n"));
    }
}
