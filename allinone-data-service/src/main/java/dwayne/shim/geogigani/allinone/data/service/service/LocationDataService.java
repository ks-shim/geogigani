package dwayne.shim.geogigani.allinone.data.service.service;

import dwayne.shim.geogigani.common.data.TravelData;
import dwayne.shim.geogigani.common.indexing.TravelDataIndexField;
import dwayne.shim.geogigani.common.storage.IdWeightSnapshot;
import dwayne.shim.geogigani.core.storage.IdWeightStorage;
import dwayne.shim.geogigani.searching.SearchResult;
import dwayne.shim.geogigani.searching.SearchingExecutor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.map.LRUMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
public class LocationDataService {

    @Value("${location.inertia}")
    private double inertia;

    @Value("${location.topn}")
    private int topN;

    @Value("${location.similar.size}")
    private int similarLocationSize;

    @Value("${location.short.dist.size}")
    private int shortDistLocationSize;

    @Value("${location.interest.size}")
    private int interestLocationSize;

    @Value("${location.snapshot.dir}")
    private String locationSnapshotDir;

    @Autowired
    private IdWeightStorage locationStorage;

    @Autowired
    private SearchingExecutor searchingExecutor;

    private final LRUMap<Object, List<TravelData>> cache;
    public LocationDataService() {
        cache = new LRUMap<>(2);
    }

    private final String[] fieldToSearchForPopularLocations = {TravelDataIndexField.CONTENT_ID.label()};
    private final String[] fieldToGetForPopularLocations = {
            TravelDataIndexField.CONTENT_ID.label(),
            TravelDataIndexField.CONTENT_TYPE_ID.label(),
            TravelDataIndexField.TITLE.label(),
            TravelDataIndexField.TITLE_SHORT.label(),
            TravelDataIndexField.OVERVIEW.label(),
            TravelDataIndexField.OVERVIEW_SHORT.label(),
            TravelDataIndexField.ADDR1.label(),
            TravelDataIndexField.CAT1.label(),
            TravelDataIndexField.AREA_CODE.label(),
            TravelDataIndexField.FIRST_IMAGE.label(),
    };

    //***************************************************************************************
    // Popular location
    //***************************************************************************************
    public List<TravelData> getPopularLocations() throws Exception {
        // 1. get topN id-weights
        IdWeightSnapshot[] popularLocations = locationStorage.getTopNIdWeights();

        // 2. build key to search
        StringBuilder sb = new StringBuilder();
        for(IdWeightSnapshot location : popularLocations) {
            // 2-1. increment impress count  ...
            //locationStorage.impress(location.getId());
            // 2-2. append keyword
            sb.append(location.getId()).append(' ');
        }
        String ids = sb.toString().trim();

        // 3. If cached data exists, return it right away !!
        List<TravelData> cachedTravelData = null;
        synchronized (cache) {
             cachedTravelData = cache.get(popularLocations);
        }
        if(cachedTravelData != null) return cachedTravelData;

        // 4. If cached data doesn't exist, search from lucene
        SearchResult result = searchingExecutor.search(
                fieldToGetForPopularLocations,
                fieldToSearchForPopularLocations,
                ids,
                topN
        );

        Map<String, Map<String, String>> idDocMap = result.asMap(TravelDataIndexField.CONTENT_ID.label());

        // 5. create TravelData list ...
        List<TravelData> travelDataList = new ArrayList<>();
        for(IdWeightSnapshot location : popularLocations) {
            Map<String, String> docMap = idDocMap.get(location.getId());
            if(docMap == null) continue;
            travelDataList.add(new TravelData(location, docMap));
        }

        // 6. clear
        idDocMap.clear(); idDocMap = null;
        result.clear();

        // 7. put travel-data into the cache !!
        synchronized (cache) {
            cache.put(popularLocations, travelDataList);
        }
        return travelDataList;
    }

    //***************************************************************************************
    // Location detail
    //***************************************************************************************
    private final String[] fieldToSearchForLocationDetail = {TravelDataIndexField.CONTENT_ID.label()};
    private final String[] fieldToGetForLocationDetail = {
            TravelDataIndexField.CONTENT_ID.label(),
            TravelDataIndexField.CONTENT_TYPE_ID.label(),
            TravelDataIndexField.CAT1.label(),
            TravelDataIndexField.AREA_CODE.label(),
            TravelDataIndexField.TITLE.label(),
            TravelDataIndexField.TITLE_KEYWORDS.label(),
            TravelDataIndexField.OVERVIEW.label(),
            TravelDataIndexField.OVERVIEW_KEYWORDS.label(),
            TravelDataIndexField.ADDR1.label(),
            TravelDataIndexField.ADDR2.label(),
            TravelDataIndexField.TEL.label(),
            TravelDataIndexField.TEL_NAME.label(),
            TravelDataIndexField.FIRST_IMAGE.label(),
            TravelDataIndexField.FIRST_IMAGE2.label(),
            TravelDataIndexField.MAP_X.label(),
            TravelDataIndexField.MAP_Y.label(),
            TravelDataIndexField.HOMEPAGE.label(),
            TravelDataIndexField.IN_5KM.label(),
            TravelDataIndexField.IN_10KM.label(),
            TravelDataIndexField.ACCOM_COUNT.label(),
            TravelDataIndexField.CHK_BABY_CARRIAGE.label(),
            TravelDataIndexField.CHK_CREDIT_CARD.label(),
            TravelDataIndexField.CHK_PET.label(),
            TravelDataIndexField.EXP_AGE_RANGE.label(),
            TravelDataIndexField.EXP_GUIDE.label(),
            TravelDataIndexField.INFO_CENTER.label(),
            TravelDataIndexField.OPEN_DATE.label(),
            TravelDataIndexField.PARKING.label(),
            TravelDataIndexField.REST_DATE.label(),
            TravelDataIndexField.USE_SEASON.label(),
            TravelDataIndexField.USE_TIME.label(),
            TravelDataIndexField.ACCOM_COUNT_CULTURE.label(),
            TravelDataIndexField.CHK_BABY_CARRIAGE_CULTURE.label(),
            TravelDataIndexField.CHK_CREDIT_CARD_CULTURE.label(),
            TravelDataIndexField.CHK_PE_TCULTURE.label(),
            TravelDataIndexField.DISCOUNT_INFO.label(),
            TravelDataIndexField.INFO_CENTER_CULTURE.label(),
            TravelDataIndexField.PARKING_CULTURE.label(),
            TravelDataIndexField.PARKING_FEE.label(),
            TravelDataIndexField.REST_DATE_CULTURE.label(),
            TravelDataIndexField.USE_FEE.label(),
            TravelDataIndexField.USE_TIME_CULTURE.label(),
            TravelDataIndexField.SPEND_TIME.label(),
            TravelDataIndexField.AGE_LIMIT.label(),
            TravelDataIndexField.BOOKING_PLACE.label(),
            TravelDataIndexField.DISCOUNT_INFO_FESTIVAL.label(),
            TravelDataIndexField.EVENT_END_DATE.label(),
            TravelDataIndexField.EVEN_THOMEPAGE.label(),
            TravelDataIndexField.EVENT_PLACE.label(),
            TravelDataIndexField.EVEN_TSTART_DATE.label(),
            TravelDataIndexField.PLACE_INFO.label(),
            TravelDataIndexField.PLAY_TIME.label(),
            TravelDataIndexField.PROGRAM.label(),
            TravelDataIndexField.SPEND_TIME_FESTIVAL.label(),
            TravelDataIndexField.SPONSOR1.label(),
            TravelDataIndexField.SPONSOR1_TEL.label(),
            TravelDataIndexField.SPONSOR2.label(),
            TravelDataIndexField.SPONSOR2_TEL.label(),
            TravelDataIndexField.SUB_EVENT.label(),
            TravelDataIndexField.USE_TIME_FESTIVAL.label(),
            TravelDataIndexField.DISTANCE.label(),
            TravelDataIndexField.INFO_CENTER_TOUR_COURSE.label(),
            TravelDataIndexField.SCHEDULE.label(),
            TravelDataIndexField.TAKE_TIME.label(),
            TravelDataIndexField.THEME.label(),
            TravelDataIndexField.ACCOM_COUNT_LEPORTS.label(),
            TravelDataIndexField.CHK_BABY_CARRIAGE_LEPORTS.label(),
            TravelDataIndexField.CHK_CREDIT_CARD_LEPORTS.label(),
            TravelDataIndexField.CHK_PET_LEPORTS.label(),
            TravelDataIndexField.EXP_AGE_RANGE_LEPORTS.label(),
            TravelDataIndexField.INFO_CENTER_LEPORTS.label(),
            TravelDataIndexField.OPEN_PERIOD.label(),
            TravelDataIndexField.PARKING_FEE_LEPORTS.label(),
            TravelDataIndexField.PARKING_LEPORTS.label(),
            TravelDataIndexField.RESERVATION.label(),
            TravelDataIndexField.REST_DATE_LEPORTS.label(),
            TravelDataIndexField.SCALE_LEPORTS.label(),
            TravelDataIndexField.USE_FEE_LEPORTS.label(),
            TravelDataIndexField.USE_TIME_LEPORTS.label(),
            TravelDataIndexField.ACCOM_COUNT_LODGING.label(),
            TravelDataIndexField.CHECK_IN_TIME.label(),
            TravelDataIndexField.CHECK_OUT_TIME.label(),
            TravelDataIndexField.CHK_COOKING.label(),
            TravelDataIndexField.FOOD_PLACE.label(),
            TravelDataIndexField.HANOK.label(),
            TravelDataIndexField.INFO_CENTER_LODGING.label(),
            TravelDataIndexField.PARKING_LODGING.label(),
            TravelDataIndexField.PICKUP.label(),
            TravelDataIndexField.ROOM_COUNT.label(),
            TravelDataIndexField.RESERVATION_LODGING.label(),
            TravelDataIndexField.RESERVATION_URL.label(),
            TravelDataIndexField.ROOM_TYPE.label(),
            TravelDataIndexField.SCALE_LODGING.label(),
            TravelDataIndexField.SUB_FACILITY.label(),
            TravelDataIndexField.BARBECUE.label(),
            TravelDataIndexField.BEAUTY.label(),
            TravelDataIndexField.BEVERAGE.label(),
            TravelDataIndexField.BICYCLE.label(),
            TravelDataIndexField.CAMPFIRE.label(),
            TravelDataIndexField.FITNESS.label(),
            TravelDataIndexField.KARAOKE.label(),
            TravelDataIndexField.PUBLIC_BATH.label(),
            TravelDataIndexField.PUBLIC_PC.label(),
            TravelDataIndexField.SAUNA.label(),
            TravelDataIndexField.SEMINAR.label(),
            TravelDataIndexField.SPORTS.label(),
            TravelDataIndexField.CHK_BABY_CARRIAGE_SHOPPING.label(),
            TravelDataIndexField.CHK_CREDIT_CARD_SHOPPING.label(),
            TravelDataIndexField.CHK_PET_SHOPPING.label(),
            TravelDataIndexField.CULTURE_CENTER.label(),
            TravelDataIndexField.FAIRDAY.label(),
            TravelDataIndexField.INFO_CENTER_SHOPPING.label(),
            TravelDataIndexField.OPEN_DATE_SHOPPING.label(),
            TravelDataIndexField.OPEN_TIME.label(),
            TravelDataIndexField.PARKING_SHOPPING.label(),
            TravelDataIndexField.REST_DATE_SHOPPING.label(),
            TravelDataIndexField.RESTROOM.label(),
            TravelDataIndexField.SALE_ITEM.label(),
            TravelDataIndexField.SALE_ITEM_COST.label(),
            TravelDataIndexField.SCALE_SHOPPING.label(),
            TravelDataIndexField.SHOP_GUIDE.label(),
            TravelDataIndexField.CHK_CREDIT_CARD_FOOD.label(),
            TravelDataIndexField.DISCOUNT_INFO_FOOD.label(),
            TravelDataIndexField.FIRST_MENU.label(),
            TravelDataIndexField.INFO_CENTER_FOOD.label(),
            TravelDataIndexField.KIDS_FACILITY.label(),
            TravelDataIndexField.OPEN_DATE_FOOD.label(),
            TravelDataIndexField.OPEN_TIME_FOOD.label(),
            TravelDataIndexField.PACKING.label(),
            TravelDataIndexField.PARKING_FOOD.label(),
            TravelDataIndexField.RESERVATION_FOOD.label(),
            TravelDataIndexField.REST_DATE_FOOD.label(),
            TravelDataIndexField.SCALE_FOOD.label(),
            TravelDataIndexField.SEAT.label(),
            TravelDataIndexField.SMOKING.label(),
            TravelDataIndexField.TREAT_MENU.label(),
    };

    public TravelData getLocationDetail(String locationId,
                                        boolean skipSocring) throws Exception {
        // 1. increment click count
        if(!skipSocring) locationStorage.click(locationId);

        SearchResult result = searchingExecutor.search(
                fieldToGetForLocationDetail,
                fieldToSearchForLocationDetail,
                locationId,
                1
        );

        return new TravelData(locationStorage.getSnapshot(locationId), result.mapAt(0));
    }

    //***************************************************************************************
    // Searching location
    //***************************************************************************************
    private final String[] fieldToSearchForSearchingLocations = {
            TravelDataIndexField.TITLE.label(),
            TravelDataIndexField.TITLE_KEYWORDS.label(),
            TravelDataIndexField.OVERVIEW.label(),
            TravelDataIndexField.OVERVIEW_KEYWORDS.label(),
            TravelDataIndexField.ADDR1.label(),
    };
    private final String[] fieldToGetForSearchingLocations = {
            TravelDataIndexField.CONTENT_ID.label(),
            TravelDataIndexField.CONTENT_TYPE_ID.label(),
            TravelDataIndexField.TITLE.label(),
            TravelDataIndexField.TITLE_SHORT.label(),
            TravelDataIndexField.OVERVIEW.label(),
            TravelDataIndexField.OVERVIEW_SHORT.label(),
            TravelDataIndexField.ADDR1.label(),
            TravelDataIndexField.CAT1.label(),
            TravelDataIndexField.AREA_CODE.label(),
            TravelDataIndexField.FIRST_IMAGE.label(),
    };

    public List<TravelData> searchLocations(String keywords,
                                            int resultSize) throws Exception {
        return searchLocations(
                keywords, fieldToSearchForSearchingLocations, fieldToGetForSearchingLocations, topN);
    }

    public List<TravelData> searchLocations(String keywords) throws Exception {
        return searchLocations(keywords, topN);
    }

    private List<TravelData> searchLocations(String keywords,
                                             String[] fieldsToSearch,
                                             String[] fieldsToGet,
                                             int resultSize) throws Exception {
        // 1. search location by keywords
        SearchResult result = searchingExecutor.search(
                fieldsToGet,
                fieldsToSearch,
                keywords,
                resultSize
        );

        // 2. build travel-data list ...
        List<TravelData> travelDataList = new ArrayList<>();
        for(Map<String, String> docMap : result.getDocMapList()) {
            String tmpLocationId = docMap.get(TravelDataIndexField.CONTENT_ID.label());
            if(tmpLocationId == null) continue;

            // 2-1. increment impress count !!
            locationStorage.impress(tmpLocationId);
            travelDataList.add(new TravelData(locationStorage.getSnapshot(tmpLocationId), docMap));
        }

        // 3. sort by score
        Collections.sort(travelDataList);
        return travelDataList;
    }

    //***************************************************************************************
    // Similar location
    //***************************************************************************************
    private final String[] fieldToSearchForSimilarLocations1 = {TravelDataIndexField.CONTENT_ID.label()};
    private final String[] fieldToGetForSimilarLocations1 = {
            TravelDataIndexField.TITLE_KEYWORDS.label(),
            TravelDataIndexField.OVERVIEW_KEYWORDS.label()
    };
    private final String[] fieldToSearchForSimilarLocations2 = {
            TravelDataIndexField.TITLE_KEYWORDS.label(),
            TravelDataIndexField.OVERVIEW_KEYWORDS.label(),
            TravelDataIndexField.ADDR1.label()
    };
    private final String[] fieldToGetForSimilarLocations2 = {
            TravelDataIndexField.CONTENT_ID.label(),
            TravelDataIndexField.CONTENT_TYPE_ID.label(),
            TravelDataIndexField.TITLE.label(),
            TravelDataIndexField.TITLE_SHORT.label(),
            TravelDataIndexField.OVERVIEW.label(),
            TravelDataIndexField.OVERVIEW_SHORT.label(),
            TravelDataIndexField.ADDR1.label(),
            TravelDataIndexField.CAT1.label(),
            TravelDataIndexField.AREA_CODE.label(),
            TravelDataIndexField.FIRST_IMAGE.label(),
    };

    public List<TravelData> getSimilarLocations(String locationId) throws Exception {
        // 1. get keywords data of the location ...
        SearchResult result = searchingExecutor.search(
                fieldToGetForSimilarLocations1,
                fieldToSearchForSimilarLocations1,
                locationId,
                1);

        // 2. make keywords string ...
        Map<String, String> docMap = result.mapAt(0);
        StringBuilder sb = new StringBuilder();
        for(String keywordStr : docMap.values())
            sb.append(keywordStr).append(' ');
        String keywords = sb.toString().trim();

        // 3. search similar location by keywords ...
        result = searchingExecutor.search(
                fieldToGetForSimilarLocations2,
                fieldToSearchForSimilarLocations2,
                keywords,
                similarLocationSize + 1);

        // 4. make travel-data list ...
        List<TravelData> travelDataList = new ArrayList<>();
        List<Map<String, String>> docMapList = result.getDocMapList();
        for(Map<String, String> doc : docMapList) {
            if(travelDataList.size() >= similarLocationSize) break;

            String tmpLocationId = doc.get(TravelDataIndexField.CONTENT_ID.label());
            if(tmpLocationId == null) continue;
            else if(tmpLocationId.equals(locationId)) continue;

            // 4-1. increment impress count !!
            //locationStorage.impress(tmpLocationId);

            IdWeightSnapshot snapshot = locationStorage.getSnapshot(tmpLocationId);
            travelDataList.add(new TravelData(snapshot, doc));
        }

        return travelDataList;
    }

    private final String[] fieldToSearchForShortDistanceLocations1 = {TravelDataIndexField.CONTENT_ID.label()};
    private final String[] fieldToGetForShortDistanceLocations1 = {
            TravelDataIndexField.IN_5KM.label(),
            TravelDataIndexField.IN_10KM.label()
    };
    private final String[] fieldToSearchForShortDistanceLocations2 = {
            TravelDataIndexField.CONTENT_ID.label()
    };
    private final String[] fieldToGetForShortDistanceLocations2 = {
            TravelDataIndexField.CONTENT_ID.label(),
            TravelDataIndexField.CONTENT_TYPE_ID.label(),
            TravelDataIndexField.TITLE.label(),
            TravelDataIndexField.TITLE_SHORT.label(),
            TravelDataIndexField.OVERVIEW.label(),
            TravelDataIndexField.OVERVIEW_SHORT.label(),
            TravelDataIndexField.ADDR1.label(),
            TravelDataIndexField.CAT1.label(),
            TravelDataIndexField.AREA_CODE.label(),
            TravelDataIndexField.FIRST_IMAGE.label(),
    };
    public List<TravelData> getShortDistanceLocations(String locationId) throws Exception {
        // 1. get keywords data of the location ...
        SearchResult result = searchingExecutor.search(
                fieldToGetForShortDistanceLocations1,
                fieldToSearchForShortDistanceLocations1,
                locationId,
                1);

        // 2. make keywords string ...
        Map<String, String> docMap = result.mapAt(0);
        StringBuilder sb = new StringBuilder();
        for(String keywordStr : docMap.values())
            sb.append(keywordStr).append(' ');
        String keywords = sb.toString().trim();

        // 3. search similar location by keywords ...
        result = searchingExecutor.search(
                fieldToGetForShortDistanceLocations2,
                fieldToSearchForShortDistanceLocations2,
                keywords,
                shortDistLocationSize);

        // 4. make travel-data list ...
        List<TravelData> travelDataList = new ArrayList<>();
        List<Map<String, String>> docMapList = result.getDocMapList();
        for(Map<String, String> doc : docMapList) {
            String tmpLocationId = doc.get(TravelDataIndexField.CONTENT_ID.label());

            // 4-1. increment impress count !!
            //locationStorage.impress(tmpLocationId);

            IdWeightSnapshot snapshot = locationStorage.getSnapshot(tmpLocationId);
            travelDataList.add(new TravelData(snapshot, doc));
        }

        return travelDataList;
    }

    //**********************************************************************************
    // Interesting Locations
    //**********************************************************************************
    private final String[] fieldToSearchForInterestingLocations = {
            TravelDataIndexField.TITLE.label(),
            TravelDataIndexField.TITLE_KEYWORDS.label(),
            TravelDataIndexField.OVERVIEW.label(),
            TravelDataIndexField.OVERVIEW_KEYWORDS.label(),
            TravelDataIndexField.ADDR1.label()
    };
    private final String[] fieldToGetForInterestingLocations = {
            TravelDataIndexField.CONTENT_ID.label(),
            TravelDataIndexField.CONTENT_TYPE_ID.label(),
            TravelDataIndexField.TITLE.label(),
            TravelDataIndexField.TITLE_SHORT.label(),
            TravelDataIndexField.OVERVIEW.label(),
            TravelDataIndexField.OVERVIEW_SHORT.label(),
            TravelDataIndexField.ADDR1.label(),
            TravelDataIndexField.CAT1.label(),
            TravelDataIndexField.AREA_CODE.label(),
            TravelDataIndexField.FIRST_IMAGE.label(),
    };
    public List<TravelData> interestingLocations(String keywords) throws Exception {
        return searchLocations(
                keywords, fieldToSearchForInterestingLocations, fieldToGetForInterestingLocations, interestLocationSize);
    }

    //**********************************************************************************
    // ETC
    //**********************************************************************************
    public void applyInertia() {
        locationStorage.applyInertia(inertia);
    }

    public void sortData() {
        locationStorage.sortAndPickTopN(topN);
    }

    public void saveSnapshot() throws Exception {
        locationStorage.saveAllSnapshots(locationSnapshotDir);
    }
}
