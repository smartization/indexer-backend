package cloud.ptl.indexer.api.barcode;

import cloud.ptl.indexer.model.BarcodeEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String title;
    private String value;
    private String searchResult;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime processingTime;
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
