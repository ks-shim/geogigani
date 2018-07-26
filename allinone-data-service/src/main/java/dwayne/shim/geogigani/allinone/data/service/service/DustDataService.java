package dwayne.shim.geogigani.allinone.data.service.service;

import dwayne.shim.geogigani.common.data.DustData;
import dwayne.shim.geogigani.crawler.DustDataCrawler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Log4j2
@Service
public class DustDataService {

    private DustData dustData;
    private final Object dustDataLock = new Object();

    @Value("${dust.authkey}")
    private String authKey;

    @Autowired
    private DustDataCrawler dustDataCrawler;

    public void switchDustData() {
        DustData newDustData = null;
        try {
            newDustData = dustDataCrawler.execute(authKey, new Date());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
            newDustData = new DustData(new Date());
        }

        synchronized (dustDataLock) {
            dustData = newDustData;
        }
    }

    public DustData getDustData() {
        synchronized (dustDataLock) {
            if(dustData == null) dustData = new DustData(new Date());
            return dustData;
        }
    }
}
