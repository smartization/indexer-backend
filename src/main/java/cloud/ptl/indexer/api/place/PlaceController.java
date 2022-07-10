package cloud.ptl.indexer.api.place;

import cloud.ptl.indexer.model.PlaceEntity;
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
    public PlaceDTO get(@PathVariable("id") Long id) {
        PlaceEntity entity = placeService.getPlace(id);
        return PlaceDTO.of(entity);
    }

    @PostMapping("/")
    public PlaceDTO post(@Valid PlaceDTO placeDTO) {
        PlaceEntity entity = placeService.createPlace(placeDTO);
        return PlaceDTO.of(entity);
    }

    @GetMapping("/")
    public List<PlaceDTO> getAll() {
        List<PlaceEntity> entities = placeService.getAll();
        return entities.stream().map(PlaceDTO::of).toList();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        placeService.deletePlace(id);
    }

    @PatchMapping("/{placeId}/add/item/{itemId}")
    public PlaceDTO addItem(@PathVariable("placeId") Long placeId, @PathVariable("itemId") Long itemId) {
        PlaceEntity entity = placeService.addItemToPlace(placeId, itemId);
        return PlaceDTO.of(entity);
    }

    @PatchMapping("/{placeId}/delete/item/{itemId}")
    public PlaceDTO removeItem(@PathVariable("placeId") Long placeId, @PathVariable("itemId") Long itemId) {
        PlaceEntity entity = placeService.removeItemFromPlace(placeId, itemId);
        return PlaceDTO.of(entity);
    }
}
