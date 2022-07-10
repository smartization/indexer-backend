package cloud.ptl.indexer.serp;

import lombok.Data;

import java.util.List;

@Data
public class OrganicResult {
    private int position;
    private String title;
    private String link;
    private String displayed_link;
    private String snippet;
    private List<String> snippet_highlighted_words;
    private String cached_page_link;
    private String thumbnail;
    private RichSnippet rich_snippet;
    private String date;
    private List<RelatedResult> related_results;
}
