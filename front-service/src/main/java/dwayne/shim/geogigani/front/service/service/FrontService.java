package dwayne.shim.geogigani.front.service.service;

import dwayne.shim.geogigani.common.data.TravelData;
import dwayne.shim.geogigani.common.storage.IdWeightSnapshot;
import dwayne.shim.geogigani.front.service.constants.DetinationInfoField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FrontService {

    @Value("${rest.popular}")
    private String restPopular;

    @Value("${rest.detail}")
    private String restDetail;

    @Value("${rest.search}")
    private String restSearch;

    @Value("${rest.similar}")
    private String restSimilar;

    @Autowired
    private RestTemplate restTemplate;

    public List<Map<String, String>> getPopularLocations() {
        TravelData[] result = restTemplate.getForObject(restPopular, TravelData[].class);
        return asMapList(result);
    }

    private List<Map<String, String>> asMapList(TravelData[] travelDatas) {
        List<Map<String, String>> mapList = new ArrayList<>();
        for(TravelData td : travelDatas) {
            Map<String, String> map = new HashMap<>();
            mapList.add(map);

            // 1. get info from snapshot
            IdWeightSnapshot snapshot = td.getIdWeightSnapshot();
            map.put(DetinationInfoField.IMPRESSION_COUNT.label(), String.valueOf(snapshot.getImpressionCount()));
            map.put(DetinationInfoField.CLICK_COUNT.label(), String.valueOf(snapshot.getClickCount()));
            map.put(DetinationInfoField.SCORE.label(), String.valueOf(snapshot.getScore()));

            // 2. get info from map
            map.putAll(td.getInfoMap());
        }

        return mapList;
    }
}
