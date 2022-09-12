package cloud.ptl.indexer.api.place;

import cloud.ptl.indexer.api.item.ItemService;
import cloud.ptl.indexer.model.ItemEntity;
import cloud.ptl.indexer.model.PlaceEntity;
import cloud.ptl.indexer.repositories.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final ItemService itemService;

    public PlaceEntity createPlace(PlaceDTO placeDTO) {
        PlaceEntity entity = placeDTO.toEntity();
        return placeRepository.save(entity);
    }

    public PlaceEntity save(PlaceEntity place) {
        return placeRepository.save(place);
    }

    public PlaceEntity getPlace(Long id) {
        return placeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found"));
    }

    public List<PlaceEntity> getAll() {
        return (List<PlaceEntity>) placeRepository.findAll();
    }

    public void deletePlace(Long id) {
        placeRepository.deleteById(id);
    }

    public PlaceEntity addItemToPlace(Long placeId, Long itemId) {
        PlaceEntity place = getPlace(placeId);
        ItemEntity item = itemService.getItem(itemId);
        place.getItems().add(item);
        item.setStoragePlace(place);
        itemService.save(item);
        return save(place);
    }

    public PlaceEntity removeItemFromPlace(Long placeId, Long itemId) {
        PlaceEntity place = getPlace(placeId);
        ItemEntity item = itemService.getItem(itemId);
        if (!place.getItems().remove(item)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    String.join("Item %d does not belong to place %d", itemId.toString(), placeId.toString())
            );
        }
        item.setStoragePlace(null);
        itemService.save(item);
        return save(place);
    }

    public PlaceEntity updatePlace(PlaceDTO placeDTO) {
        return save(placeDTO.toEntity());
    }
}
