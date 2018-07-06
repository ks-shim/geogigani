package dwayne.shim.geogigani.front.service.controller;

import dwayne.shim.geogigani.front.service.constants.ModelField;
import dwayne.shim.geogigani.front.service.service.FrontService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class FrontController {

    @Resource
    private FrontService frontService;

    @RequestMapping(value = {"/popular-destinations"}, produces = "application/json; charset=utf8", method = {RequestMethod.GET})
    public String showPopularDestinations(Model model) {
        List<Map<String, String>> result = frontService.getPopularDestinations();
        model.addAttribute(ModelField.DESTINATION_INFO.label(), result);
        return "main-page";
    }

    @RequestMapping(value = {"/destination-detail/{destId}"}, produces = "application/json; charset=utf8", method = {RequestMethod.GET})
    public String showDestinationDetail(Model model,
                                        @PathVariable(value = "destId") String destId) {

        Map<String, String> detailResult = frontService.getDestinationDetail(destId);
        model.addAttribute(ModelField.DESTINATION_DETAIL_INFO.label(), detailResult);

        List<Map<String, String>> similarResult = frontService.getSimilarDestinations(destId);
        model.addAttribute(ModelField.DESTINATION_SIMILAR_INFO.label(), similarResult);
        return "detail-page";
    }
}
