package cloud.ptl.indexer.api.item;

import cloud.ptl.indexer.api.mail.MailService;
import cloud.ptl.indexer.model.ItemEntity;
import cloud.ptl.indexer.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.context.Context;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@RequiredArgsConstructor
@Component
@EnableScheduling
@Slf4j
public class ItemService {
    private final ItemRepository itemRepository;
    private final MailService mailService;
    private String stringRepresentationWithDueDate(ItemEntity item){
        String itemDescription = item.getName();
        if(item.getStoragePlace() != null){
            itemDescription += " stored in " + item.getStoragePlace();
        }
        itemDescription += " with due date: " + item.getDueDate();
        return itemDescription;
    }


    public ItemEntity createItem(ItemDTO itemDTO) {
        ItemEntity entity = itemDTO.toEntity();
        return itemRepository.save(entity);
    }

    public ItemEntity save(ItemEntity item) {
        return itemRepository.save(item);
    }

    public ItemEntity getItem(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
    }

    public List<ItemEntity> getAll() {
        return (List<ItemEntity>) itemRepository.findAll();
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }

    public List<ItemEntity> getAllExpiredProducts() throws Exception {
        List<ItemEntity> expiredItems = itemRepository.findByDueDateBefore(LocalDate.now());
        if(!expiredItems.isEmpty()){
            mailService.sendmail(expiredItems, "indexer - list of expired products");
        }
        return expiredItems;
    }
    @Scheduled(cron = "0 30 3 * * *")
    void getAllExpiredProductsSendMail() throws Exception {
        List<ItemEntity> expiredItems = itemRepository.findByDueDateBefore(LocalDate.now());
        if(!expiredItems.isEmpty()){
            mailService.sendmail(expiredItems, "indexer - list of expired products");
        }
    }


    public List<ItemEntity> getAllSoonExpiredProducts(int daysNum) throws Exception {
        List<ItemEntity> expiredItems = itemRepository.findByDueDateIsBetween(LocalDate.now().minusDays(daysNum), LocalDate.now());
        if(!expiredItems.isEmpty()){
            mailService.sendmail(expiredItems, "indexer - list of products which will be expired in less then " + daysNum + " days");
        }
        return expiredItems;
    }
}
