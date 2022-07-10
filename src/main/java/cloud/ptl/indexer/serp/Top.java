package cloud.ptl.indexer.serp;

import lombok.Data;

import java.util.List;

@Data
public class Top {
    private DetectedExtensions detected_extensions;
    private List<String> extensions;
}
