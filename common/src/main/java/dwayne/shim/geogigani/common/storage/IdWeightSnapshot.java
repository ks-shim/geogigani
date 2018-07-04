package dwayne.shim.geogigani.common.storage;

import lombok.Data;

@Data
public class IdWeightSnapshot {

    private final String id;

    private final int impressionCount;
    private final double impressionWeight;

    private final int clickCount;
    private final double clickWeight;

    private final double score;
    private final long lastAccessTime;

    public IdWeightSnapshot(String id,
                            int impressionCount,
                            double impressionWeight,
                            int clickCount,
                            double clickWeight,
                            double score,
                            long lastAccessTime) {
        this.id = id;
        this.impressionCount = impressionCount;
        this.impressionWeight = impressionWeight;
        this.clickCount = clickCount;
        this.clickWeight = clickWeight;
        this.score = score;
        this.lastAccessTime = lastAccessTime;
    }

    public IdWeight asIdWeight() {
        return new IdWeight(id, impressionCount, impressionWeight,
                clickCount, clickWeight, score, lastAccessTime);
    }
}
