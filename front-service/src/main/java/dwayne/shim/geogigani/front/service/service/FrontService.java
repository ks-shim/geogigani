package dwayne.shim.geogigani.front.service.service;

import dwayne.shim.geogigani.common.code.AreaCode;
import dwayne.shim.geogigani.common.code.ContentTypeIdCode;
import dwayne.shim.geogigani.common.data.TravelData;
import dwayne.shim.geogigani.common.storage.IdWeightSnapshot;
import dwayne.shim.geogigani.front.service.constants.DestinationInfoField;
import dwayne.shim.geogigani.front.service.model.DestinationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.Null;
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

    public List<DestinationInfo> getPopularDestinations() {
        TravelData[] result = restTemplate.getForObject(restPopular, TravelData[].class);
        return asCategorizedDestInfo(result);
    }

    public Map<String, String> getDestinationDetail(String destId,
                                                    String userId,
                                                    boolean skipScoring) {
        String url = restDetail + '/' + destId + ((userId == null || userId.isEmpty()) ? "" : "?userId=" + userId + "&skipScoring=" + skipScoring);
        TravelData result = restTemplate.getForObject(url, TravelData.class);
        return asMap(result);
    }

    public List<DestinationInfo> getSimilarDestinations(String destId) {
        TravelData[] result = restTemplate.getForObject(restSimilar + '/' + destId, TravelData[].class);
        return asCategorizedDestInfo(result);
    }

    public List<DestinationInfo> getShortDistanceDestinations(String destId) {
        TravelData[] result = restTemplate.getForObject(restShortDistance + '/' + destId, TravelData[].class);
        return asCategorizedDestInfo(result);
    }

    public List<DestinationInfo> searchDestinations(String keywords) {
        TravelData[] result = restTemplate.getForObject(restSearch + "?keywords=" + keywords, TravelData[].class);
        return asCategorizedDestInfo(result);
    }

    public List<DestinationInfo> getInterestingDestinations(String userId) {
        if(userId == null || userId.trim().isEmpty()) return new ArrayList<>();
        
        TravelData[] result = restTemplate.getForObject(restInterest + "/" + userId, TravelData[].class);
        return asCategorizedDestInfo(result);
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

    private List<DestinationInfo> asCategorizedDestInfo(TravelData[] travelDatas) {

        Map<String, DestinationInfo> keyDestMap = new HashMap<>();

        int seq = 0;
        for(TravelData td : travelDatas) {
            Map<String, String> docMap = asMap(td);

            // 1. categorizing by content-type
            String contentTypeId = docMap.get(DestinationInfoField.CONTENT_TYPE_ID.label());
            String areaCode = docMap.get(DestinationInfoField.AREA_CODE.label());
            if(contentTypeId == null || areaCode == null) continue;

            String contentTypeLabel = null;
            String areaLabel = null;
            try {
                contentTypeLabel = ContentTypeIdCode.getTypeIdCode(contentTypeId).label();
                areaLabel = AreaCode.getAreaCode(areaCode).label();
            } catch (Exception e) {
                continue;
            }

            DestinationInfo destInfo = keyDestMap.get(contentTypeLabel);
            if(destInfo == null) {
                destInfo = new DestinationInfo(contentTypeLabel, ++seq);
                keyDestMap.put(contentTypeLabel, destInfo);
            }

            // 2. categorizing by area
            destInfo.add(areaLabel, docMap);
        }

        return new ArrayList<>(keyDestMap.values());
    }
}
