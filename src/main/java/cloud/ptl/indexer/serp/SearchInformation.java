package cloud.ptl.indexer.serp;

import lombok.Data;

@Data
public class SearchInformation {
    private String organic_results_state;
    private String query_displayed;
    private int total_results;
    private double time_taken_displayed;
}