package cloud.ptl.indexer.api.mail;

import cloud.ptl.indexer.api.item.ItemDTO;
import cloud.ptl.indexer.api.item.ItemService;
import cloud.ptl.indexer.model.ItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
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
    final String DAYS = "days";
    final int DAYS_NUM  = 5;

    @GetMapping("/expired-products")
    public List<ItemDTO> sendEmailWithExpiredProducts() throws Exception {
        List<ItemEntity> entities = itemService.getAllExpiredProducts();
        sendEmail(entities, EXPIRED_MESSAGE);
        return entities.stream().map(ItemDTO::of).toList();
    }

    @GetMapping("/soon-expired-products/{daysNum}")
    public List<ItemDTO> sendEmailWithExpiredProducts(@PathVariable("daysNum") int daysNum) throws Exception {
        List<ItemEntity> entities = itemService.getAllSoonExpiredProducts(daysNum);
        sendEmail(entities,SOON_EXPIRED_MESSAGE);
        return entities.stream().map(ItemDTO::of).toList();
    }


    @Scheduled(cron = "0 30 3 * * *")
    void sendEmailWithAllExpiredProducts() throws Exception {
        List<ItemEntity> entities = itemService.getAllExpiredProducts();
        sendEmail(entities, EXPIRED_MESSAGE);
    }

    @Scheduled(cron = "0 30 3 * * *")
    void sendEmailWithAllSoonExpiredProducts() throws Exception {
        List<ItemEntity> expiredItems = itemService.getAllSoonExpiredProducts(DAYS_NUM);
        sendEmail(expiredItems, SOON_EXPIRED_MESSAGE + " " + DAYS_NUM + " " + DAYS);
    }

    private void sendEmail(List<ItemEntity> entities,String mailMessage) throws Exception {
        if(!entities.isEmpty()){
            mailService.sendmail(entities, mailMessage);
        }
    }
}
