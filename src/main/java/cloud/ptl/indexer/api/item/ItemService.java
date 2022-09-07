package cloud.ptl.indexer.api.item;

import cloud.ptl.indexer.api.mail.MailService;
import cloud.ptl.indexer.model.ItemEntity;
import cloud.ptl.indexer.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Component
@EnableScheduling
@Slf4j
public class ItemService {
    private final ItemRepository itemRepository;
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

    public List<ItemEntity> getAllExpiredProducts() {
        return itemRepository.findByDueDateBefore(LocalDate.now());
    }

    public List<ItemEntity> getAllSoonExpiredProducts(int daysNum) {
        return itemRepository.findByDueDateIsBetween(LocalDate.now().minusDays(daysNum), LocalDate.now());
    }

}
