package dwayne.shim.geogigani.allinone.data.service.service;

import dwayne.shim.geogigani.common.data.TravelData;
import dwayne.shim.geogigani.common.indexing.TravelDataIndexField;
import dwayne.shim.geogigani.core.storage.IdWeightSnapshot;
import dwayne.shim.geogigani.core.storage.IdWeightStorage;
import dwayne.shim.geogigani.searching.SearchResult;
import dwayne.shim.geogigani.searching.SearchingExecutor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    private final String[] fieldToSearchForPopularLocations = {TravelDataIndexField.CONTENT_ID.label()};
    private final String[] fieldToGetForPopularLocations = {
            TravelDataIndexField.CONTENT_ID.label(),
            TravelDataIndexField.CONTENT_TYPE_ID.label(),
            TravelDataIndexField.TITLE.label(),
            TravelDataIndexField.OVERVIEW.label(),
            TravelDataIndexField.ADDR1.label(),
            TravelDataIndexField.FIRST_IMAGE.label(),
    };

    public List<Map<String, String>> getPopularLocation() throws Exception {
        IdWeightSnapshot[] popularLocations = locationStorage.getTopNIdWeights();

        StringBuilder sb = new StringBuilder();
        for(IdWeightSnapshot location : popularLocations) {
            // 1. increment impress count  ...
            locationStorage.impress(location.getId());
            sb.append(location.getId()).append(' ');
        }

        SearchResult result = searchingExecutor.search(
                fieldToGetForPopularLocations,
                fieldToSearchForPopularLocations,
                sb.toString().trim(),
                topN
        );

        return result.getDocMapList();
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
