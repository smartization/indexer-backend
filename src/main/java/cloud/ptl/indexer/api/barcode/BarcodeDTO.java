package cloud.ptl.indexer.api.barcode;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.net.URL;
import java.time.LocalDateTime;

@Data
public class BarcodeDTO {
    private String title;
    private String searchSuccess;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime processingTime;
    private URL link;
}
