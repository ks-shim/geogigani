package dwayne.shim.geogigani.allinone.data.service.service;

import dwayne.shim.geogigani.core.storage.IdWeightStorage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class LocationDataService {

    @Value("${location.inertia}")
    private double inertia;

    @Value("${location.topn}")
    private int topN;

    @Autowired
    private IdWeightStorage locationStorage;

    public void applyInertia() {
        locationStorage.applyInertia(inertia);
    }

    public void sortData() {
        locationStorage.sortAndPickTopN(topN);
    }

    public void impress(String id) {
        locationStorage.impress(id);
    }

    public void click(String id) {
        locationStorage.impress(id);
    }
}
