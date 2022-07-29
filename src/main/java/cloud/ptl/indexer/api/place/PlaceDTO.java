package cloud.ptl.indexer.api.place;

import cloud.ptl.indexer.model.PlaceEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Data
@Builder
public class PlaceDTO {
    @Schema(
            description = "Internal place identifier",
            example = "1"
    )
    private Long id;
    @NotNull(message = "Name cannot be null")
    @Schema(
            description = "Place name",
            example = "Home"
    )
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
