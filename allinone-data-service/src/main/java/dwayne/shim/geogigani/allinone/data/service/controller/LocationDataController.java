package dwayne.shim.geogigani.allinone.data.service.controller;

import dwayne.shim.geogigani.allinone.data.service.service.KeywordDataService;
import dwayne.shim.geogigani.allinone.data.service.service.LocationDataService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequestMapping("/location")
public class LocationDataController {

    @Resource
    private KeywordDataService keywordDataService;

    @Resource
    private LocationDataService locationDataService;

    @RequestMapping(value = {"/popular"}, produces = "application/json; charset=utf8", method = {RequestMethod.GET})
    public ResponseEntity<?> getPopularLocations() {
        List<Map<String, String>> result;
        try {
            result = locationDataService.getPopularLocation();
        } catch (Exception e) {
            result = new ArrayList<>();
        }

        return new ResponseEntity(result, HttpStatus.OK);
    }

    @RequestMapping(value = {"/popular-by-keywords"}, produces = "application/json; charset=utf8", method = {RequestMethod.GET})
    public ResponseEntity<?> getPopularLocationsByKeywords() {
        // * increment impress count !!
        return null;
    }

    @RequestMapping(value = {"/search"}, produces = "application/json; charset=utf8", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> searchLocations(@RequestParam(value = "keywords", required = true) String keywords) {

        // * increment impress count !!
        return null;
    }

    @RequestMapping(value = {"/similar/{locationId}"}, produces = "application/json; charset=utf8", method = {RequestMethod.GET})
    public ResponseEntity<?> getSimilarLocations(@PathVariable(value = "locationId") String locationId) {
        // * increment impress count !!
        return null;
    }

    @RequestMapping(value = {"/detail/{locationId}"}, produces = "application/json; charset=utf8", method = {RequestMethod.GET})
    public ResponseEntity<?> getLocationDetail(@PathVariable(value = "locationId") String locationId) {

        // * increment click count !!
        return null;
    }

    @Scheduled(fixedRateString = "${common.inertia.interval}", initialDelayString = "${common.inertia.init-time}")
    private void applyInertia() {
        log.info("Start applying inertia ...");
        locationDataService.applyInertia();
        keywordDataService.applyInertia();
        log.info("Finished applying inertia ...");
    }

    @Scheduled(fixedRateString = "${common.sort.interval}", initialDelayString = "${common.sort.init-time}")
    private void sortData() {
        log.info("Start sorting data ...");
        locationDataService.sortData();
        keywordDataService.sortData();
        log.info("Finished sorting data ...");
    }
}
