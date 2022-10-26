package cloud.ptl.indexer.api.mail.template;

import cloud.ptl.indexer.api.item.ItemService;
import cloud.ptl.indexer.model.ItemEntity;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Component
public class MailSoonExpiredProducts extends MailTemplate {

    public MailSoonExpiredProducts(ItemService itemService,
                                   TemplateEngine templateEngine, MessageSource messageSource) {
        super(itemService, templateEngine, messageSource);
    }

    @Override
    String chooseTemplate() {
        return "mail/products-expiration-soon.html";
    }

    @Override
    String chooseTitle(HashMap params) {
        if(params.containsKey("daysNum")) {
            return messageSource.getMessage("mail.soonExpiredProductsMessage",
                    new Object[]{params.get("daysNum")},
                    Locale.getDefault());
        }
        return null;
    }

    @Override
    String chooseSubject() {
        return messageSource.getMessage("mail.soonExpiredProductsSubject",
                null,
                Locale.getDefault());
    }

    @Override
    List<HashMap> chooseItems(HashMap params) {
        if(params.containsKey("daysNum")) {

            List<ItemEntity> entities = itemService.getAllSoonExpiredProducts((Integer) params.get("daysNum"));
            return entities.stream().map(entity -> {
                HashMap elem = new HashMap();
                elem.put("name", entity.getName());
                elem.put("days", Period.between(entity.getDueDate(), LocalDate.now()).getDays());
                return elem;
            }).toList();
        }
        return null;
    }
}
