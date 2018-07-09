package dwayne.shim.geogigani.allinone.data.service.service;

import dwayne.shim.geogigani.core.storage.IdWeightStorage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class KeywordDataService {

    @Value("${keyword.inertia}")
    private double inertia;

    @Value("${keyword.topn}")
    private int topN;

    @Value("${keyword.snapshot.dir}")
    private String keywordSnapshotDir;

    @Autowired
    private IdWeightStorage keywordStorage;

    public void applyInertia() {
        keywordStorage.applyInertia(inertia);
    }

    public void sortData() {
        keywordStorage.sortAndPickTopN(topN);
    }

    public void impress(String id) {
        keywordStorage.impress(id);
    }

    public void click(String id) {
        keywordStorage.click(id);
    }

    public void saveSnapshot() throws Exception {
        keywordStorage.saveAllSnapshots(keywordSnapshotDir);
    }
}
