package cloud.ptl.indexer.api.item;

import cloud.ptl.indexer.model.ItemEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping(value = "/")
    @Operation(
            summary = "Post single item into indexer"
    )
    public ItemDTO post(@Valid @RequestBody ItemDTO itemDTO) {
        ItemEntity itemEntity = itemService.createItem(itemDTO);
        return ItemDTO.of(itemEntity);
    }

    @GetMapping("/")
    @Operation(
            summary = "Returns all possible items"
    )
    public List<ItemDTO> get() {
        List<ItemEntity> entities = itemService.getAll();
        return entities.stream().map(ItemDTO::of).toList();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Returns single entity pointed by it id"
    )
    public ItemDTO getById(
            @Parameter(description = "item id to resolve") @PathVariable(name = "id") Long id
    ) {
        ItemEntity item = itemService.getItem(id);
        return ItemDTO.of(item);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Delete single item pointed by its id"
    )
    public void delete(
            @Parameter(description = "item description") @PathVariable("id") Long id) {
        itemService.deleteItem(id);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update single item"
    )
    public ItemDTO put(
            @Valid @RequestBody ItemDTO itemDTO) {
        ItemEntity itemEntity = itemService.updateItem(itemDTO);
        return ItemDTO.of(itemEntity);
    }
}
