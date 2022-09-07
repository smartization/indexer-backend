package cloud.ptl.indexer.api.barcode;

import cloud.ptl.indexer.model.BarcodeEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BarcodeDTO {
    @Schema(
            description = "Contains item title obtained from resolving api",
            example = "Chusteczki higieniczne 2-warstwowe 150 sztuk"
    )
    private String title;
    @Schema(
            description = "Barcode numeric or textual value",
            example = "4337185324246"
    )
    private String value;
    @Schema(
            description = "Result of searching in external api",
            example = "cached"
    )
    private String searchResult;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(
            description = "Time when barcode was resolved",
            example = "2022-07-12 16:10:21"
    )
    private LocalDateTime processingTime;
    @Schema(
            description = "Site from which barcode was resolved",
            example = "https://archiwum.allegro.pl/oferta/chusteczki-higieniczne-2-warstwowe-150-sztuk-i9179356701.html"
    )
    private URL link;

    public static BarcodeDTO from(BarcodeEntity entity) {
        return BarcodeDTO.builder()
                .title(entity.getTitle())
                .value(entity.getValue())
                .searchResult("cached")
                .processingTime(entity.getProcessingTime())
                .link(entity.getLink())
                .build();
    }

    public BarcodeEntity toEntity() {
        return BarcodeEntity.builder()
                .title(title)
                .value(value)
                .link(link)
                .processingTime(processingTime)
                .build();
    }
}
