package cloud.ptl.indexer.serp;

import lombok.Data;

@Data
public class SearchMetadata {
    private String id;
    private String status;
    private String json_endpoint;
    private String created_at;
    private String processed_at;
    private String google_url;
    private String raw_html_file;
    private double total_time_taken;
}
