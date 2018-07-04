package dwayne.shim.geogigani.common.data;

import dwayne.shim.geogigani.common.searching.LuceneResultField;
import dwayne.shim.geogigani.common.storage.IdWeightSnapshot;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class TravelData implements Comparable<TravelData> {

    private final IdWeightSnapshot idWeightSnapshot;
    private final Map<String, String> infoMap;

    public TravelData(IdWeightSnapshot idWeightSnapshot,
                      Map<String, String> infoMap) {
        this.idWeightSnapshot = idWeightSnapshot;
        this.infoMap = infoMap;
    }

    public static TravelData dummyTravelData(String locationId) {
        return new TravelData(
                new IdWeightSnapshot(locationId, 0,0,0,0,0,System.currentTimeMillis()),
                new HashMap<>());
    }

    private float getLuceneScore() {
        String scoreStr = infoMap.get(LuceneResultField.SCORE.label());
        if(scoreStr == null) return 0.0f;

        return Float.valueOf(scoreStr);
    }

    @Override
    public int compareTo(TravelData o) {
        double selfScore = getLuceneScore() + Math.log10(idWeightSnapshot.getScore()) * 5;
        double compScore = o.getLuceneScore() + Math.log10(o.idWeightSnapshot.getScore()) * 5;
        return selfScore > compScore ? -1 : selfScore == compScore ? 0 : 1;
    }
}
