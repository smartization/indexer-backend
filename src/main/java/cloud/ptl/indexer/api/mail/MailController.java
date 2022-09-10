package cloud.ptl.indexer.api.mail;

import cloud.ptl.indexer.api.item.ItemDTO;
import cloud.ptl.indexer.api.item.ItemService;
import cloud.ptl.indexer.model.ItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/mail")
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;
    private final ItemService itemService;
    final String SOON_EXPIRED_MESSAGE = "indexer - list of products which will be expired in less then";
    final String EXPIRED_MESSAGE = "indexer - list of expired products";

    @GetMapping("/notify/expired-products")
    public List<ItemDTO> sendEmailWithExpiredProducts() throws Exception {
        List<ItemEntity> entities = itemService.getAllExpiredProducts();
        mailService.tryToSendEmail(entities, EXPIRED_MESSAGE);
        return entities.stream().map(ItemDTO::of).toList();
    }

    @GetMapping("/notify/soon-expired-products/{daysNum}")
    public List<ItemDTO> sendEmailWithExpiredProducts(@PathVariable("daysNum") int daysNum) throws Exception {
        List<ItemEntity> entities = itemService.getAllSoonExpiredProducts(daysNum);
        mailService.tryToSendEmail(entities,SOON_EXPIRED_MESSAGE);
        return entities.stream().map(ItemDTO::of).toList();
    }
}
