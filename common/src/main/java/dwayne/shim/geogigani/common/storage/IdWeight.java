package dwayne.shim.geogigani.common.storage;

import lombok.Getter;

@Getter
public class IdWeight implements Comparable<IdWeight> {

    private final String id;

    private volatile int impressionCount;
    private volatile double impressionWeight;

    private volatile int clickCount;
    private volatile double clickWeight;

    private volatile double score;
    private volatile long lastAccessTime;

    public IdWeight(String id) {
        this.id = id;
    }

    public IdWeight(String id,
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

    private void updateLastAccessTime() {
        this.lastAccessTime = System.currentTimeMillis();
    }

    public synchronized long getDurationAfterLastAccess() {
        return System.currentTimeMillis() - this.lastAccessTime;
    }

    public synchronized void impress() {
        impressionCount++;
        impressionWeight++;
        updateLastAccessTime();
    }

    public synchronized void click() {
        clickCount++;
        clickWeight++;
        updateLastAccessTime();
    }

    public synchronized void applyInertia(double inertia) {
        impressionWeight *= inertia;
        clickWeight *= inertia;
    }

    public synchronized void calculateScore() {
        this.score = calculateScoreAndGet();
    }

    private double calculateScoreAndGet() {
        return impressionWeight + (clickWeight * 10.0);
    }

    public synchronized IdWeightSnapshot snapshot() {
        return new IdWeightSnapshot(
                id, impressionCount, impressionWeight,
                clickCount, clickWeight, calculateScoreAndGet(), lastAccessTime);
    }

    @Override
    public int compareTo(IdWeight o) {
        return score > o.score ? -1 : score == o.score ? 0 : 1;
    }
}
