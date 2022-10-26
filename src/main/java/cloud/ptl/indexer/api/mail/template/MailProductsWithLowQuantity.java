package cloud.ptl.indexer.api.mail.template;

import cloud.ptl.indexer.api.item.ItemService;
import cloud.ptl.indexer.model.ItemEntity;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Component
public class MailProductsWithLowQuantity extends MailTemplate {

    public MailProductsWithLowQuantity(ItemService itemService,
                                       TemplateEngine templateEngine, MessageSource messageSource) {
        super(itemService, templateEngine, messageSource);
    }
    @Override
    String chooseTemplate() {
        return "mail/products-quantity.html";
    }

    @Override
    String chooseTitle(HashMap params) {
        return messageSource.getMessage("mail.lowQuantityProducts", null, Locale.getDefault());
    }
    @Override
    String chooseSubject() {
        return messageSource.getMessage("mail.lowQuantityProductsSubject",
                null,
                Locale.getDefault());
    }
    @Override
    List<HashMap> chooseItems(HashMap params) {
        List<ItemEntity> entities = itemService.getAllLowQuantityProducts();
        return entities.stream().map(entity -> {
            HashMap elem = new HashMap();
            elem.put("name", entity.getName());
            elem.put("quantity", entity.getQuantity());
            elem.put("notifyQuantity", entity.getNotifyQuantity());
            return elem;
        }).toList();
    }
}
