package dwayne.shim.geogigani.common.data;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class DustData {

    public enum DustStatus {
        GOOD("좋음", "blue"),
        NORMAL("보통", "green"),
        BAD("나쁨", "orange"),
        VERY_BAD("매우나쁨", "red");

        private String label;
        private String color;
        private DustStatus(String _label, String _color) {
            label = _label;
            color = _color;
        }

        public String label() {
            return label;
        }

        public String color() {
            return color;
        }

        public static DustStatus getDustStatusByLabel(String _label) {
            for(DustStatus ds : values()) {
                if(ds.label.equals(_label)) return ds;
            }

            throw new IllegalStateException(_label + " isn't valid status.");
        }
    }

    private String dateTime;
    private List<Map<String, String>> regionDustDataList;
    private Map<String, Map<String, String>> dustImagesMap;

    public DustData() {}

    public DustData(String dateTime) {
        this.dateTime = dateTime;
        this.regionDustDataList = new ArrayList<>();
        this.dustImagesMap = new HashMap<>();
    }

    public void addRegionDustData(Map<String, String> regionDustData) {
        regionDustDataList.add(regionDustData);
    }

    public void addDustImageListMap(String dustName,
                                    Map<String, String> dustImages) {
        dustImagesMap.put(dustName, dustImages);
    }
}
