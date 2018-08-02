package dwayne.shim.geogigani.front.service.controller;

import dwayne.shim.geogigani.front.service.constants.ModelField;
import dwayne.shim.geogigani.front.service.model.IdFreq;
import dwayne.shim.geogigani.front.service.service.FrontStatisticsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Controller
@RequestMapping("/")
public class FrontStatisticsController {

    @Resource
    private FrontStatisticsService statisticsService;


    @RequestMapping(value = {"/statistics"}, produces = "application/json; charset=utf8", method = {RequestMethod.GET})
    public String showAreaStatistics(Model model) {

        // 1. get area result from ...
        List<IdFreq> areaResultList = new ArrayList<>();
        List<IdFreq> contentTypeResultList = new ArrayList<>();
        try {
            statisticsService.getAreaAndContentTypeStatistics(areaResultList, contentTypeResultList);
        } catch (Exception e) {
            log.error(e);
        }

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

        // 4. get session-count data
        List<String> sessionLabelList = new ArrayList<>();
        List<Integer> sessionCountList = new ArrayList<>();
        try {
            statisticsService.getSessionCount(sessionLabelList, sessionCountList);
        } catch (Exception e) {
            log.error(e);
        }

        model.addAttribute(ModelField.STATISTICS_SESSION_COUNT_LABELS.label(), sessionLabelList);
        model.addAttribute(ModelField.STATISTICS_SESSION_COUNT_VALUES.label(), sessionCountList);

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
