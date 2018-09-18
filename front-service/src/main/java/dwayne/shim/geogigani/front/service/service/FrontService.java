package dwayne.shim.geogigani.front.service.service;

import dwayne.shim.geogigani.common.code.AreaCode;
import dwayne.shim.geogigani.common.code.ContentTypeIdCode;
import dwayne.shim.geogigani.common.data.DustData;
import dwayne.shim.geogigani.common.data.TravelData;
import dwayne.shim.geogigani.common.storage.IdWeightSnapshot;
import dwayne.shim.geogigani.front.service.constants.DestinationInfoField;
import dwayne.shim.geogigani.front.service.model.Destination1DepthInfo;
import dwayne.shim.geogigani.front.service.model.Destination2DepthInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class FrontService {

    @Value("${rest.popular}")
    private String restPopular;

    @Value("${rest.detail}")
    private String restDetail;

    @Value("${rest.blog}")
    private String restBlog;

    @Value("${rest.youtube}")
    private String restYouTube;

    @Value("${rest.search}")
    private String restSearch;

    @Value("${rest.similar}")
    private String restSimilar;

    @Value("${rest.short-dist}")
    private String restShortDistance;

    @Value("${rest.short-dist-from-me}")
    private String restShortDistanceFromMe;

    @Value("${rest.interest}")
    private String restInterest;

    @Value("${rest.dust}")
    private String restDust;

    @Value("${rest.area}")
    private String restArea;

    @Autowired
    private RestTemplate restTemplate;

    public List<Destination2DepthInfo> getPopularDestinations() {
        TravelData[] result = restTemplate.getForObject(restPopular, TravelData[].class);
        return asCategorized2DepthDestInfo(result);
    }

    public Map<String, String> getDestinationDetail(String destId,
                                                    String userId,
                                                    boolean skipScoring) {
        String url = restDetail + '/' + destId + ((userId == null || userId.isEmpty()) ? "" : "?userId=" + userId + "&skipScoring=" + skipScoring);
        TravelData result = restTemplate.getForObject(url, TravelData.class);
        return asMap(result);
    }

    public List<Map<String, String>> getDestinationBlog(String destId) {
        String url = restBlog + '/' + destId;
        List<Map<String, String>> result = restTemplate.getForObject(url, List.class);
        editLink(result);
        return result;
    }

    public List<Map<String, String>> getDestinationYouTube(String destId) {
        String url = restYouTube + '/' + destId;
        List<Map<String, String>> result = restTemplate.getForObject(url, List.class);
        return result;
    }

    private void editLink(List<Map<String, String>> blogList) {
        for(Map<String, String> blog : blogList) {
            String link = blog.get("link");
            if(link == null) continue;

            blog.put("link", link.replace("?Redirect=Log&amp;logNo=", "/"));
        }
    }

    public List<Destination2DepthInfo> getSimilarDestinations(String destId) {
        TravelData[] result = restTemplate.getForObject(restSimilar + '/' + destId, TravelData[].class);
        return asCategorized2DepthDestInfo(result);
    }

    public List<Destination2DepthInfo> getShortDistanceDestinations(String destId) {
        TravelData[] result = restTemplate.getForObject(restShortDistance + '/' + destId, TravelData[].class);
        return asCategorized2DepthDestInfo(result);
    }

    public List<Destination2DepthInfo> getShortDistanceDestinationsFromMe(String latitude, String longitude) {
        TravelData[] result = restTemplate.getForObject(restShortDistanceFromMe + '/' + latitude + '/' + longitude, TravelData[].class);
        return asCategorized2DepthDestInfo(result);
    }

    public List<Destination2DepthInfo> searchDestinations(String keywords) {
        TravelData[] result = restTemplate.getForObject(restSearch + "?keywords=" + keywords, TravelData[].class);
        return asCategorized2DepthDestInfo(result);
    }

    public List<Destination2DepthInfo> getInterestingDestinations(String userId) {
        if(userId == null || userId.trim().isEmpty()) return new ArrayList<>();
        
        TravelData[] result = restTemplate.getForObject(restInterest + "/" + userId, TravelData[].class);
        return asCategorized2DepthDestInfo(result);
    }

    public DustData getDustData() {
        DustData dustData = restTemplate.getForObject(restDust, DustData.class);
        return dustData;
    }

    public List<Destination1DepthInfo> getDestinationsByAreacode(String areaCode) {
        if(areaCode == null || areaCode.trim().isEmpty()) return new ArrayList<>();

        TravelData[] result = restTemplate.getForObject(restArea + "/" + areaCode, TravelData[].class);
        return asCategorized1DepthDestInfo(result);
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

        Map<String, String> infoMap = td.getInfoMap();
        if(infoMap.isEmpty()) return map;

        // 1. get info from snapshot
        IdWeightSnapshot snapshot = td.getIdWeightSnapshot();
        map.put(DestinationInfoField.IMPRESSION_COUNT.label(), String.valueOf(snapshot.getImpressionCount()));
        map.put(DestinationInfoField.CLICK_COUNT.label(), String.valueOf(snapshot.getClickCount()));
        map.put(DestinationInfoField.SCORE.label(), String.valueOf(snapshot.getScore()));

        // 2. get info from map
        map.putAll(infoMap);

        return map;
    }

    private List<Destination2DepthInfo> asCategorized2DepthDestInfo(TravelData[] travelDatas) {

        Map<String, Destination2DepthInfo> keyDestMap = new HashMap<>();

        int seq = 0;
        for(TravelData td : travelDatas) {
            td.regularizeDistance();

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

            Destination2DepthInfo destInfo = keyDestMap.get(contentTypeLabel);
            if(destInfo == null) {
                destInfo = new Destination2DepthInfo(contentTypeLabel, ++seq);
                keyDestMap.put(contentTypeLabel, destInfo);
            }

            // 2. categorizing by area
            destInfo.add(areaLabel, docMap);
        }

        return new ArrayList<>(keyDestMap.values());
    }

    private List<Destination1DepthInfo> asCategorized1DepthDestInfo(TravelData[] travelDatas) {

        Map<String, Destination1DepthInfo> keyDestMap = new TreeMap<>();

        int seq = 0;
        for(TravelData td : travelDatas) {
            Map<String, String> docMap = asMap(td);

            // 1. categorizing by content-type
            String contentTypeId = docMap.get(DestinationInfoField.CONTENT_TYPE_ID.label());
            if(contentTypeId == null) continue;

            String contentTypeLabel = null;
            try {
                contentTypeLabel = ContentTypeIdCode.getTypeIdCode(contentTypeId).label();
            } catch (Exception e) {
                continue;
            }

            Destination1DepthInfo destInfo = keyDestMap.get(contentTypeLabel);
            if(destInfo == null) {
                destInfo = new Destination1DepthInfo(contentTypeLabel, ++seq);
                keyDestMap.put(contentTypeLabel, destInfo);
            }

            // 2. categorizing by area
            destInfo.add(docMap);
        }

        return new ArrayList<>(keyDestMap.values());
    }
}
