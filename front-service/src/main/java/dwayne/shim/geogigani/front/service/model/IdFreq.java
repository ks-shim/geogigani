package dwayne.shim.geogigani.front.service.model;

import lombok.Data;

@Data
public class IdFreq implements Comparable<IdFreq> {

    private String id;
    private double freq;

    public IdFreq() {}
    public IdFreq(String id) {
        this.id = id;
    }

    public void incrementFreq() {
        freq++;
    }

    @Override
    public int compareTo(IdFreq o) {
        return this.freq > o.freq ? -1 : (this.freq == o.freq) ? 0 : 1;
    }
}
