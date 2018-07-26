package dwayne.shim.geogigani.common.data;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.*;

@Data
public class DustData {

    public enum DustStatus {
        GOOD("좋음", "#337ab7"),
        NORMAL("보통", "#5cb85c"),
        BAD("나쁨", "#f0ad4e"),
        VERY_BAD("매우나쁨", "#d9534f");

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

    public DustData(Date date) {
        this.dateTime = todayAsString(date);
        this.regionDustDataList = new ArrayList<>();
        this.dustImagesMap = new HashMap<>();
    }

    public DustData(String dateTime) {
        this.dateTime = dateTime;
        this.regionDustDataList = new ArrayList<>();
        this.dustImagesMap = new HashMap<>();
    }

    public boolean isEmpty() {
        return regionDustDataList.size() == 0;
    }

    public static String todayAsString(Date today) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(today);
    }

    public void addRegionDustData(Map<String, String> regionDustData) {
        regionDustDataList.add(regionDustData);
    }

    public void addDustImageListMap(String dustName,
                                    Map<String, String> dustImages) {
        dustImagesMap.put(dustName, dustImages);
    }

    public void sortByRegion() {

    }
}
