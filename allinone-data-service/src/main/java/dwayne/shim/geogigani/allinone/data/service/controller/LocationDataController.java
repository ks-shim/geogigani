package dwayne.shim.geogigani.allinone.data.service.controller;

import dwayne.shim.geogigani.allinone.data.service.service.KeywordDataService;
import dwayne.shim.geogigani.allinone.data.service.service.LocationDataService;
import dwayne.shim.geogigani.common.data.TravelData;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
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
    public ResponseEntity<List<TravelData>> getPopularLocations() {
        List<TravelData> result;
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
    public ResponseEntity<List<TravelData>> searchLocations(@RequestParam(value = "keywords", required = true) String keywords) {
        List<TravelData> result;
        try {
            result = locationDataService.searchLocation(keywords);
        } catch (Exception e) {
            result = new ArrayList<>();
        }

        return new ResponseEntity(result, HttpStatus.OK);
    }

    @RequestMapping(value = {"/similar/{locationId}"}, produces = "application/json; charset=utf8", method = {RequestMethod.GET})
    public ResponseEntity<List<TravelData>> getSimilarLocations(@PathVariable(value = "locationId") String locationId) {

        List<TravelData> result;
        try {
            result = locationDataService.getSimilarLocation(locationId);
        } catch (Exception e) {
            result = new ArrayList<>();
        }

        return new ResponseEntity(result, HttpStatus.OK);
    }

    @RequestMapping(value = {"/detail/{locationId}"}, produces = "application/json; charset=utf8", method = {RequestMethod.GET})
    public ResponseEntity<TravelData> getLocationDetail(@PathVariable(value = "locationId") String locationId) {

        TravelData result;
        try {
            result = locationDataService.getLocationDetail(locationId);
        } catch (Exception e) {
            result = TravelData.dummyTravelData(locationId);
        }

        return new ResponseEntity(result, HttpStatus.OK);
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

    @Scheduled(fixedRateString = "${common.snapshot.save.interval}", initialDelayString = "${common.snapshot.save.init-time}")
    private void saveSnapshots() {
        log.info("Start saving snapshots ...");
        try {
            locationDataService.saveSnapshot();
            keywordDataService.saveSnapshot();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        log.info("Finished saving snapshots ...");
    }
}
