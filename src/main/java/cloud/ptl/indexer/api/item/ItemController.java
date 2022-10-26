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

    @GetMapping("/expired")
    public List<ItemDTO> getAllExpiredProducts() throws Exception {
        List<ItemEntity> entities = itemService.getAllExpiredProducts();
        return entities.stream().map(ItemDTO::of).toList();
    }
    @GetMapping("/soon-expired/{daysNum}")
    public List<ItemDTO> getAllSoonExpiredProducts(@PathVariable("daysNum") int daysNum) throws Exception {
        List<ItemEntity> entities = itemService.getAllSoonExpiredProducts(daysNum);
        return entities.stream().map(ItemDTO::of).toList();
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

    @GetMapping("/onCategory/{categoryId}")
    @Operation(
            method = "GET",
            summary = "Returns all items on category"
    )
    public List<ItemDTO> getItemsOnCategory(
            @Parameter(description = "category id to resolve") @PathVariable(name = "categoryId") Long categoryId
    ) {
        List<ItemEntity> entities = itemService.getAllByCategory(categoryId);
        return entities.stream().map(ItemDTO::of).toList();
    }

    @GetMapping("/count/in/category/{categoryId}")
    @Operation(
            method = "GET",
            description = "Get number of items in given category"
    )
    public Long countItemsInCategory(
            @Parameter(description = "Id of category to resolve") @PathVariable(name = "categoryId") Long categoryId
    ) {
        return itemService.countItemsOnCategory(categoryId);
    }

    @GetMapping("/count/in/place/{placeId}")
    @Operation(
            method = "GET",
            description = "Count items in given place"
    )
    public Long countItems(
            @Parameter(description = "Place from which item will be counted") @PathVariable("placeId") Long placeId
    ) {
        return itemService.countItemsOnPlace(placeId);
    }

    @Operation(
            summary = "Add one item if possible"
    )
    @PatchMapping("/{id}/add/one")
    public ItemDTO addOneItem(
            @Parameter(description = "item id to resolve") @PathVariable(name = "id") Long id
    ) {
        ItemEntity item = itemService.addOneItem(id);
        return ItemDTO.of(item);
    }

    @Operation(
            summary = "Remove one item if possible"
    )
    @PatchMapping("/{id}/remove/one")
    public ItemDTO removeOneItem(
            @Parameter(description = "item id to resolve") @PathVariable(name = "id") Long id
    ) throws Exception {
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

    @PatchMapping("/add/{itemId}/to/category/{categoryId}")
    @Operation(
            method = "PATCH",
            description = "Add single item to single category"
    )
    @ResponseStatus(HttpStatus.OK)
    public void addItemToCategory(
            @Parameter(description = "Item id to resolve") @PathVariable("itemId") Long itemId,
            @Parameter(description = "Category id to resolve") @PathVariable("categoryId") Long categoryId) {
        itemService.addItemToCategory(itemId, categoryId);
    }

    @PatchMapping("/remove/{itemId}/from/category/{categoryId}")
    @Operation(
            method = "PATCH",
            description = "Remove single item to single category"
    )
    @ResponseStatus(HttpStatus.OK)
    public void removeItemFromCategory(
            @Parameter(description = "Item id to resolve") @PathVariable("itemId") Long itemId,
            @Parameter(description = "Category id to resolve") @PathVariable("categoryId") Long categoryId) {
        itemService.removeItemFromCategory(itemId, categoryId);
    }
}
