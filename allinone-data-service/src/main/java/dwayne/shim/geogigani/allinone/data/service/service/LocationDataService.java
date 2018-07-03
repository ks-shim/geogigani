package dwayne.shim.geogigani.allinone.data.service.service;

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

    public void applyInertia() {
        locationStorage.applyInertia(inertia);
    }

    public void sortData() {
        locationStorage.sortAndPickTopN(topN);
    }

    /*public void impress(String id) {
        locationStorage.impress(id);
    }

    public void click(String id) {
        locationStorage.impress(id);
    }*/
}
