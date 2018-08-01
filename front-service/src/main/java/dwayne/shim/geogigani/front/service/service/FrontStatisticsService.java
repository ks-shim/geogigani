package dwayne.shim.geogigani.front.service.service;

import dwayne.shim.geogigani.common.code.AreaCode;
import dwayne.shim.geogigani.common.code.ContentTypeIdCode;
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

    public void getAreaAndContentTypeStatistics(List<IdFreq> areaIdFreqList,
                                                List<IdFreq> contentTypeFreqList) {
        // 1. get TravelData from server ..
        TravelData[] result = restTemplate.getForObject(restPopular, TravelData[].class);
        getAreaStatistics(result, areaIdFreqList);
        getContentTypeStatistics(result, contentTypeFreqList);
    }

    public void getAreaStatistics(TravelData[] result,
                                  List<IdFreq> idFreqList) {
        // 1. make TravelData as id-freq pairs ...
        asIdFreqList(result, idFreqList, DestinationInfoField.AREA_LABEL);

        // 2. sort
        Collections.sort(idFreqList);
    }

    public void getContentTypeStatistics(TravelData[] result,
                                         List<IdFreq> idFreqList) {
        // 1. make TravelData as id-freq pairs ...
        asIdFreqList(result, idFreqList, DestinationInfoField.CONTENT_TYPE_LABEL);

        // 2. sort
        Collections.sort(idFreqList);
    }

    private void asIdFreqList(TravelData[] travelDatas,
                                      List<IdFreq> idFreqList,
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

        idFreqList.addAll(idFreqMap.values());
    }

    private final DestinationInfoField[][] newFields = {
            {DestinationInfoField.AREA_CODE, DestinationInfoField.AREA_LABEL},
            {DestinationInfoField.CONTENT_TYPE_ID, DestinationInfoField.CONTENT_TYPE_LABEL}
    };

    private final LabelFinder[] labelFinders = {
            id -> AreaCode.getAreaCode(id).label(),
            id -> ContentTypeIdCode.getTypeIdCode(id).label()
    };

    @FunctionalInterface
    public interface LabelFinder {
        String asLabel(String id);
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

        // 3. find labels of the designated fields ...
        for(int i=0; i<newFields.length; i++) {
            DestinationInfoField[] idLabelFields = newFields[i];
            String id = map.get(idLabelFields[0].label());
            if(id == null || id.trim().isEmpty()) continue;

            try {
                map.put(idLabelFields[1].label(), labelFinders[i].asLabel(id));
            } catch (Exception e) {
                //ignore
                continue;
            }
        }

        return map;
    }
}

