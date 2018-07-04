package dwayne.shim.geogigani.common.data;

import dwayne.shim.geogigani.common.storage.IdWeightSnapshot;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class TravelData {

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
}
