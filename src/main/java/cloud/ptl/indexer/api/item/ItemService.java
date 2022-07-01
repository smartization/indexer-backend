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

    public ItemEntity getItem(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found"));
    }

    public List<ItemEntity> getAll() {
        return (List<ItemEntity>) itemRepository.findAll();
    }
}
