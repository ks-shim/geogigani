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


    @RequestMapping(value = {"/statistics"}, produces = "application/json; charset=utf8", method = {RequestMethod.GET})
    public String showAreaStatistics(Model model) {

        List<IdFreq> result = statisticsService.getAreaStatistics();
        String[] ids = new String[result.size()];
        String[] freqs = new String[result.size()];
        getEachIdAndFreqArrays(result, ids, freqs);

        model.addAttribute(ModelField.STATISTICS_AREA_LABELS.label(), ids);
        model.addAttribute(ModelField.STATISTICS_AREA_VALUES.label(), freqs);

        return "statistics-page";
    }

    private void getEachIdAndFreqArrays(List<IdFreq> idFreqList,
                                        final String[] ids,
                                        final String[] freqs) {
        for(int i=0; i<idFreqList.size(); i++) {
            ids[i] = idFreqList.get(i).getId();
            freqs[i] = String.valueOf(idFreqList.get(i).getFreq());
        }
    }

}
