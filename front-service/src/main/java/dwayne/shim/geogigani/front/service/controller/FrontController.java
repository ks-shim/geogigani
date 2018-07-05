package dwayne.shim.geogigani.front.service.controller;

import dwayne.shim.geogigani.common.data.TravelData;
import dwayne.shim.geogigani.front.service.constants.ModelField;
import dwayne.shim.geogigani.front.service.service.FrontService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/main-page")
public class FrontController {

    @Resource
    private FrontService frontService;

    @RequestMapping(value = {"/popular-locations"}, produces = "application/json; charset=utf8", method = {RequestMethod.GET})
    public String showPopularLocations(Model model) {
        List<Map<String, String>> result = frontService.getPopularLocations();
        model.addAttribute(ModelField.DESTINATION_INFO.label(), result);
        return "main-page";
    }
}
