package dwayne.shim.geogigani.common.data;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.*;

@Data
public class DustData {

    public enum DustStatus {
        GOOD("좋음", "#337ab7", 1),
        NORMAL("보통", "#5cb85c", 0),
        BAD("나쁨", "#f0ad4e", -2),
        VERY_BAD("매우나쁨", "#d9534f", -4);

        private String label;
        private String color;
        private int score;
        private DustStatus(String _label, String _color, int _score) {
            label = _label;
            color = _color;
            score = _score;
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

        public static DustStatus asColor(String... _labels) {
            int totalScore = 0;
            for(String _label : _labels) {
                if(_label == null || _label.isEmpty()) continue;

                DustStatus ds = getDustStatusByLabel(_label);
                totalScore += ds.score;
            }

            for(DustStatus ds : values())
                if(totalScore >= ds.score) return ds;

            return VERY_BAD;
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
