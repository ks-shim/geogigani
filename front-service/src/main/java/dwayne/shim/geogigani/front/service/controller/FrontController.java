package dwayne.shim.geogigani.front.service.controller;

import dwayne.shim.geogigani.common.code.AreaCode;
import dwayne.shim.geogigani.common.data.DustData;
import dwayne.shim.geogigani.common.util.LocationDistance;
import dwayne.shim.geogigani.front.service.constants.ModelField;
import dwayne.shim.geogigani.front.service.model.Destination1DepthInfo;
import dwayne.shim.geogigani.front.service.model.Destination2DepthInfo;
import dwayne.shim.geogigani.front.service.model.IdFreq;
import dwayne.shim.geogigani.front.service.service.FrontService;
import org.apache.lucene.geo.GeoUtils;
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
import java.util.HashMap;
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
        model.addAttribute(ModelField.DUST_MAP_INFO.label(), asMapChartData(dustData.getRegionDustDataList()));
        return "dust-page";
    }

    private List<Map<String, Object>> asMapChartData(List<Map<String, String>> regionDustMap) {
        Map<String, Map<String, Object>> dataMap = new HashMap();
        if(regionDustMap == null) return new ArrayList<>();

        for(Map<String, String> dustMap : regionDustMap) {
            try {
                String regionName = dustMap.get("regionname");
                String regionCode = dustMap.get("regioncode");
                String pm10status = dustMap.get("pm10status");
                String pm25status = dustMap.get("pm25status");
                String o3status = dustMap.get("o3status");

                AreaCode ac = AreaCode.getBaseAreaCodeByLabel(regionName);
                regionName = ac.label();
                String code3 = ac.code3();

                // read existing data like '경기북부' & '경기남부' = '경기도'
                // merge '경기북부' and '경기남부'
                Map<String, Object> oldDataMap = dataMap.get(regionName);
                String oldStatus = oldDataMap == null ? null : (String)oldDataMap.get("status");
                DustData.DustStatus ds = DustData.DustStatus.asColor(pm10status, pm25status, o3status, oldStatus);

                Map<String, Object> oneDataMap = new HashMap<>();
                oneDataMap.put("hc-key", code3);
                oneDataMap.put("color", ds.color());
                oneDataMap.put("status", ds.label());
                oneDataMap.put("regioncode", regionCode);
                oneDataMap.put("regionname", regionName);
                dataMap.put(regionName, oneDataMap);
            } catch (Exception e) {
                continue;
            }
        }

        return new ArrayList<>(dataMap.values());
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

        List<Map<String, String>> blogResult = frontService.getDestinationBlog(destId);
        model.addAttribute(ModelField.DESTINATION_BLOG_INFO.label(), blogResult.size() == 0 ? null : blogResult);

        return "detail-page";
    }

    @RequestMapping(value = {"/search-destinations"}, produces = "application/json; charset=utf8", method = {RequestMethod.GET})
    public String searchDestinations(Model model,
                                     @RequestParam(value = "keywords") String keywords) {
        List<Destination2DepthInfo> result = frontService.searchDestinations(keywords);
        model.addAttribute(ModelField.DESTINATION_INFO.label(), result.size() == 0 ? null : result);
        return "search-page";
    }

    @RequestMapping(value = {"/short-dist-destinations-from-me/{latitude}/{longitude:.+}"}, produces = "application/json; charset=utf8", method = {RequestMethod.GET})
    public String showShortDistDestinationsFromMe(Model model,
                                                  @PathVariable(value = "latitude", required = true) Double latitude,
                                                  @PathVariable(value = "longitude", required = true) Double longitude) {
        try {
            GeoUtils.checkLatitude(latitude);
            GeoUtils.checkLongitude(longitude);
        } catch (Exception e) {
            return "short-dist-from-me-page";
        }

        model.addAttribute(ModelField.LATITUDE.label(), latitude);
        model.addAttribute(ModelField.LONGITUDE.label(), longitude);

        List<Destination2DepthInfo> shortDistResult = frontService.getShortDistanceDestinationsFromMe(String.valueOf(latitude), String.valueOf(longitude));
        model.addAttribute(ModelField.DESTINATION_IN10KM_INFO.label(), shortDistResult.size() == 0 ? null : shortDistResult);

        return "short-dist-from-me-page";
    }
}
