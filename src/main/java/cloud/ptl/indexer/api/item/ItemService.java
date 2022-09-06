package cloud.ptl.indexer.api.item;

import cloud.ptl.indexer.model.ItemEntity;
import cloud.ptl.indexer.repositories.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Component
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

    public ItemEntity updateItem(ItemDTO itemDTO) {
        return save(itemDTO.toEntity());
    }

    public List<ItemEntity> getItemsOnPlace(Long placeId) {
        return itemRepository.findAllByStoragePlace_Id(placeId);
    }

    public ItemEntity addOneItem(Long itemId) {
        ItemEntity item = getItem(itemId);
        item.setQuantity(item.getQuantity() + 1);
        return save(item);
    }

    public ItemEntity removeOneItem(Long itemId) {
        ItemEntity item = getItem(itemId);
        if (item.getQuantity() == 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.format("Item %d has no items to remove", itemId)
            );
        }
        item.setQuantity(item.getQuantity() - 1);
        return save(item);
    }
}
