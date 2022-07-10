package cloud.ptl.indexer.serp;

import lombok.Data;

import java.util.List;

@Data
public class RelatedResult {
    private int position;
    private String title;
    private String link;
    private String displayed_link;
    private String date;
    private String snippet;
    private List<String> snippet_highlighted_words;
    private RichSnippet rich_snippet;
    private String cached_page_link;
}
