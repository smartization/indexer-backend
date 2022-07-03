package cloud.ptl.indexer.api.item;

import cloud.ptl.indexer.model.ItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping(value = "/")
    public ItemDTO post(ItemDTO itemDTO) {
        ItemEntity itemEntity = itemService.createItem(itemDTO);
        return ItemDTO.of(itemEntity);
    }

    @GetMapping("/")
    public List<ItemDTO> get() {
        List<ItemEntity> entities = itemService.getAll();
        return entities.stream().map(ItemDTO::of).toList();
    }

    @GetMapping("/{id}")
    public ItemDTO getById(@PathVariable(name = "id") Long id) {
        ItemEntity item = itemService.getItem(id);
        return ItemDTO.of(item);
    }
}
