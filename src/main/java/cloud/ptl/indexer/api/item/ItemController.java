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

    @GetMapping("/onPlace/{placeId}")
    @Operation(
            summary = "Returns all items on place"
    )
    public List<ItemDTO> getItemsOnPlace(
            @Parameter(description = "place id to resolve") @PathVariable(name = "placeId") Long placeId
    ) {
        List<ItemEntity> entities = itemService.getItemsOnPlace(placeId);
        return entities.stream().map(ItemDTO::of).toList();
    }

    @Operation(
            summary = "Add one item if possible"
    )
    @PatchMapping("/{id}/add/one")
    public ItemDTO addOneItem(
            @Parameter(description = "item id to resolve") @PathVariable(name = "id") Long id
    ) {
        ItemEntity item = itemService.getItem(id);
        return ItemDTO.of(item);
    }

    @Operation(
            summary = "Remove one item if possible"
    )
    @PatchMapping("/{id}/remove/one")
    public ItemDTO removeOneItem(
            @Parameter(description = "item id to resolve") @PathVariable(name = "id") Long id
    ) {
        ItemEntity item = itemService.removeOneItem(id);
        return ItemDTO.of(item);
    }

    @PatchMapping("/{id}/enable/quantity")
    @Operation(
            summary = "Enable quantity for item"
    )
    public ItemDTO enableItemQuantity(
            @Parameter(description = "item id to resolve") @PathVariable(name = "id") Long id
    ) {
        ItemEntity item = itemService.enableItemQuantity(id);
        return ItemDTO.of(item);
    }

    @PatchMapping("/{id}/disable/quantity")
    @Operation(
            summary = "Disable quantity for item"
    )
    public ItemDTO disableItemQuantity(
            @Parameter(description = "item id to resolve") @PathVariable(name = "id") Long id
    ) {
        ItemEntity item = itemService.disableItemQuantity(id);
        return ItemDTO.of(item);
    }
}
