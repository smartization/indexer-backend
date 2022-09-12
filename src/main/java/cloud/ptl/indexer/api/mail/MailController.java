package cloud.ptl.indexer.api.mail;

import cloud.ptl.indexer.api.item.ItemDTO;
import cloud.ptl.indexer.api.item.ItemService;
import cloud.ptl.indexer.model.ItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping(value = "/mail")
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;
    private final ItemService itemService;
    private final MessageSource messageSource;

    @GetMapping("/notify/expired-products")
    public List<ItemDTO> sendEmailWithExpiredProducts() throws Exception {
        List<ItemEntity> entities = itemService.getAllExpiredProducts();
        mailService.tryToSendEmail(entities, messageSource.getMessage("mail.expiredProductsMessage",null, Locale.getDefault()));
        return entities.stream().map(ItemDTO::of).toList();
    }

    @GetMapping("/notify/soon-expired-products/{daysNum}")
    public List<ItemDTO> sendEmailWithExpiredProducts(@PathVariable("daysNum") int daysNum) throws Exception {
        List<ItemEntity> entities = itemService.getAllSoonExpiredProducts(daysNum);
        mailService.tryToSendEmail(entities,messageSource.getMessage("mail.soonExpiredProductsMessage", new Object[] {daysNum}, Locale.getDefault()));
        return entities.stream().map(ItemDTO::of).toList();
    }
}
