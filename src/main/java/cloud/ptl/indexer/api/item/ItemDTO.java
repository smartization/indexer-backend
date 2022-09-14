package cloud.ptl.indexer.api.item;

import cloud.ptl.indexer.api.category.CategoryDTO;
import cloud.ptl.indexer.api.place.PlaceDTO;
import cloud.ptl.indexer.model.BarcodeType;
import cloud.ptl.indexer.model.ItemEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@Slf4j
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
            nullable = true,
            example = "słoik nutelli"
    )
    private String description;
    @Schema(
            description = "Numeric or textual barcode of item",
            example = "8000500179864",
            nullable = true
    )
    private String barcode;
    @Schema(
            description = "Type of barcode like EAN16",
            example = "EAN"
    )
    private BarcodeType barcodeType;
    @Schema(
            description = "Location in which item is hold",
            nullable = true
    )
    private PlaceDTO storagePlace;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(
            description = "Due date of item",
            nullable = true,
            example = "2022-07-07"
    )
    private LocalDate dueDate;
    @Schema(
            description = "Quantity of items",
            nullable = true,
            example = "1"
    )
    private Integer quantity;
    @Schema(
            description = "Category of item",
            nullable = true
    )
    private CategoryDTO category;

    public static ItemDTO of(ItemEntity item) {
        ItemDTO dto = ItemDTO.builder()
                .id(item.getId())
                .barcode(item.getBarcode())
                .description(item.getDescription())
                .name(item.getName())
                .barcodeType(item.getBarcodeType())
                .dueDate(item.getDueDate())
                .quantity(item.getQuantity())
                .build();
        // there could be items with no storage location set already
        if (item.getStoragePlace() != null) {
            dto.setStoragePlace(PlaceDTO.of(item.getStoragePlace()));
        }
        if (item.getCategory() != null) {
            dto.setCategory(CategoryDTO.of(item.getCategory()));
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
                .quantity(quantity)
                .build();
        if (storagePlace != null) {
            log.info("adding place to " + this);
            entity.setStoragePlace(storagePlace.toEntity());
        }
        if (category != null) {
            log.info("adding category to " + this);
            entity.setCategory(category.toEntity());
        }
        return entity;
    }
}
