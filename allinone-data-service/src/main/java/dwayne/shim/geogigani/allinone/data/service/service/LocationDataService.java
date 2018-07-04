package dwayne.shim.geogigani.allinone.data.service.service;

import com.google.common.cache.Cache;
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
import java.util.List;
import java.util.Map;

@Log4j2
@Service
public class LocationDataService {

    @Value("${location.inertia}")
    private double inertia;

    @Value("${location.topn}")
    private int topN;

    @Autowired
    private IdWeightStorage locationStorage;

    @Autowired
    private SearchingExecutor searchingExecutor;

    private final LRUMap<Object, List<TravelData>> cache;
    public LocationDataService() {
        cache = new LRUMap(2);
    }

    private final String[] fieldToSearchForPopularLocations = {TravelDataIndexField.CONTENT_ID.label()};
    private final String[] fieldToGetForPopularLocations = {
            TravelDataIndexField.CONTENT_ID.label(),
            TravelDataIndexField.CONTENT_TYPE_ID.label(),
            TravelDataIndexField.TITLE.label(),
            TravelDataIndexField.OVERVIEW.label(),
            TravelDataIndexField.ADDR1.label(),
            TravelDataIndexField.FIRST_IMAGE.label(),
    };

    public List<TravelData> getPopularLocation() throws Exception {
        // 1. get topN id-weights
        IdWeightSnapshot[] popularLocations = locationStorage.getTopNIdWeights();

        // 2. build key to search
        StringBuilder sb = new StringBuilder();
        for(IdWeightSnapshot location : popularLocations) {
            // 2-1. increment impress count  ...
            locationStorage.impress(location.getId());
            // 2-2. append keyword
            sb.append(location.getId()).append(' ');
        }
        String ids = sb.toString().trim();

        // 3. If cached data exists, return it right away !!
        List<TravelData> cachedTravelData = cache.get(popularLocations);
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
        cache.put(popularLocations, travelDataList);
        return travelDataList;
    }

    private final String[] fieldToSearchForLocationDetail = {TravelDataIndexField.CONTENT_ID.label()};
    private final String[] fieldToGetForLocationDetail = {
            TravelDataIndexField.CONTENT_ID.label(),
            TravelDataIndexField.CONTENT_TYPE_ID.label(),
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
    };

    public Map<String, String> getLocationDetail(String locationId) throws Exception {
        // 1. increment click count
        locationStorage.click(locationId);

        SearchResult result = searchingExecutor.search(
                fieldToGetForPopularLocations,
                fieldToSearchForPopularLocations,
                locationId,
                1
        );

        return result.getDocMapList().get(0);
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
}
