package dwayne.shim.geogigani.front.service.controller;

import dwayne.shim.geogigani.front.service.constants.ModelField;
import dwayne.shim.geogigani.front.service.model.Destination2DepthInfo;
import dwayne.shim.geogigani.front.service.model.IdFreq;
import dwayne.shim.geogigani.front.service.service.FrontStatisticsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/")
public class FrontStatisticsController {

    @Resource
    private FrontStatisticsService statisticsService;


    @RequestMapping(value = {"/statistics-area"}, produces = "application/json; charset=utf8", method = {RequestMethod.GET})
    public String showAreaStatistics(Model model) {

        List<IdFreq> result = statisticsService.getAreaStatistics();
        model.addAttribute(ModelField.STATISTICS_AREA.label(), result.size() == 0 ? null : result);

        return "statistics-page";
    }
}
