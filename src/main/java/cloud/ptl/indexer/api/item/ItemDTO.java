package cloud.ptl.indexer.api.item;

import cloud.ptl.indexer.api.item.validators.BarcodeFormat;
import cloud.ptl.indexer.api.item.validators.BarcodeLength;
import cloud.ptl.indexer.api.place.PlaceDTO;
import cloud.ptl.indexer.model.BarcodeType;
import cloud.ptl.indexer.model.ItemEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@Slf4j
@Builder
@BarcodeFormat
@BarcodeLength
public class ItemDTO {
    @Schema(
            description = "Internal identifier of item",
            example = "1"
    )
    private Long id;
    @NotNull
    @Schema(
            description = "Name of otem",
            example = "nutella"
    )
    private String name;
    @Schema(
            description = "Item description",
            example = "słoik nutelli"
    )
    private String description;
    @Schema(
            description = "Numeric or textual barcode of item",
            example = "8000500179864"
    )
    private String barcode;
    @Schema(
            description = "Type of barcode like EAN16",
            example = "EAN"
    )
    private BarcodeType barcodeType;
    @Schema(
            description = "Location in which item is hold"
    )
    private PlaceDTO storagePlace;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(
            description = "Due date of item",
            example = "2022-07-07"
    )
    private LocalDate dueDate;

    public static ItemDTO of(ItemEntity item) {
        ItemDTO dto = ItemDTO.builder()
                .id(item.getId())
                .barcode(item.getBarcode())
                .description(item.getDescription())
                .name(item.getName())
                .barcodeType(item.getBarcodeType())
                .dueDate(item.getDueDate())
                .build();
        // there could be items with no storage location set already
        if (item.getStoragePlace() != null) {
            dto.setStoragePlace(PlaceDTO.of(item.getStoragePlace()));
        }
        return dto;
    }

    public static List<ItemDTO> of(List<ItemEntity> items) {
        return items.stream().map(ItemDTO::of).toList();
    }

    public ItemEntity toEntity() {
        ItemEntity entity = ItemEntity.builder()
                .id(id)
                .barcode(barcode)
                .description(description)
                .name(name)
                .barcodeType(barcodeType)
                .dueDate(dueDate)
                .build();
        if(storagePlace != null){
            log.info("adding place to " + this);
            entity.setStoragePlace(storagePlace.toEntity());
        }
        return entity;
    }
}
