package cloud.ptl.indexer.serp;

import lombok.Data;

@Data
public class SearchParameters {
    private String engine;
    private String q;
    private String google_domain;
    private String hl;
    private String gl;
    private String device;
}
