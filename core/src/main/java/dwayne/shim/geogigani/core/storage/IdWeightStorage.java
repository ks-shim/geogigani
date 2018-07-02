package dwayne.shim.geogigani.core.storage;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Log4j2
public class IdWeightStorage {

    private List<IdWeight> idWeightList;
    private final Map<String, IdWeight> idWeightMap;

    private final Object listLock = new Object();

    public IdWeightStorage() {
        this.idWeightList = new ArrayList<>();
        this.idWeightMap = new ConcurrentHashMap<>();
    }

    public void addIdWeight(IdWeight iw) {
        idWeightList.add(iw);
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

    public void sortList() {
        List<IdWeight> newList = new ArrayList<>();
        for(IdWeight iw : idWeightList) {
            newList.add(iw);
            iw.calculateScore();
        }

        Collections.sort(newList);

        synchronized (listLock) {
            idWeightList = newList;
        }
    }

    public List<IdWeightSnapshot> snapshot(int topN) {
        List<IdWeightSnapshot> list = new ArrayList<>();
        synchronized (listLock) {
            int index = 0;
            for(IdWeight iw : idWeightList) {
                if(++index > topN) break;
                list.add(iw.snapshot());
            }
        }
        return list;
    }
}
