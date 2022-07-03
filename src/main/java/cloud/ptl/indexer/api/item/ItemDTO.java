package cloud.ptl.indexer.api.item;

import cloud.ptl.indexer.model.BarcodeType;
import cloud.ptl.indexer.model.ItemEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemDTO {
    private String id;
    private String name;
    private String description;
    private String barcode;
    private BarcodeType barcodeType;

    public static ItemDTO of(ItemEntity item) {
        return ItemDTO.builder()
                .id(item.getId())
                .barcode(item.getBarcode())
                .description(item.getDescription())
                .name(item.getName())
                .barcodeType(item.getBarcodeType())
                .build();
    }

    public ItemEntity toEntity() {
        return ItemEntity.builder()
                .id(id)
                .barcode(barcode)
                .description(description)
                .name(name)
                .barcodeType(barcodeType)
                .build();
    }
}
