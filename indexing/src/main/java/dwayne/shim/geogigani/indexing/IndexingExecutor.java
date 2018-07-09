package dwayne.shim.geogigani.indexing;

import com.lyncode.jtwig.functions.util.HtmlUtils;
import dwayne.shim.geogigani.common.indexing.TravelDataIndexField;
import dwayne.shim.geogigani.core.keyword.KeywordExtractor;
import dwayne.shim.geogigani.indexing.tfidf.DFCalculator;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.util.*;

@Log4j2
public class IndexingExecutor {

    private final KeywordExtractor keywordExtractor;
    public IndexingExecutor(KeywordExtractor keywordExtractor) {
        this.keywordExtractor = keywordExtractor;
    }

    public void execute(String inDirLocation,
                        int docSizeLimit,
                        Set<String> allowedContentTypes,
                        String outDirLocation) throws Exception {

        // 1. get ready ...
        new File(outDirLocation).mkdirs();
        BatchIndexer batchIndexer = new BatchIndexer(outDirLocation, 1024.0);

        // 2. ** start calculate idf ...
        ObjectMapper objectMapper = new ObjectMapper();
        File[] docFiles = new File(inDirLocation).listFiles();

        // 3. ** start indexing ...
        List<Map<String, String>> docList = new ArrayList<>();
        int docCount = 0;
        int allowedDocCount = 0;
        for(File docFile : docFiles) {
            Map<String, String> docMap = objectMapper.readValue(docFile, Map.class);
            // 2-1. add just only allowed document in the list ...
            if(!allowedContentType(allowedContentTypes, docMap)) continue;

            docList.add(docMap);
            ++allowedDocCount;

            // 2-2. remove html tags
            removeHtmlTags(docMap, TravelDataIndexField.TITLE.label());
            removeHtmlTags(docMap, TravelDataIndexField.OVERVIEW.label());

            // 2-3. extract keywords and put it into docMap as new fields
            extractKeywords(docMap, TravelDataIndexField.TITLE.label(), TravelDataIndexField.TITLE_KEYWORDS.label());
            extractKeywords(docMap, TravelDataIndexField.OVERVIEW.label(), TravelDataIndexField.OVERVIEW_KEYWORDS.label());

            // 2-4. shorten contents (title and overview)
            shortenContent(docMap, TravelDataIndexField.TITLE.label(), TravelDataIndexField.TITLE_SHORT.label(), 10);
            shortenContent(docMap, TravelDataIndexField.OVERVIEW.label(), TravelDataIndexField.OVERVIEW_SHORT.label(), 50);

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
        batchIndexer.forceMerge(1);
        batchIndexer.close();
    }

    private void removeHtmlTags(Map<String, String> docMap,
                                String fieldName) throws Exception {
        String content = docMap.get(fieldName);
        content = content.replaceAll("&nbsp;", " ");
        docMap.put(fieldName, HtmlUtils.stripTags(content));
    }

    private void extractKeywords(Map<String, String> docMap,
                                 String sourceFieldName,
                                 String targetFieldName) throws Exception {
        String content = docMap.get(sourceFieldName);
        docMap.put(targetFieldName, extractKeywords(content));
    }

    private void shortenContent(Map<String, String> docMap,
                                String sourceFieldName,
                                String targetFieldName,
                                int contentLengthLimit) throws Exception {
        String content = docMap.get(sourceFieldName);
        if(content.length() > contentLengthLimit) content = content.substring(0, contentLengthLimit-3) + " ...";
        docMap.put(targetFieldName, content);
    }

    private String extractKeywords(String content) {
        if(content == null) return "";
        List<String> keywords = keywordExtractor.extract(content);
        return asString(keywords);
    }

    private String asString(List<String> keywords) {
        StringBuilder sb = new StringBuilder();
        for(String keyword : keywords)
            sb.append(keyword).append(' ');

        return sb.toString().trim();
    }

    private boolean allowedContentType(Set<String> allowedContentTypes,
                                       Map<String, String> docMap) {
        String contentTypeId = docMap.get(TravelDataIndexField.CONTENT_TYPE_ID.label());
        if(contentTypeId == null) return false;

        return allowedContentTypes.contains(contentTypeId);
    }

    public static void main(String[] args) throws Exception {

        final String keyExtConfigLocation = "D:/korean-analyzer/configurations/main.conf";
        final String inLocation = "D:/TravelLocationData";
        final String outLocation = "D:/TravelLocationIndexData";
        final int docSizeLimit = 1000;

        // 관광지, 문화시설, 숙박, 쇼핑, 음식점
        final int[] contentTypes = {12, 14, 32, 38, 39};
        final Set<String> allowedContentTypes = new HashSet<>();
        for(int contentType : contentTypes)
            allowedContentTypes.add(String.valueOf(contentType));

        new IndexingExecutor(new KeywordExtractor(keyExtConfigLocation)).execute(inLocation, docSizeLimit, allowedContentTypes, outLocation);
    }
}
