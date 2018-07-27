package dwayne.shim.geogigani.front.service.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Destination2DepthInfo {

    private String key;
    private int seq;
    private Map<String, List<Map<String, String>>> destListMap;
    private int totalSize;

    public Destination2DepthInfo(String key,
                                 int seq) {
        this.key = key;
        this.seq = seq;
        this.destListMap = new HashMap<>();
    }

    public void add(String subKey,
                    Map<String, String> destMap) {
        List<Map<String, String>> destMapList = destListMap.get(subKey);
        if(destMapList == null) {
            destMapList = new ArrayList<>();
            destListMap.put(subKey, destMapList);
        }

        totalSize++;
        destMapList.add(destMap);
    }
}


