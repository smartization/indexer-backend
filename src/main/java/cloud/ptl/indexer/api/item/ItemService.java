package cloud.ptl.indexer.api.item;

import cloud.ptl.indexer.api.category.CategoryService;
import cloud.ptl.indexer.api.item.sync.DeleteSyncCommand;
import cloud.ptl.indexer.api.item.sync.ItemSyncEventExecutor;
import cloud.ptl.indexer.api.item.sync.SaveSyncCommand;
import cloud.ptl.indexer.api.item.sync.UpdateSyncCommand;
import cloud.ptl.indexer.api.notification.MediaEnum;
import cloud.ptl.indexer.api.notification.NotificationMediator;
import cloud.ptl.indexer.api.notification.TemplateEnum;
import cloud.ptl.indexer.model.CategoryEntity;
import cloud.ptl.indexer.model.ItemEntity;
import cloud.ptl.indexer.repositories.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Component
@EnableScheduling
@Slf4j
public class ItemService {
    private final ItemRepository itemRepository;
    private final CategoryService categoryService;
    private final NotificationMediator notificationMediator;
    private final ItemSyncEventExecutor itemSyncEventExecutor;

    public ItemService(ItemRepository itemRepository, CategoryService categoryService,
            @Lazy NotificationMediator notificationMediator,
            ItemSyncEventExecutor itemSyncEventExecutor) {
        this.itemRepository = itemRepository;
        this.categoryService = categoryService;
        this.notificationMediator = notificationMediator;
        this.itemSyncEventExecutor = itemSyncEventExecutor;
    }


    public ItemEntity save(ItemDTO itemDTO) {
        ItemEntity entity = itemDTO.toEntity();
        entity = save(entity);
        itemSyncEventExecutor.execute(new SaveSyncCommand(entity.getId()));
        return entity;
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

    public List<ItemEntity> getAllByCategory(Long categoryId) {
        return itemRepository.findAllByCategory_Id(categoryId);
    }

    public void deleteItem(Long id) {
        itemSyncEventExecutor.execute(new DeleteSyncCommand(id));
        itemRepository.deleteById(id);
    }

    public List<ItemEntity> getAllExpiredProducts() {
        return itemRepository.findByDueDateBefore(LocalDate.now());
    }

    public List<ItemEntity> getAllSoonExpiredProducts(int daysNum) {
        return itemRepository.findByDueDateIsBetween( LocalDate.now(), LocalDate.now().plusDays(daysNum));
    }

    public ItemEntity updateItem(ItemDTO itemDTO) {
        itemSyncEventExecutor.execute(new UpdateSyncCommand(itemDTO.getId()));
        return save(itemDTO.toEntity());
    }

    public List<ItemEntity> getItemsOnPlace(Long placeId) {
        return itemRepository.findAllByStoragePlace_Id(placeId);
    }

    public ItemEntity addOneItem(Long itemId) {
        ItemEntity item = getItem(itemId);
        checkIfItemHasQuanity(item);
        item.incrementQuantity();
        return save(item);
    }

    public ItemEntity removeOneItem(Long itemId) throws Exception {
        ItemEntity item = getItem(itemId);
        checkIfItemHasQuanity(item);
        if (item.getQuantity() == 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Item %d has no items to remove", itemId)
            );
        }
        item.decrementQuantity();
        tryToSendNotificationAboutLowQuantity(item);
        return save(item);
    }

    private void tryToSendNotificationAboutLowQuantity(ItemEntity item) throws Exception {
        if (item.getNotifyQuantity() != null && item.getQuantity() <= item.getNotifyQuantity()) {
            notificationMediator.sendNotification(TemplateEnum.QUANTITY, MediaEnum.MAIL);
        }
    }

    public boolean checkIfItemHasQuanity(ItemEntity item) {
        if (item.getQuantity() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Item %d does not have quantity", item.getId())
            );
        }
        return true;
    }

    public ItemEntity enableItemQuantity(Long itemId) {
        ItemEntity item = getItem(itemId);
        item.setQuantity(1);
        return save(item);
    }

    public ItemEntity disableItemQuantity(Long itemId) {
        ItemEntity item = getItem(itemId);
        item.setQuantity(null);
        return save(item);
    }

    public boolean addItemToCategory(Long itemId, Long categoryId) {
        ItemEntity item = getItem(itemId);
        CategoryEntity category = categoryService.findById(categoryId);
        item.setCategory(category);
        if (category.getItems().add(item)) {
            categoryService.save(category);
            save(item);
            return true;
        }
        return false;
    }

    public boolean removeItemFromCategory(Long itemId, Long categoryId) {
        ItemEntity item = getItem(itemId);
        CategoryEntity category = categoryService.findById(categoryId);
        if (category.getItems().remove(item)) {
            categoryService.save(category);
            save(item);
            return false;
        }
        return true;
    }

    public Long countItemsOnPlace(Long placeId) {
        return itemRepository.countByStoragePlace_Id(placeId);
    }

    public Long countItemsOnCategory(Long categoryId) {
        return itemRepository.countByCategory(categoryId);
    }

    public List<ItemEntity> getAllLowQuantityProducts() {
        return itemRepository.findByQuantityLessThanEqual();
    }
}
