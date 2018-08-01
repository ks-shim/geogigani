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
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class FrontStatisticsController {

    @Resource
    private FrontStatisticsService statisticsService;


    @RequestMapping(value = {"/statistics"}, produces = "application/json; charset=utf8", method = {RequestMethod.GET})
    public String showAreaStatistics(Model model) {

        // 1. get result from ...
        List<IdFreq> areaResultList = new ArrayList<>();
        List<IdFreq> contentTypeResultList = new ArrayList<>();
        statisticsService.getAreaAndContentTypeStatistics(areaResultList, contentTypeResultList);

        // 2. do job for area
        String[] areaIds = new String[areaResultList.size()];
        String[] areaFreqs = new String[areaResultList.size()];
        getEachIdAndFreqArrays(areaResultList, areaIds, areaFreqs);

        model.addAttribute(ModelField.STATISTICS_AREA_LABELS.label(), areaIds);
        model.addAttribute(ModelField.STATISTICS_AREA_VALUES.label(), areaFreqs);

        // 3. do job for content type ...
        String[] ctIds = new String[contentTypeResultList.size()];
        String[] ctFreqs = new String[contentTypeResultList.size()];
        getEachIdAndFreqArrays(contentTypeResultList, ctIds, ctFreqs);

        model.addAttribute(ModelField.STATISTICS_CONTENT_TYPE_LABELS.label(), ctIds);
        model.addAttribute(ModelField.STATISTICS_CONTENT_TYPE_VALUES.label(), ctFreqs);

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
