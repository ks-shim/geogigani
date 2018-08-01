package dwayne.shim.geogigani.allinone.data.service.controller;

import dwayne.shim.geogigani.allinone.data.service.service.StatisticsDataService;
import dwayne.shim.geogigani.common.data.statistics.SessionCountData;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/statistics")
public class StatisticsDataController {

    @Resource
    private StatisticsDataService dataService;

    @RequestMapping(value = {"/session-count"}, produces = "application/json; charset=utf8", method = {RequestMethod.GET})
    public ResponseEntity<List<SessionCountData>> getDustData() {
        List<SessionCountData> sessionCountDataList;
        try {
            sessionCountDataList = dataService.getTop10SessionCountData();
        } catch (Exception e) {
            sessionCountDataList = new ArrayList<>();
        }
        return new ResponseEntity(sessionCountDataList, HttpStatus.OK);
    }
}
