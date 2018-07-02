package dwayne.shim.geogigani.searching;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class SearchResult {

    private long totalHits;
    private List<Map<String, String>> docMapList;

    public SearchResult() {
        docMapList = new ArrayList<>();
    }

    public void addDocMap(Map<String, String> docMap) {
        docMapList.add(docMap);
    }
}
