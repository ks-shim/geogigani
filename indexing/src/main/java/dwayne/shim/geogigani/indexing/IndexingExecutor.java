package dwayne.shim.geogigani.indexing;

import com.lyncode.jtwig.functions.util.HtmlUtils;
import dwayne.shim.geogigani.common.indexing.TravelDataIndexField;
import dwayne.shim.geogigani.core.keyword.KeywordExtractor;
import dwayne.shim.geogigani.indexing.distance.LocationDistanceCalculator;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
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

        // 2. ** start calculate distance ...
        ObjectMapper objectMapper = new ObjectMapper();
        final double distanceLimit = 20;
        int docCount = 0;
        LocationDistanceCalculator ldc = new LocationDistanceCalculator();
        File[] docFiles = new File(inDirLocation).listFiles();
        for(File docFile : docFiles) {
            Map<String, String> docMap = objectMapper.readValue(docFile, Map.class);
            // 3-1. add just only allowed document in the list ...
            if(!allowedContentType(allowedContentTypes, docMap)) continue;

            String contentId = docMap.get(TravelDataIndexField.CONTENT_ID.label());
            String mapXstr = docMap.get(TravelDataIndexField.MAP_X.label());
            String mapYstr = docMap.get(TravelDataIndexField.MAP_Y.label());

            if(StringUtils.isBlank(mapXstr) || StringUtils.isBlank(mapYstr)) continue;

            System.out.print("\r Calculating distance : " + ++docCount);
            ldc.addAndCalculateDistance(contentId, Double.valueOf(mapYstr), Double.valueOf(mapXstr), distanceLimit);
        }

        Map<String, LocationDistanceCalculator.LocationMapInfo> distanceMap = ldc.asMap();

        // 3. ** start indexing ...
        List<Map<String, String>> docList = new ArrayList<>();
        docCount = 0;
        int allowedDocCount = 0;
        for(File docFile : docFiles) {
            Map<String, String> docMap = objectMapper.readValue(docFile, Map.class);
            // 3-1. add just only allowed document in the list ...
            if(!allowedContentType(allowedContentTypes, docMap)) continue;

            docList.add(docMap);
            ++allowedDocCount;

            // 3-2. remove html tags
            removeHtmlTags(docMap, TravelDataIndexField.TITLE.label());
            removeHtmlTags(docMap, TravelDataIndexField.OVERVIEW.label());
            /*removeHtmlTags(docMap, TravelDataIndexField.ACCOM_COUNT.label());
            removeHtmlTags(docMap, TravelDataIndexField.CHK_BABY_CARRIAGE.label());
            removeHtmlTags(docMap, TravelDataIndexField.CHK_CREDIT_CARD.label());
            removeHtmlTags(docMap, TravelDataIndexField.CHK_PET.label());
            removeHtmlTags(docMap, TravelDataIndexField.EXP_AGE_RANGE.label());
            removeHtmlTags(docMap, TravelDataIndexField.EXP_GUIDE.label());
            removeHtmlTags(docMap, TravelDataIndexField.INFO_CENTER.label());
            removeHtmlTags(docMap, TravelDataIndexField.OPEN_DATE.label());
            removeHtmlTags(docMap, TravelDataIndexField.PARKING.label());
            removeHtmlTags(docMap, TravelDataIndexField.REST_DATE.label());
            removeHtmlTags(docMap, TravelDataIndexField.USE_SEASON.label());
            removeHtmlTags(docMap, TravelDataIndexField.USE_TIME.label());
            removeHtmlTags(docMap, TravelDataIndexField.ACCOM_COUNT_CULTURE.label());
            removeHtmlTags(docMap, TravelDataIndexField.CHK_BABY_CARRIAGE_CULTURE.label());
            removeHtmlTags(docMap, TravelDataIndexField.CHK_CREDIT_CARD_CULTURE.label());
            removeHtmlTags(docMap, TravelDataIndexField.CHK_PE_TCULTURE.label());
            removeHtmlTags(docMap, TravelDataIndexField.DISCOUNT_INFO.label());
            removeHtmlTags(docMap, TravelDataIndexField.INFO_CENTER_CULTURE.label());
            removeHtmlTags(docMap, TravelDataIndexField.PARKING_CULTURE.label());
            removeHtmlTags(docMap, TravelDataIndexField.PARKING_FEE.label());
            removeHtmlTags(docMap, TravelDataIndexField.REST_DATE_CULTURE.label());
            removeHtmlTags(docMap, TravelDataIndexField.USE_FEE.label());
            removeHtmlTags(docMap, TravelDataIndexField.USE_TIME_CULTURE.label());
            removeHtmlTags(docMap, TravelDataIndexField.SPEND_TIME.label());
            removeHtmlTags(docMap, TravelDataIndexField.AGE_LIMIT.label());
            removeHtmlTags(docMap, TravelDataIndexField.BOOKING_PLACE.label());
            removeHtmlTags(docMap, TravelDataIndexField.DISCOUNT_INFO_FESTIVAL.label());
            removeHtmlTags(docMap, TravelDataIndexField.EVENT_END_DATE.label());
            removeHtmlTags(docMap, TravelDataIndexField.EVEN_THOMEPAGE.label());
            removeHtmlTags(docMap, TravelDataIndexField.EVENT_PLACE.label());
            removeHtmlTags(docMap, TravelDataIndexField.EVEN_TSTART_DATE.label());
            removeHtmlTags(docMap, TravelDataIndexField.PLACE_INFO.label());
            removeHtmlTags(docMap, TravelDataIndexField.PLAY_TIME.label());
            removeHtmlTags(docMap, TravelDataIndexField.PROGRAM.label());
            removeHtmlTags(docMap, TravelDataIndexField.SPEND_TIME_FESTIVAL.label());
            removeHtmlTags(docMap, TravelDataIndexField.SPONSOR1.label());
            removeHtmlTags(docMap, TravelDataIndexField.SPONSOR1_TEL.label());
            removeHtmlTags(docMap, TravelDataIndexField.SPONSOR2.label());
            removeHtmlTags(docMap, TravelDataIndexField.SPONSOR2_TEL.label());
            removeHtmlTags(docMap, TravelDataIndexField.SUB_EVENT.label());
            removeHtmlTags(docMap, TravelDataIndexField.USE_TIME_FESTIVAL.label());
            removeHtmlTags(docMap, TravelDataIndexField.DISTANCE.label());
            removeHtmlTags(docMap, TravelDataIndexField.INFO_CENTER_TOUR_COURSE.label());
            removeHtmlTags(docMap, TravelDataIndexField.SCHEDULE.label());
            removeHtmlTags(docMap, TravelDataIndexField.TAKE_TIME.label());
            removeHtmlTags(docMap, TravelDataIndexField.THEME.label());
            removeHtmlTags(docMap, TravelDataIndexField.ACCOM_COUNT_LEPORTS.label());
            removeHtmlTags(docMap, TravelDataIndexField.CHK_BABY_CARRIAGE_LEPORTS.label());
            removeHtmlTags(docMap, TravelDataIndexField.CHK_CREDIT_CARD_LEPORTS.label());
            removeHtmlTags(docMap, TravelDataIndexField.CHK_PET_LEPORTS.label());
            removeHtmlTags(docMap, TravelDataIndexField.EXP_AGE_RANGE_LEPORTS.label());
            removeHtmlTags(docMap, TravelDataIndexField.INFO_CENTER_LEPORTS.label());
            removeHtmlTags(docMap, TravelDataIndexField.OPEN_PERIOD.label());
            removeHtmlTags(docMap, TravelDataIndexField.PARKING_FEE_LEPORTS.label());
            removeHtmlTags(docMap, TravelDataIndexField.PARKING_LEPORTS.label());
            removeHtmlTags(docMap, TravelDataIndexField.RESERVATION.label());
            removeHtmlTags(docMap, TravelDataIndexField.REST_DATE_LEPORTS.label());
            removeHtmlTags(docMap, TravelDataIndexField.SCALE_LEPORTS.label());
            removeHtmlTags(docMap, TravelDataIndexField.USE_FEE_LEPORTS.label());
            removeHtmlTags(docMap, TravelDataIndexField.USE_TIME_LEPORTS.label());
            removeHtmlTags(docMap, TravelDataIndexField.ACCOM_COUNT_LODGING.label());
            removeHtmlTags(docMap, TravelDataIndexField.CHECK_IN_TIME.label());
            removeHtmlTags(docMap, TravelDataIndexField.CHECK_OUT_TIME.label());
            removeHtmlTags(docMap, TravelDataIndexField.CHK_COOKING.label());
            removeHtmlTags(docMap, TravelDataIndexField.FOOD_PLACE.label());
            removeHtmlTags(docMap, TravelDataIndexField.HANOK.label());
            removeHtmlTags(docMap, TravelDataIndexField.INFO_CENTER_LODGING.label());
            removeHtmlTags(docMap, TravelDataIndexField.PARKING_LODGING.label());
            removeHtmlTags(docMap, TravelDataIndexField.PICKUP.label());
            removeHtmlTags(docMap, TravelDataIndexField.ROOM_COUNT.label());
            removeHtmlTags(docMap, TravelDataIndexField.RESERVATION_LODGING.label());
            removeHtmlTags(docMap, TravelDataIndexField.RESERVATION_URL.label());
            removeHtmlTags(docMap, TravelDataIndexField.ROOM_TYPE.label());
            removeHtmlTags(docMap, TravelDataIndexField.SCALE_LODGING.label());
            removeHtmlTags(docMap, TravelDataIndexField.SUB_FACILITY.label());
            removeHtmlTags(docMap, TravelDataIndexField.BARBECUE.label());
            removeHtmlTags(docMap, TravelDataIndexField.BEAUTY.label());
            removeHtmlTags(docMap, TravelDataIndexField.BEVERAGE.label());
            removeHtmlTags(docMap, TravelDataIndexField.BICYCLE.label());
            removeHtmlTags(docMap, TravelDataIndexField.CAMPFIRE.label());
            removeHtmlTags(docMap, TravelDataIndexField.FITNESS.label());
            removeHtmlTags(docMap, TravelDataIndexField.KARAOKE.label());
            removeHtmlTags(docMap, TravelDataIndexField.PUBLIC_BATH.label());
            removeHtmlTags(docMap, TravelDataIndexField.PUBLIC_PC.label());
            removeHtmlTags(docMap, TravelDataIndexField.SAUNA.label());
            removeHtmlTags(docMap, TravelDataIndexField.SEMINAR.label());
            removeHtmlTags(docMap, TravelDataIndexField.SPORTS.label());
            removeHtmlTags(docMap, TravelDataIndexField.CHK_BABY_CARRIAGE_SHOPPING.label());
            removeHtmlTags(docMap, TravelDataIndexField.CHK_CREDIT_CARD_SHOPPING.label());
            removeHtmlTags(docMap, TravelDataIndexField.CHK_PET_SHOPPING.label());
            removeHtmlTags(docMap, TravelDataIndexField.CULTURE_CENTER.label());
            removeHtmlTags(docMap, TravelDataIndexField.FAIRDAY.label());
            removeHtmlTags(docMap, TravelDataIndexField.INFO_CENTER_SHOPPING.label());
            removeHtmlTags(docMap, TravelDataIndexField.OPEN_DATE_SHOPPING.label());
            removeHtmlTags(docMap, TravelDataIndexField.OPEN_TIME.label());
            removeHtmlTags(docMap, TravelDataIndexField.PARKING_SHOPPING.label());
            removeHtmlTags(docMap, TravelDataIndexField.REST_DATE_SHOPPING.label());
            removeHtmlTags(docMap, TravelDataIndexField.RESTROOM.label());
            removeHtmlTags(docMap, TravelDataIndexField.SALE_ITEM.label());
            removeHtmlTags(docMap, TravelDataIndexField.SALE_ITEM_COST.label());
            removeHtmlTags(docMap, TravelDataIndexField.SCALE_SHOPPING.label());
            removeHtmlTags(docMap, TravelDataIndexField.SHOP_GUIDE.label());
            removeHtmlTags(docMap, TravelDataIndexField.CHK_CREDIT_CARD_FOOD.label());
            removeHtmlTags(docMap, TravelDataIndexField.DISCOUNT_INFO_FOOD.label());
            removeHtmlTags(docMap, TravelDataIndexField.FIRST_MENU.label());
            removeHtmlTags(docMap, TravelDataIndexField.INFO_CENTER_FOOD.label());
            removeHtmlTags(docMap, TravelDataIndexField.KIDS_FACILITY.label());
            removeHtmlTags(docMap, TravelDataIndexField.OPEN_DATE_FOOD.label());
            removeHtmlTags(docMap, TravelDataIndexField.OPEN_TIME_FOOD.label());
            removeHtmlTags(docMap, TravelDataIndexField.PACKING.label());
            removeHtmlTags(docMap, TravelDataIndexField.PARKING_FOOD.label());
            removeHtmlTags(docMap, TravelDataIndexField.RESERVATION_FOOD.label());
            removeHtmlTags(docMap, TravelDataIndexField.REST_DATE_FOOD.label());
            removeHtmlTags(docMap, TravelDataIndexField.SCALE_FOOD.label());
            removeHtmlTags(docMap, TravelDataIndexField.SEAT.label());
            removeHtmlTags(docMap, TravelDataIndexField.SMOKING.label());
            removeHtmlTags(docMap, TravelDataIndexField.TREAT_MENU.label());*/

            // 3-3. extract keywords and put it into docMap as new fields
            extractKeywords(docMap, TravelDataIndexField.TITLE.label(), TravelDataIndexField.TITLE_KEYWORDS.label());
            extractKeywords(docMap, TravelDataIndexField.OVERVIEW.label(), TravelDataIndexField.OVERVIEW_KEYWORDS.label());

            // 3-4. shorten contents (title and overview)
            shortenContent(docMap, TravelDataIndexField.TITLE.label(), TravelDataIndexField.TITLE_SHORT.label(), 10);
            shortenContent(docMap, TravelDataIndexField.OVERVIEW.label(), TravelDataIndexField.OVERVIEW_SHORT.label(), 50);

            // 3-5. add short distance location info
            String contentId = docMap.get(TravelDataIndexField.CONTENT_ID.label());
            addShortDistanceInfo(docMap, distanceMap.get(contentId), 0.0, 5.0, TravelDataIndexField.IN_5KM.label(), 20);
            addShortDistanceInfo(docMap, distanceMap.get(contentId), 5.0, 10.0, TravelDataIndexField.IN_10KM.label(), 20);

            if(++docCount % docSizeLimit != 0) continue;

            batchIndexer.index(docList);
            log.info("indexing {}", docList.size());
            docList.clear();
        }

        // 4. do indexing last ones ...
        if(!docList.isEmpty()) {
            batchIndexer.index(docList);
            log.info("indexing {}", docList.size());
            docList.clear();
        }

        log.info("allowed doc count : {}", allowedDocCount);
        batchIndexer.forceMerge(1);
        batchIndexer.close();
    }

    private void addShortDistanceInfo(Map<String, String> docMap,
                                      LocationDistanceCalculator.LocationMapInfo locationMapInfo,
                                      double distanceFrom,
                                      double distanceTo,
                                      String targetFieldName,
                                      int listSize) {
        if(locationMapInfo == null) return;
        String shortDistances = locationMapInfo.getShortDistancesInfoAsString(distanceFrom, distanceTo, listSize);
        if(StringUtils.isBlank(shortDistances)) return;

        docMap.put(targetFieldName, shortDistances);
    }

    private void removeHtmlTags(Map<String, String> docMap,
                                String fieldName) throws Exception {
        String content = docMap.get(fieldName);
        if(content == null) return;

        content = content.replaceAll("&nbsp;", " ");
        docMap.put(fieldName, HtmlUtils.stripTags(content));
    }

    private void extractKeywords(Map<String, String> docMap,
                                 String sourceFieldName,
                                 String targetFieldName) throws Exception {
        String content = docMap.get(sourceFieldName);
        if(content == null) return;
        docMap.put(targetFieldName, extractKeywords(content));
    }

    private void shortenContent(Map<String, String> docMap,
                                String sourceFieldName,
                                String targetFieldName,
                                int contentLengthLimit) throws Exception {
        String content = docMap.get(sourceFieldName);
        if(content == null) return;
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
        final String outLocation = "D:/TravelLocationIndexData1";
        final int docSizeLimit = 1000;

        // 관광지, 문화시설, 숙박, 쇼핑, 음식점, 축제(15), 여행코스(25)
        final int[] contentTypes = {12, 14, 28, 32, 38, 39, 15, 25};
        final Set<String> allowedContentTypes = new HashSet<>();
        for(int contentType : contentTypes)
            allowedContentTypes.add(String.valueOf(contentType));

        new IndexingExecutor(new KeywordExtractor(keyExtConfigLocation)).execute(inLocation, docSizeLimit, allowedContentTypes, outLocation);
    }
}
