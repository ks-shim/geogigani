package dwayne.shim.geogigani.front.service.model;

import lombok.Data;

import java.util.*;

@Data
public class Destination1DepthInfo {

    private String key;
    private int seq;
    private List<Map<String, String>> destMapList;
    private int totalSize;

    public Destination1DepthInfo(String key,
                                 int seq) {
        this.key = key;
        this.seq = seq;
        this.destMapList = new ArrayList<>();
    }

    public void add(Map<String, String> destMap) {
        totalSize++;
        destMapList.add(destMap);
    }
}
