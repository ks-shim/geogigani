package dwayne.shim.geogigani.core.storage;

import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
public class IdWeightStorage {

    private IdWeightSnapshot[] topNSnapshots;
    private final Map<String, IdWeight> idWeightMap;

    private final Object snapshotLock = new Object();

    public IdWeightStorage() {
        this.topNSnapshots = new IdWeightSnapshot[0];
        this.idWeightMap = new ConcurrentHashMap<>();
    }

    public void addIdWeight(IdWeight iw) {
        idWeightMap.put(iw.getId(), iw);
    }

    public void impress(String id) {
        IdWeight iw = idWeightMap.get(id);
        if(iw == null) return;

        iw.impress();
    }

    public void click(String id) {
        IdWeight iw = idWeightMap.get(id);
        if(iw == null) return;

        iw.click();
    }

    public void sortAndPickTopN(int topN) {
        List<IdWeight> newList = new ArrayList<>();
        for(IdWeight iw : idWeightMap.values()) {
            newList.add(iw);
            iw.calculateScore();
        }

        Collections.sort(newList);

        synchronized (snapshotLock) {
            topNSnapshots = newList.toArray(new IdWeightSnapshot[topN]);
        }
    }

    public IdWeightSnapshot[] getTopNIdWeights() {
        synchronized (snapshotLock) {
            return topNSnapshots;
        }
    }
}
