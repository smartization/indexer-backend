package cloud.ptl.indexer.api.place;

import cloud.ptl.indexer.model.PlaceEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/places")
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService placeService;

    @GetMapping("/{id}")
    @Operation(
            summary = "Get one place pointed by id"
    )
    public PlaceDTO get(@PathVariable("id") Long id) {
        PlaceEntity entity = placeService.getPlace(id);
        return PlaceDTO.of(entity);
    }

    @PostMapping("/")
    @Operation(
            summary = "Add one place"
    )
    public PlaceDTO post(@Valid @RequestBody PlaceDTO placeDTO) {
        PlaceEntity entity = placeService.createPlace(placeDTO);
        return PlaceDTO.of(entity);
    }

    @GetMapping("/")
    @Operation(
            summary = "Get all available places"
    )
    public List<PlaceDTO> getAll() {
        List<PlaceEntity> entities = placeService.getAll();
        return entities.stream().map(PlaceDTO::of).toList();
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete one place pointed by give id"
    )
    public void delete(@PathVariable Long id) {
        placeService.deletePlace(id);
    }

    @PatchMapping("/{placeId}/add/item/{itemId}")
    @Operation(
            summary = "Add single item into given place"
    )
    public PlaceDTO addItem(
            @Parameter(description = "Place to which item will be added") @PathVariable("placeId") Long placeId,
            @Parameter(description = "Item which will be added") @PathVariable("itemId") Long itemId
    ) {
        PlaceEntity entity = placeService.addItemToPlace(placeId, itemId);
        return PlaceDTO.of(entity);
    }

    @PatchMapping("/{placeId}/delete/item/{itemId}")
    @Operation(
            summary = "Delete single item from given place"
    )
    public PlaceDTO removeItem(
            @Parameter(description = "Place from which item will be deleted") @PathVariable("placeId") Long placeId,
            @Parameter(description = "Item which will be deleted") @PathVariable("itemId") Long itemId
    ) {
        PlaceEntity entity = placeService.removeItemFromPlace(placeId, itemId);
        return PlaceDTO.of(entity);
    }

    @GetMapping("/{placeId}/count")
    @Operation(
            summary = "Count items in given place"
    )
    public Long countItems(
            @Parameter(description = "Place from which item will be counted") @PathVariable("placeId") Long placeId
    ) {
        return placeService.countItems(placeId);
    }
}
