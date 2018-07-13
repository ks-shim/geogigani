package dwayne.shim.geogigani.front.service.controller;

import dwayne.shim.geogigani.front.service.constants.ModelField;
import dwayne.shim.geogigani.front.service.service.FrontService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class FrontController {

    @Resource
    private FrontService frontService;

    @RequestMapping(value = {"", "/", "/popular-destinations"}, produces = "application/json; charset=utf8", method = {RequestMethod.GET})
    public String showPopularDestinations(Model model,
                                          HttpSession session) {

        List<Map<String, String>> result = frontService.getPopularDestinations();
        model.addAttribute(ModelField.DESTINATION_INFO.label(), result);

        String userId = session.getId();
        List<Map<String, String>> interestResult = frontService.getInterestingDestinations(userId);
        model.addAttribute(ModelField.DESTINATION_INTEREST_INFO.label(), interestResult.size() == 0 ? null : interestResult);

        return "main-page";
    }

    @RequestMapping(value = {"/destination-detail/{destId}"}, produces = "application/json; charset=utf8", method = {RequestMethod.GET})
    public String showDestinationDetail(Model model,
                                        HttpSession session,
                                        @PathVariable(value = "destId") String destId) {

        String userId = session.getId();
        Map<String, String> detailResult = frontService.getDestinationDetail(destId, userId);
        model.addAttribute(ModelField.DESTINATION_DETAIL_INFO.label(), detailResult);

        List<Map<String, String>> similarResult = frontService.getSimilarDestinations(destId);
        model.addAttribute(ModelField.DESTINATION_SIMILAR_INFO.label(), similarResult);

        List<Map<String, String>> shortDistResult = frontService.getShortDistanceDestinations(destId);
        model.addAttribute(ModelField.DESTINATION_IN10KM_INFO.label(), shortDistResult);

        return "detail-page";
    }

    @RequestMapping(value = {"/search-destinations"}, produces = "application/json; charset=utf8", method = {RequestMethod.GET})
    public String searchDestinations(Model model,
                                     @RequestParam(value = "keywords") String keywords) {
        List<Map<String, String>> result = frontService.searchDestinations(keywords);
        model.addAttribute(ModelField.DESTINATION_INFO.label(), result);
        return "main-page";
    }
}
