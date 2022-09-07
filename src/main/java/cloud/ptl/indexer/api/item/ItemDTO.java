package cloud.ptl.indexer.api.item;

import cloud.ptl.indexer.api.item.validators.BarcodeFormat;
import cloud.ptl.indexer.api.item.validators.BarcodeLength;
import cloud.ptl.indexer.api.place.PlaceDTO;
import cloud.ptl.indexer.model.BarcodeType;
import cloud.ptl.indexer.model.ItemEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Long id;
    @NotNull
    private String name;
    private String description;
    private String barcode;
    private BarcodeType barcodeType;
    private PlaceDTO storagePlace;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
    public static ItemDTO of(ItemEntity item){
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
