package dwayne.shim.geogigani.allinone.data.service.service;

import dwayne.shim.geogigani.core.storage.IdWeightStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeywordDataService {

    @Autowired
    private IdWeightStorage keywordStorage;
}
