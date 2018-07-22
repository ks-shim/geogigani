package dwayne.shim.geogigani.allinone.data.service.controller;

import dwayne.shim.geogigani.allinone.data.service.service.LocationDataService;
import dwayne.shim.geogigani.allinone.data.service.service.UserPreferenceDataService;
import dwayne.shim.geogigani.common.data.TravelData;
import dwayne.shim.geogigani.common.indexing.TravelDataIndexField;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/location")
public class LocationDataController {

    //@Resource
    //private KeywordDataService keywordDataService;

    @Resource
    private UserPreferenceDataService userPreferenceDataService;

    @Resource
    private LocationDataService locationDataService;

    @RequestMapping(value = {"/popular"}, produces = "application/json; charset=utf8", method = {RequestMethod.GET})
    public ResponseEntity<List<TravelData>> getPopularLocations() {
        List<TravelData> result;
        try {
            result = locationDataService.getPopularLocations();
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
            result = locationDataService.searchLocations(keywords);
        } catch (Exception e) {
            result = new ArrayList<>();
        }

        return new ResponseEntity(result, HttpStatus.OK);
    }

    @RequestMapping(value = {"/interest/{userId}"}, produces = "application/json; charset=utf8", method = {RequestMethod.GET})
    public ResponseEntity<List<TravelData>> getInterestingLocations(@PathVariable(value = "userId") String userId) {

        List<TravelData> result;
        try {
            String userKeywords = userPreferenceDataService.getUserKeywords(userId);
            if(userKeywords == null) throw new NullPointerException();

            result = locationDataService.interestingLocations(userKeywords);
        } catch (Exception e) {
            result = new ArrayList<>();
        }

        return new ResponseEntity(result, HttpStatus.OK);
    }

    @RequestMapping(value = {"/similar/{locationId}"}, produces = "application/json; charset=utf8", method = {RequestMethod.GET})
    public ResponseEntity<List<TravelData>> getSimilarLocations(@PathVariable(value = "locationId") String locationId) {

        List<TravelData> result;
        try {
            result = locationDataService.getSimilarLocations(locationId);
        } catch (Exception e) {
            result = new ArrayList<>();
        }

        return new ResponseEntity(result, HttpStatus.OK);
    }

    @RequestMapping(value = {"/short-distance/{locationId}"}, produces = "application/json; charset=utf8", method = {RequestMethod.GET})
    public ResponseEntity<List<TravelData>> getShortDistanceLocations(@PathVariable(value = "locationId") String locationId) {

        List<TravelData> result;
        try {
            result = locationDataService.getShortDistanceLocations(locationId);
        } catch (Exception e) {
            result = new ArrayList<>();
        }

        return new ResponseEntity(result, HttpStatus.OK);
    }

    private final TravelDataIndexField[] fieldsForUserKeywords = {
            TravelDataIndexField.TITLE_KEYWORDS,
            TravelDataIndexField.ADDR1
    };

    @RequestMapping(value = {"/detail/{locationId}"}, produces = "application/json; charset=utf8", method = {RequestMethod.GET})
    public ResponseEntity<TravelData> getLocationDetail(@PathVariable(value = "locationId") String locationId,
                                                        @RequestParam(value = "userId", required = false) String userId,
                                                        @RequestParam(value = "skipScoring", required = false, defaultValue = "false") boolean skipScoring) {

        TravelData result;
        try {
            // 1. get location detail data ...
            result = locationDataService.getLocationDetail(locationId, skipScoring);

            // 2. register user-keywords info ...
            if(userId != null) userPreferenceDataService.addUserKeywords(
                    userId, result.getInfoMap(), fieldsForUserKeywords);
        } catch (Exception e) {
            result = TravelData.dummyTravelData(locationId);
        }

        return new ResponseEntity(result, HttpStatus.OK);
    }

    @Scheduled(fixedRateString = "${common.inertia.interval}", initialDelayString = "${common.inertia.init-time}")
    private void applyInertia() {
        log.info("Start applying inertia ...");
        locationDataService.applyInertia();
        //keywordDataService.applyInertia();
        log.info("Finished applying inertia ...");
    }

    @Scheduled(fixedRateString = "${common.sort.interval}", initialDelayString = "${common.sort.init-time}")
    private void sortData() {
        log.info("Start sorting data ...");
        locationDataService.sortData();
        //keywordDataService.sortData();
        log.info("Finished sorting data ...");
    }

    @Scheduled(fixedRateString = "${common.snapshot.save.interval}", initialDelayString = "${common.snapshot.save.init-time}")
    private void saveSnapshots() {
        log.info("Start saving snapshots ...");
        try {
            locationDataService.saveSnapshot();
            //keywordDataService.saveSnapshot();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        log.info("Finished saving snapshots ...");
    }

    @Scheduled(fixedRateString = "${user.keywords.ttl-check.interval}", initialDelayString = "${user.keywords.ttl-check.init-time}")
    private void checkTTLofUserKeywords() {
        log.info("Start checking user-keywords ttl ...");
        try {
            userPreferenceDataService.removeOldUserData();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        log.info("Finished checking user-keywords ttl ...");
    }
}
