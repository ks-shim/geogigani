package dwayne.shim.geogigani.core.storage;

import dwayne.shim.geogigani.common.storage.IdWeight;
import dwayne.shim.geogigani.common.storage.IdWeightSnapshot;
import lombok.extern.log4j.Log4j2;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
public class IdWeightStorage {

    private IdWeightSnapshot[] topNSnapshots;
    private final Map<String, String> idAndAreaCodeMap;
    private final Map<String, IdWeight> idWeightMap;

    private final Object snapshotLock = new Object();

    public IdWeightStorage() {
        this.topNSnapshots = new IdWeightSnapshot[0];
        this.idWeightMap = new ConcurrentHashMap<>();
        this.idAndAreaCodeMap = new ConcurrentHashMap<>();
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

    public void addAllIdAndAreadCode(Map<String, String> newIdAreaCodeMap) {
        for(String locationId : newIdAreaCodeMap.keySet()) {
            String areaCode = newIdAreaCodeMap.get(locationId);
            idAndAreaCodeMap.putIfAbsent(locationId, areaCode);
        }
    }

    public String getAreaCodeBy(String locationId) {
        return idAndAreaCodeMap.get(locationId);
    }

    //******************************************************************
    // Scoring related methods ...
    //******************************************************************
    public void impress(String id) {
        IdWeight iw = idWeightMap.get(id);
        if(iw == null) {
            iw = new IdWeight(id);
            idWeightMap.put(id, iw);
        }

        iw.impress();
    }

    public void click(String id) {
        IdWeight iw = idWeightMap.get(id);
        if(iw == null) {
            iw = new IdWeight(id);
            idWeightMap.put(id, iw);
        }

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

            try {
                IdWeightSnapshot snapshot = mapper.readValue(file, IdWeightSnapshot.class);
                addIdWeight(snapshot.asIdWeight());
            } catch (EOFException e) {
                log.error(e.getMessage());
            }
        }
    }

    public void saveAllSnapshots(String outDirLocation) throws Exception {
        try {
            saveAllSnapshots(new ObjectMapper(), outDirLocation);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveAllSnapshots(ObjectMapper mapper,
                                 String outDirLocation) throws Exception {
        for(IdWeight iw : idWeightMap.values()) {
            String outFileLocation = outDirLocation + '/' + iw.getId();
            try {
                mapper.writeValue(new File(outFileLocation), iw.snapshot());
            } catch (FileNotFoundException e) {
                log.error(e.getMessage());
            }
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
