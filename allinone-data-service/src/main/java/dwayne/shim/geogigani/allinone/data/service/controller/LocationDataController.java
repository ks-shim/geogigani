package dwayne.shim.geogigani.allinone.data.service.controller;

import dwayne.shim.geogigani.allinone.data.service.service.KeywordDataService;
import dwayne.shim.geogigani.allinone.data.service.service.LocationDataService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Log4j2
@RestController
@RequestMapping("/data/location")
public class LocationDataController {

    @Resource
    private KeywordDataService keywordDataService;

    @Resource
    private LocationDataService locationDataService;

    public ResponseEntity<?> getPopularLocations() {
        return null;
    }

    public ResponseEntity<?> getPopularLocationsByKeywords() {
        return null;
    }

    public ResponseEntity<?> searchLocations(String keyword) {
        return null;
    }

    public ResponseEntity<?> getSimilarLocations(String locationId) {
        return null;
    }
}
