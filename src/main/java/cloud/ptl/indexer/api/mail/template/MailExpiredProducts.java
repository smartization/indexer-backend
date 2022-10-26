package cloud.ptl.indexer.api.mail.template;

import cloud.ptl.indexer.api.item.ItemService;
import cloud.ptl.indexer.model.ItemEntity;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Component
public class MailExpiredProducts extends MailTemplate {

    public MailExpiredProducts(ItemService itemService,
                               TemplateEngine templateEngine, MessageSource messageSource) {
        super(itemService, templateEngine, messageSource);
    }

    @Override
    String chooseTemplate() {
       return "mail/products-expiration.html";
    }

    @Override
    String chooseTitle(HashMap params) {
        return messageSource.getMessage("mail.expiredProductsMessage", null, Locale.getDefault());
    }
    @Override
    String chooseSubject() {
        return messageSource.getMessage("mail.expiredProductsSubject",
                null,
                Locale.getDefault());
    }
    @Override
    List<HashMap> chooseItems(HashMap params) {
        List<ItemEntity> entities = itemService.getAllExpiredProducts();
        return entities.stream().map(entity -> {
            HashMap elem = new HashMap();
            elem.put("name", entity.getName());
            elem.put("days", Period.between(entity.getDueDate(), LocalDate.now()).getDays());
            return elem;
        }).toList();
    }
}
