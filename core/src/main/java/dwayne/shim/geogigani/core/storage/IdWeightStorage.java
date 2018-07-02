package dwayne.shim.geogigani.core.storage;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Log4j2
public class IdWeightStorage {

    private final List<IdWeight> idWeightList;
    private final Map<String, IdWeight> idWeightMap;

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
}
