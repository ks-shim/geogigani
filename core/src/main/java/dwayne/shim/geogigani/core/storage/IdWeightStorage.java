package dwayne.shim.geogigani.core.storage;

import dwayne.shim.geogigani.common.storage.IdWeight;
import dwayne.shim.geogigani.common.storage.IdWeightSnapshot;
import lombok.extern.log4j.Log4j2;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
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

    //******************************************************************
    // Initialization related methods ...
    //******************************************************************
    public void addIdWeight(String id) {
        addIdWeight(new IdWeight(id));
    }

    public void addIdWeight(IdWeight iw) {
        idWeightMap.put(iw.getId(), iw);
    }

    //******************************************************************
    // Scoring related methods ...
    //******************************************************************
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

    //******************************************************************
    // topN related methods ...
    //******************************************************************
    public void sortAndPickTopN(int topN) {
        List<IdWeight> newList = new ArrayList<>();
        for(IdWeight iw : idWeightMap.values()) {
            newList.add(iw);
            iw.calculateScore();
        }

        Collections.sort(newList);

        if(topN > newList.size()) topN = newList.size();
        synchronized (snapshotLock) {
            IdWeightSnapshot[] snapshots = new IdWeightSnapshot[topN];
            for(int i=0; i<topN; i++)
                snapshots[i] = newList.get(i).snapshot();
            topNSnapshots = snapshots;
        }
    }

    public IdWeightSnapshot[] getTopNIdWeights() {
        synchronized (snapshotLock) {
            return topNSnapshots;
        }
    }

    //******************************************************************
    // save & read snapshots related methods ...
    //******************************************************************
    public IdWeightSnapshot getSnapshot(String id) {
        return idWeightMap.get(id).snapshot();
    }

    public void readAllSnapshots(String inDirLocation) throws Exception {
        readAllSnapshots(new ObjectMapper(), inDirLocation);
    }

    public void readAllSnapshots(ObjectMapper mapper,
                                 String inDirLocation) throws Exception {
        File[] files = new File(inDirLocation).listFiles();
        for(File file : files) {
            if(file.isDirectory()) continue;

            IdWeightSnapshot snapshot = mapper.readValue(file, IdWeightSnapshot.class);
            addIdWeight(snapshot.asIdWeight());
        }
    }

    public void saveAllSnapshots(String outDirLocation) throws Exception {
        saveAllSnapshots(new ObjectMapper(), outDirLocation);
    }

    public void saveAllSnapshots(ObjectMapper mapper,
                                 String outDirLocation) throws Exception {
        for(IdWeight iw : idWeightMap.values()) {
            String outFileLocation = outDirLocation + '/' + iw.getId();
            mapper.writeValue(new File(outFileLocation), iw.snapshot());
        }
    }

    //******************************************************************
    // applying inertia related methods ...
    //******************************************************************
    public void applyInertia(double inertia) {
        for(IdWeight iw : idWeightMap.values())
            iw.applyInertia(inertia);
    }
}
