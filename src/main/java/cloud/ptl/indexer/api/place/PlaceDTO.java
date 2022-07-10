package cloud.ptl.indexer.api.place;

import cloud.ptl.indexer.model.PlaceEntity;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Data
@Builder
public class PlaceDTO {
    private Long id;
    @NotNull(message = "Name cannot be null")
    private String name;

    public static PlaceDTO of(PlaceEntity entity) {
        return PlaceDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public PlaceEntity toEntity() {
        return PlaceEntity.builder()
                .id(id)
                .name(name)
                .items(new ArrayList<>())
                .build();
    }
}
