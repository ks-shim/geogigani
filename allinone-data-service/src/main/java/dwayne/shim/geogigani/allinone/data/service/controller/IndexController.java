package dwayne.shim.geogigani.allinone.data.service.controller;

import dwayne.shim.geogigani.allinone.data.service.util.IndexPathUtil;
import dwayne.shim.geogigani.searching.SearchingExecutor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/index")
public class IndexController {

    @Value("${location.index.path.file}")
    private String locationIndexPathFile;

    @Value("${location.index.dir1}")
    private String locationIndexDir1;

    @Value("${location.index.dir2}")
    private String locationIndexDir2;

    @Autowired
    private IndexPathUtil indexPathUtil;

    @Autowired
    private SearchingExecutor searchingExecutor;

    @RequestMapping(value = {"/to-be-path"}, produces = "application/json; charset=utf8", method = {RequestMethod.GET})
    public String getToBeIndexPath() {
        try {
            return indexPathUtil.getNextIndexPath(locationIndexPathFile, locationIndexDir1, locationIndexDir2);
        } catch (Exception e) {
            return "";
        }
    }

    @RequestMapping(value = {"/switch-path"}, produces = "application/json; charset=utf8", method = {RequestMethod.PUT})
    public void forceToSwitchIndexDir() {
        try {
            String toBeIndexPath = indexPathUtil.getNextIndexPath(locationIndexPathFile, locationIndexDir1, locationIndexDir2);
            searchingExecutor.switchIndexLocation(toBeIndexPath);
            log.info("Switched index path ...");
            indexPathUtil.switchIndexPath(locationIndexPathFile, toBeIndexPath);
            log.info("Switched index path info in the file ...");
        } catch (Exception e) {
            log.error(e);
        }
    }
}
