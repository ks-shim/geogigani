package dwayne.shim.geogigani.indexing;

import dwayne.shim.geogigani.common.indexing.TravelDataIndexField;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.util.*;

@Log4j2
public class IndexingExecutor {

    public void execute(String inDirLocation,
                        int docSizeLimit,
                        Set<String> allowedContentTypes,
                        String outDirLocation) throws Exception {

        // 1. get ready ...
        new File(outDirLocation).mkdirs();
        BatchIndexer batchIndexer = new BatchIndexer(outDirLocation, 1024.0);

        // 2. read from the source directory ...
        ObjectMapper objectMapper = new ObjectMapper();
        File[] docFiles = new File(inDirLocation).listFiles();
        List<Map<String, String>> docList = new ArrayList<>();
        int docCount = 0;
        int allowedDocCount = 0;
        for(File docFile : docFiles) {
            Map<String, String> docMap = objectMapper.readValue(docFile, Map.class);
            // 2-1. add just only allowed document in the list ...
            if(!allowedContentType(allowedContentTypes, docMap)) continue;

            docList.add(docMap);
            ++allowedDocCount;

            if(++docCount % docSizeLimit != 0) continue;

            batchIndexer.index(docList);
            log.info("indexing {}", docList.size());
            docList.clear();
        }

        // 2-2. do indexing last ones ...
        if(!docList.isEmpty()) {
            batchIndexer.index(docList);
            log.info("indexing {}", docList.size());
            docList.clear();
        }

        log.info("allowed doc count : {}", allowedDocCount);
    }

    private boolean allowedContentType(Set<String> allowedContentTypes,
                                       Map<String, String> docMap) {
        String contentTypeId = docMap.get(TravelDataIndexField.CONTENT_TYPE_ID.label());
        if(contentTypeId == null) return false;

        return allowedContentTypes.contains(contentTypeId);
    }

    public static void main(String[] args) throws Exception {

        final String inLocation = "D:/TravelData";
        final String outLocation = "D:/TravelIndexData";
        final int docSizeLimit = 1000;

        // 관광지, 문화시설, 숙박, 쇼핑, 음식점
        final int[] contentTypes = {12, 14, 32, 38, 39};
        final Set<String> allowedContentTypes = new HashSet<>();
        for(int contentType : contentTypes)
            allowedContentTypes.add(String.valueOf(contentType));

        new IndexingExecutor().execute(inLocation, docSizeLimit, allowedContentTypes, outLocation);
    }
}
