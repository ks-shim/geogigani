package dwayne.shim.geogigani.searching;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
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

    public Map<String, Map<String, String>> asMap(String idFieldName) {
        Map<String, Map<String, String>> map = new HashMap<>();

        for(Map<String, String> doc : docMapList) {
            String id = doc.get(idFieldName);
            if(id == null) throw new RuntimeException("Such an id field doesn't exist ...");

            map.put(id, doc);
        }
        return map;
    }

    public void clear() {
        docMapList.clear();
        docMapList = null;
    }
}
