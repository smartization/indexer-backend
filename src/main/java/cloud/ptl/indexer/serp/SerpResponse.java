package cloud.ptl.indexer.serp;

import lombok.Data;

import java.util.List;

@Data
public class SerpResponse {
    private SearchMetadata search_metadata;
    private SearchParameters search_parameters;
    private SearchInformation search_information;
    private List<InlineImage> inline_images;
    private List<OrganicResult> organic_results;
}
