package dwayne.shim.geogigani.common.data.statistics;

import lombok.Data;

@Data
public class SessionCountData {

    private String date;
    private volatile int count = 0;
    public SessionCountData() {}
    public SessionCountData(String date) {
        this.date = date;
    }

    public boolean hasError() {
        return date == null || date.trim().isEmpty();
    }
}
