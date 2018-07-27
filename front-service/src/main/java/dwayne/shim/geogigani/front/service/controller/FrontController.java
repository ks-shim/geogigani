package dwayne.shim.geogigani.front.service.controller;

import dwayne.shim.geogigani.common.code.AreaCode;
import dwayne.shim.geogigani.common.data.DustData;
import dwayne.shim.geogigani.front.service.constants.ModelField;
import dwayne.shim.geogigani.front.service.model.Destination1DepthInfo;
import dwayne.shim.geogigani.front.service.model.Destination2DepthInfo;
import dwayne.shim.geogigani.front.service.service.FrontService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
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

        List<Destination2DepthInfo> result = frontService.getPopularDestinations();
        model.addAttribute(ModelField.DESTINATION_INFO.label(), result.size() == 0 ? null : result);

        String userId = session.getId();
        List<Destination2DepthInfo> interestResult = frontService.getInterestingDestinations(userId);
        model.addAttribute(ModelField.DESTINATION_INTEREST_INFO.label(), interestResult.size() == 0 ? null : interestResult);

        return "main-page";
    }

    @RequestMapping(value = {"/dust-info"}, produces = "application/json; charset=utf8", method = {RequestMethod.GET})
    public String showDustInfo(Model model) {

        DustData dustData = frontService.getDustData();
        model.addAttribute(ModelField.DUST_INFO.label(), dustData.isEmpty() ? null : dustData);

        return "dust-page";
    }

    @RequestMapping(value = {"/region-based-destinations/{areaCode}"}, produces = "application/json; charset=utf8", method = {RequestMethod.GET})
    public String showDustDestinations(Model model,
                                       @PathVariable(value = "areaCode", required = true) String areaCode) {
        String regionName;
        List<Destination1DepthInfo> regionResult;
        try {
            AreaCode.isValid(areaCode);
            regionName = AreaCode.getAreaCode(areaCode).label();
            regionResult = frontService.getDestinationsByAreacode(areaCode);
        } catch (Exception e) {
            regionName = null;
            regionResult = null;
        }

        model.addAttribute(ModelField.REGION_NAME.label(), regionName);
        model.addAttribute(ModelField.DESTINATION_INFO.label(), regionResult);

        return "fragments/dust-modal :: dustBasedDestList";
    }

    @RequestMapping(value = {"/destination-detail/{destId}"}, produces = "application/json; charset=utf8", method = {RequestMethod.GET})
    public String showDestinationDetail(Model model,
                                        HttpSession session,
                                        HttpServletRequest request,
                                        @PathVariable(value = "destId", required = true) String destId) {
        String userAgent = request.getHeader("user-agent");
        boolean skipScoring = userAgent == null ? false : userAgent.toLowerCase().contains("googlebot") ? true : false;

        String userId = session.getId();
        Map<String, String> detailResult = frontService.getDestinationDetail(destId, userId, skipScoring);
        model.addAttribute(ModelField.DESTINATION_DETAIL_INFO.label(), detailResult.size() == 0 ? null : detailResult);

        List<Destination2DepthInfo> similarResult = frontService.getSimilarDestinations(destId);
        model.addAttribute(ModelField.DESTINATION_SIMILAR_INFO.label(), similarResult.size() == 0 ? null : similarResult);

        List<Destination2DepthInfo> shortDistResult = frontService.getShortDistanceDestinations(destId);
        model.addAttribute(ModelField.DESTINATION_IN10KM_INFO.label(), shortDistResult.size() == 0 ? null : shortDistResult);

        return "detail-page";
    }

    @RequestMapping(value = {"/search-destinations"}, produces = "application/json; charset=utf8", method = {RequestMethod.GET})
    public String searchDestinations(Model model,
                                     @RequestParam(value = "keywords") String keywords) {
        List<Destination2DepthInfo> result = frontService.searchDestinations(keywords);
        model.addAttribute(ModelField.DESTINATION_INFO.label(), result.size() == 0 ? null : result);
        return "search-page";
    }
}
