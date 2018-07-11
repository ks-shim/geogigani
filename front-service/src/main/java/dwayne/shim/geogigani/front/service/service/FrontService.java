package dwayne.shim.geogigani.front.service.service;

import dwayne.shim.geogigani.common.data.TravelData;
import dwayne.shim.geogigani.common.storage.IdWeightSnapshot;
import dwayne.shim.geogigani.front.service.constants.DestinationInfoField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${rest.short-dist}")
    private String restShortDistance;

    @Autowired
    private RestTemplate restTemplate;

    public List<Map<String, String>> getPopularDestinations() {
        TravelData[] result = restTemplate.getForObject(restPopular, TravelData[].class);
        return asMapList(result);
    }

    public Map<String, String> getDestinationDetail(String destId) {
        TravelData result = restTemplate.getForObject(restDetail + '/' + destId, TravelData.class);
        return asMap(result);
    }

    public List<Map<String, String>> getSimilarDestinations(String destId) {
        TravelData[] result = restTemplate.getForObject(restSimilar + '/' + destId, TravelData[].class);
        return asMapList(result);
    }

    public List<Map<String, String>> getShortDistanceDestinations(String destId) {
        TravelData[] result = restTemplate.getForObject(restShortDistance + '/' + destId, TravelData[].class);
        return asMapList(result);
    }

    public List<Map<String, String>> searchDestinations(String keywords) {
        TravelData[] result = restTemplate.getForObject(restSearch + "?keywords=" + keywords, TravelData[].class);
        return asMapList(result);
    }

    //****************************************************************************
    // Common methods ...
    //****************************************************************************
    private List<Map<String, String>> asMapList(TravelData[] travelDatas) {
        List<Map<String, String>> mapList = new ArrayList<>();
        for(TravelData td : travelDatas)
            mapList.add(asMap(td));

        return mapList;
    }

    private Map<String, String> asMap(TravelData td) {
        Map<String, String> map = new HashMap<>();

        // 1. get info from snapshot
        IdWeightSnapshot snapshot = td.getIdWeightSnapshot();
        map.put(DestinationInfoField.IMPRESSION_COUNT.label(), String.valueOf(snapshot.getImpressionCount()));
        map.put(DestinationInfoField.CLICK_COUNT.label(), String.valueOf(snapshot.getClickCount()));
        map.put(DestinationInfoField.SCORE.label(), String.valueOf(snapshot.getScore()));

        // 2. get info from map
        map.putAll(td.getInfoMap());

        return map;
    }
}
