package dwayne.shim.geogigani.front.service.model;

import lombok.Data;

import java.util.*;

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
        this.destListMap = new TreeMap<>();
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


