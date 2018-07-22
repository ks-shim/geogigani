package dwayne.shim.geogigani.front.service.service;

import dwayne.shim.geogigani.common.code.ContentTypeIdCode;
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

    @Value("${rest.interest}")
    private String restInterest;

    @Autowired
    private RestTemplate restTemplate;

    public Map<String, List<Map<String, String>>> getPopularDestinations() {
        TravelData[] result = restTemplate.getForObject(restPopular, TravelData[].class);
        return asCategorizedMapList(result);
    }

    public Map<String, String> getDestinationDetail(String destId,
                                                    String userId,
                                                    boolean skipSocring) {
        String url = restDetail + '/' + destId + ((userId == null || userId.isEmpty()) ? "" : "?userId=" + userId + "&skipScoring=" + skipSocring);
        TravelData result = restTemplate.getForObject(url, TravelData.class);
        return asMap(result);
    }

    public Map<String, List<Map<String, String>>> getSimilarDestinations(String destId) {
        TravelData[] result = restTemplate.getForObject(restSimilar + '/' + destId, TravelData[].class);
        return asCategorizedMapList(result);
    }

    public Map<String, List<Map<String, String>>> getShortDistanceDestinations(String destId) {
        TravelData[] result = restTemplate.getForObject(restShortDistance + '/' + destId, TravelData[].class);
        return asCategorizedMapList(result);
    }

    public Map<String, List<Map<String, String>>> searchDestinations(String keywords) {
        TravelData[] result = restTemplate.getForObject(restSearch + "?keywords=" + keywords, TravelData[].class);
        return asCategorizedMapList(result);
    }

    public Map<String, List<Map<String, String>>> getInterestingDestinations(String userId) {
        if(userId == null || userId.trim().isEmpty()) return new HashMap<>();
        
        TravelData[] result = restTemplate.getForObject(restInterest + "/" + userId, TravelData[].class);
        return asCategorizedMapList(result);
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

    private Map<String, List<Map<String, String>>> asCategorizedMapList(TravelData[] travelDatas) {
        return asCategorizedMapList(travelDatas, new HashMap<>());
    }

    private Map<String, List<Map<String, String>>> asCategorizedMapList(TravelData[] travelDatas,
                                                                        Map<String, List<Map<String, String>>> categoryMap) {
        for(TravelData td : travelDatas) {
            Map<String, String> docMap = asMap(td);
            String contentTypeId = docMap.get(DestinationInfoField.CONTENT_TYPE_ID.label());
            if(contentTypeId == null) continue;

            String categoryLabel = null;
            try {
                categoryLabel = ContentTypeIdCode.getTypeIdCode(contentTypeId).label();
            } catch (Exception e) {
                continue;
            }

            List<Map<String, String>> docMapList = categoryMap.get(categoryLabel);
            if(docMapList == null) {
                docMapList = new ArrayList<>();
                categoryMap.put(categoryLabel, docMapList);
            }

            docMapList.add(docMap);
        }

        return categoryMap;
    }
}
