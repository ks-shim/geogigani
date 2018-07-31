package dwayne.shim.geogigani.front.service.service;

import dwayne.shim.geogigani.common.code.AreaCode;
import dwayne.shim.geogigani.common.data.TravelData;
import dwayne.shim.geogigani.common.storage.IdWeightSnapshot;
import dwayne.shim.geogigani.front.service.constants.DestinationInfoField;
import dwayne.shim.geogigani.front.service.model.IdFreq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class FrontStatisticsService {

    @Value("${rest.popular}")
    private String restPopular;

    @Autowired
    private RestTemplate restTemplate;

    public List<IdFreq> getAreaStatistics() {
        // 1. get TravelData from server ..
        TravelData[] result = restTemplate.getForObject(restPopular, TravelData[].class);

        // 2. make TravelData as id-freq pairs ...
        List<IdFreq> idFreqList = asIdFreqList(result, DestinationInfoField.AREA_LABEL);

        // 3. sort
        Collections.sort(idFreqList);

        return idFreqList;
    }

    private List<IdFreq> asIdFreqList(TravelData[] travelDatas,
                                      DestinationInfoField keyField) {

        Map<String, IdFreq> idFreqMap = new HashMap<>();
        for (TravelData td : travelDatas) {
            Map<String, String> tmpMap = asMap(td);
            String key = tmpMap.get(keyField.label());
            if (key == null) continue;

            IdFreq idFreq = idFreqMap.get(key);
            if (idFreq == null) {
                idFreq = new IdFreq(key);
                idFreqMap.put(key, idFreq);
            }
            idFreq.incrementFreq();
        }

        return new ArrayList<>(idFreqMap.values());
    }

    private Map<String, String> asMap(TravelData td) {
        Map<String, String> map = new HashMap<>();

        // 1. get info from map
        map.putAll(td.getInfoMap());

        // 2. get info from snapshot
        IdWeightSnapshot snapshot = td.getIdWeightSnapshot();
        map.put(DestinationInfoField.IMPRESSION_COUNT.label(), String.valueOf(snapshot.getImpressionCount()));
        map.put(DestinationInfoField.CLICK_COUNT.label(), String.valueOf(snapshot.getClickCount()));
        map.put(DestinationInfoField.SCORE_SNAPSHOT.label(), String.valueOf(snapshot.getScore()));

        String areaCode = map.get(DestinationInfoField.AREA_CODE.label());
        if (areaCode == null || areaCode.trim().isEmpty()) return map;

        try {
            map.put(DestinationInfoField.AREA_LABEL.label(), AreaCode.getAreaCode(areaCode).label());
        } catch (Exception e) {
            //ignore}
        }

        return map;
    }
}

