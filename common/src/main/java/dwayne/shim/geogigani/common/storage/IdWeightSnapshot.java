package dwayne.shim.geogigani.common.storage;

import lombok.Data;

import java.io.Serializable;

@Data
public class IdWeightSnapshot implements Serializable {

    private String id;

    private int impressionCount;
    private double impressionWeight;

    private int clickCount;
    private double clickWeight;

    private double score;
    private long lastAccessTime;

    public IdWeightSnapshot() {}

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
