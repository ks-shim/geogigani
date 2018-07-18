package dwayne.shim.geogigani.crawler;

import dwayne.shim.geogigani.crawler.apicaller.ApiCaller;
import dwayne.shim.geogigani.crawler.apicaller.DefaultApiCaller;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.*;

import static dwayne.shim.geogigani.crawler.TravelDataCrawler.ParameterKey.*;

@Log4j2
public class TravelDataCrawler {

    enum ParameterKey {
        SERVICE_KEY("serviceKey"),
        PAGE_NO("pageNo"),
        PAGE_SIZE("pageSize"),
        START_PAGE("startPage"),
        NUM_OF_ROWS("numOfRows"),
        MOBILE_APP("MobileApp"),
        MOBILE_OS("MobileOS"),
        ARRANGE("arrange"),
        CAT1("cat1"),
        CAT2("cat2"),
        CAT3("cat3"),
        CONTENT_TYPE_ID("contentTypeId"),
        AREA_CODE("areaCode"),
        SIGUNGU_CODE("sigunguCode"),
        LIST_YN("listYN"),
        INTRO_YN("introYN"),

        MAP_X(""),
        MAP_Y(""),
        RADIUS(""),

        CONTENT_ID("contentId"),
        DEFAULT_YN("defaultYN"),
        FIRST_IMAGE_YN("firstImageYN"),
        AREA_CODE_YN("areacodeYN"),
        CAT_CODE_YN("catcodeYN"),
        ADDR_INFO_YN("addrinfoYN"),
        MAP_INFO_YN("mapinfoYN"),
        OVERVIEW_YN("overviewYN");

        private final String label;
        private ParameterKey(String _label) {
            label = _label;
        }

        public String labe() {
            return label;
        }
    }

    enum XMLNode {
        ITEM("item"),

        ADDR1("addr1"),
        ADDR2("addr2"),
        AREA_CODE("areacode"),
        CAT1("cat1"),
        CAT2("cat2"),
        CAT3("cat3"),
        CONTENT_ID("contentid"),
        CONTENT_TYPE_ID("contenttypeid"),
        CREATED_TIME("createdtime"),
        FIRST_IMAGE("firstimage"),
        FIRST_IMAGE2("firstimage2"),
        MAP_X("mapx"),
        MAP_Y("mapy"),
        MLEVEL("mlevel"),
        MODIFIED_TIME("modifiedtime"),
        SIGUNGU_CODE("sigungucode"),
        TEL("tel"),
        TEL_NAME("telname"),
        TITLE("title"),
        ZIPCODE("zipcode"),
        OVERVIEW("overview"),

        // INTRO-DETAIL ...
        ACCOM_COUNT("accomcount"),
        CHK_BABY_CARRIAGE("chkbabycarriage"),
        CHK_CREDIT_CARD("chkcreditcard"),
        CHK_PET("chkpet"),
        EXP_AGE_RANGE("expagerange"),
        EXP_GUIDE("expguide"),
        INFO_CENTER("infocenter"),
        OPEN_DATE("opendate"),
        PARKING("parking"),
        REST_DATE("restdate"),
        USE_SEASON("useseason"),
        USE_TIME("usetime"),
        ACCOM_COUNT_CULTURE("accomcountculture"),
        CHK_BABY_CARRIAGE_CULTURE("chkbabycarriageculture"),
        CHK_CREDIT_CARD_CULTURE("chkcreditcardculture"),
        CHK_PE_TCULTURE("chkpetculture"),
        DISCOUNT_INFO("discountinfo"),
        INFO_CENTER_CULTURE("infocenterculture"),
        PARKING_CULTURE("parkingculture"),
        PARKING_FEE("parkingfee"),
        REST_DATE_CULTURE("restdateculture"),
        USE_FEE("usefee"),
        USE_TIME_CULTURE("usetimeculture"),
        SPEND_TIME("spendtime"),
        AGE_LIMIT("agelimit"),
        BOOKING_PLACE("bookingplace"),
        DISCOUNT_INFO_FESTIVAL("discountinfofestival"),
        EVENT_END_DATE("eventenddate"),
        EVEN_THOMEPAGE("eventhomepage"),
        EVENT_PLACE("eventplace"),
        EVEN_TSTART_DATE("eventstartdate"),
        PLACE_INFO("placeinfo"),
        PLAY_TIME("playtime"),
        PROGRAM("program"),
        SPEND_TIME_FESTIVAL("spendtimefestival"),
        SPONSOR1("sponsor1"),
        SPONSOR1_TEL("sponsor1tel"),
        SPONSOR2("sponsor2"),
        SPONSOR2_TEL("sponsor2tel"),
        SUB_EVENT("subevent"),
        USE_TIME_FESTIVAL("usetimefestival"),
        DISTANCE("distance"),
        INFO_CENTER_TOUR_COURSE("infocentertourcourse"),
        SCHEDULE("schedule"),
        TAKE_TIME("taketime"),
        THEME("theme"),
        ACCOM_COUNT_LEPORTS("accomcountleports"),
        CHK_BABY_CARRIAGE_LEPORTS("chkbabycarriageleports"),
        CHK_CREDIT_CARD_LEPORTS("chkcreditcardleports"),
        CHK_PET_LEPORTS("chkpetleports"),
        EXP_AGE_RANGE_LEPORTS("expagerangeleports"),
        INFO_CENTER_LEPORTS("infocenterleports"),
        OPEN_PERIOD("openperiod"),
        PARKING_FEE_LEPORTS("parkingfeeleports"),
        PARKING_LEPORTS("parkingleports"),
        RESERVATION("reservation"),
        REST_DATE_LEPORTS("restdateleports"),
        SCALE_LEPORTS("scaleleports"),
        USE_FEE_LEPORTS("usefeeleports"),
        USE_TIME_LEPORTS("usetimeleports"),
        ACCOM_COUNT_LODGING("accomcountlodging"),
        CHECK_IN_TIME("checkintime"),
        CHECK_OUT_TIME("checkouttime"),
        CHK_COOKING("chkcooking"),
        FOOD_PLACE("foodplace"),
        HANOK("hanok"),
        INFO_CENTER_LODGING("infocenterlodging"),
        PARKING_LODGING("parkinglodging"),
        PICKUP("pickup"),
        ROOM_COUNT("roomcount"),
        RESERVATION_LODGING("reservationlodging"),
        RESERVATION_URL("reservationurl"),
        ROOM_TYPE("roomtype"),
        SCALE_LODGING("scalelodging"),
        SUB_FACILITY("subfacility"),
        BARBECUE("barbecue"),
        BEAUTY("beauty"),
        BEVERAGE("beverage"),
        BICYCLE("bicycle"),
        CAMPFIRE("campfire"),
        FITNESS("fitness"),
        KARAOKE("karaoke"),
        PUBLIC_BATH("publicbath"),
        PUBLIC_PC("publicpc"),
        SAUNA("sauna"),
        SEMINAR("seminar"),
        SPORTS("sports"),
        CHK_BABY_CARRIAGE_SHOPPING("chkbabycarriageshopping"),
        CHK_CREDIT_CARD_SHOPPING("chkcreditcardshopping"),
        CHK_PET_SHOPPING("chkpetshopping"),
        CULTURE_CENTER("culturecenter"),
        FAIRDAY("fairday"),
        INFO_CENTER_SHOPPING("infocentershopping"),
        OPEN_DATE_SHOPPING("opendateshopping"),
        OPEN_TIME("opentime"),
        PARKING_SHOPPING("parkingshopping"),
        REST_DATE_SHOPPING("restdateshopping"),
        RESTROOM("restroom"),
        SALE_ITEM("saleitem"),
        SALE_ITEM_COST("saleitemcost"),
        SCALE_SHOPPING("scaleshopping"),
        SHOP_GUIDE("shopguide"),
        CHK_CREDIT_CARD_FOOD("chkcreditcardfood"),
        DISCOUNT_INFO_FOOD("discountinfofood"),
        FIRST_MENU("firstmenu"),
        INFO_CENTER_FOOD("infocenterfood"),
        KIDS_FACILITY("kidsfacility"),
        OPEN_DATE_FOOD("opendatefood"),
        OPEN_TIME_FOOD("opentimefood"),
        PACKING("packing"),
        PARKING_FOOD("parkingfood"),
        RESERVATION_FOOD("reservationfood"),
        REST_DATE_FOOD("restdatefood"),
        SCALE_FOOD("scalefood"),
        SEAT("seat"),
        SMOKING("smoking"),
        TREAT_MENU("treatmenu");


        private final String label;
        private XMLNode(String _label) {
            label = _label;
        }

        public String labe() {
            return label;
        }
    }

    public void execute(String authKey,
                        int numOfRows,
                        String appName,
                        String osName,
                        File outDir,
                        int startPage,
                        int endPage,
                        String lastModifiedTime) throws Exception {

        // 0. ready to write the results ...
        outDir.mkdirs();

        // 1. declare data store (map)
        Map<String, Map<String, String>> travelDataMap = new HashMap<>();
        // 2. api caller
        ApiCaller apiCaller = new DefaultApiCaller();
        // 3. xml parser
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        // 4. etc
        ObjectMapper objectMapper = new ObjectMapper();
        Map<ParameterKey, String> parameters = new HashMap<>();

        // 5. api info
        RestApiInfo areaBaseListApi = buildAreaBasedListApiInfo(authKey, numOfRows, appName, osName);
        RestApiInfo detailCommonApi = buildDetailCommonApiInfo(authKey, appName, osName);
        RestApiInfo introApi = buildIntroApiInfo(authKey, appName, osName);

        // 6. callAsGet areaBasedList api & detailCommon api sequentially ...
        boolean keepCrawling = true;
        int pageNo = 0;
        while(true) {

            ++pageNo;
            if(pageNo < startPage) continue;
            else if (pageNo > endPage) break;

            // 5-1. callAsGet areaBasedList and extract data ...
            keepCrawling = readAreaBasedListTravelData(travelDataMap, areaBaseListApi, parameters, apiCaller, dBuilder, pageNo, lastModifiedTime);
            if(!keepCrawling) break;

            // 5-2. callAsGet detailCommon and extract data ...
            readDetailedTravelData(travelDataMap, detailCommonApi, parameters,
                    apiCaller, dBuilder, lastModifiedTime);

            // 5-3. callAsGet detailCommon and extract data ...
            readIntroTravelData(travelDataMap, introApi, parameters,
                    apiCaller, dBuilder, lastModifiedTime);

            // 5-4. write to files ...
            writeToFile(travelDataMap, objectMapper, outDir);

            // 5-5. empty travel data map
            travelDataMap.clear();
        }
    }

    //******************************************************************************************
    // Data writing methods ...
    //******************************************************************************************
    private void writeToFile(Map<String, Map<String, String>> travelDataMap,
                             ObjectMapper objectMapper,
                             File outDir) throws Exception {

        for(String contentId : travelDataMap.keySet()) {
            Map<String, String> contentValues = travelDataMap.get(contentId);
            if(contentValues == null) continue;

            objectMapper.writeValue(new File(outDir, contentId), contentValues);
        }
    }

    //******************************************************************************************
    // Data reading methods ...
    //******************************************************************************************
    private boolean readAreaBasedListTravelData(Map<String, Map<String, String>> travelDataMap,
                                                RestApiInfo areaBaseListApi,
                                                Map<ParameterKey, String> parameters,
                                                ApiCaller apiCaller,
                                                DocumentBuilder dBuilder,
                                                int pageNo,
                                                String lastModifiedTime) throws Exception {
        parameters.clear();
        parameters.put(PAGE_NO, String.valueOf(pageNo));
        return readTravelData(travelDataMap, areaBaseListApi, parameters, apiCaller, dBuilder, lastModifiedTime);
    }

    private void readDetailedTravelData(Map<String, Map<String, String>> travelDataMap,
                                        RestApiInfo detailCommonApi,
                                        Map<ParameterKey, String> parameters,
                                        ApiCaller apiCaller,
                                        DocumentBuilder dBuilder,
                                        String lastModifiedTime) throws Exception {

        for(String contentId : travelDataMap.keySet()) {
            parameters.clear();
            parameters.put(CONTENT_ID, contentId);
            readTravelData(travelDataMap, detailCommonApi, parameters, apiCaller, dBuilder, lastModifiedTime);
            //Thread.sleep(500);
        }
    }

    private void readIntroTravelData(Map<String, Map<String, String>> travelDataMap,
                                     RestApiInfo introApi,
                                     Map<ParameterKey, String> parameters,
                                     ApiCaller apiCaller,
                                     DocumentBuilder dBuilder,
                                     String lastModifiedTime) throws Exception {
        for(Map<String, String> docMap : travelDataMap.values()) {
            parameters.clear();
            String contentId = docMap.get(XMLNode.CONTENT_ID.label);
            String contentTypeId = docMap.get(XMLNode.CONTENT_TYPE_ID.label);
            parameters.put(CONTENT_ID, contentId);
            parameters.put(CONTENT_TYPE_ID, contentTypeId);

            try {
                readTravelData(travelDataMap, introApi, parameters, apiCaller, dBuilder, lastModifiedTime);
            } catch (Exception e) {
                log.error(e);
            }
        }
    }

    private boolean readTravelData(Map<String, Map<String, String>> travelDataMap,
                                   RestApiInfo apiInfo,
                                   Map<ParameterKey, String> parameters,
                                   ApiCaller apiCaller,
                                   DocumentBuilder dBuilder,
                                   String lastModifiedTime) throws Exception {
        String url = apiInfo.asUrlStringWith(parameters);
        log.info(url);

        String xml = apiCaller.callAsGet(url);
        return putDataInfoInto(travelDataMap, dBuilder.parse(new InputSource(new StringReader(xml))), lastModifiedTime);
    }

    private boolean putDataInfoInto(Map<String, Map<String, String>> travelDataMap,
                                    Document doc,
                                    String lastModifiedTime) {

        // 1. get all item list ...
        NodeList nodeList = doc.getElementsByTagName(XMLNode.ITEM.label);
        int nodeLen = nodeList.getLength();
        if(nodeLen <= 0) return false;

        // 2. traverse all item nodes ...
        boolean hasValidOnes = false;
        for(int i=0; i<nodeLen; i++) {
            Element element = (Element)nodeList.item(i);

            // 2-1. read all data for a item ...
            Map<String, String> newItemValueMap = new HashMap<>();
            for(XMLNode xmlNode : XMLNode.values()) {
                NodeList subNodeList = element.getElementsByTagName(xmlNode.label);
                if(subNodeList == null || subNodeList.getLength() <= 0) continue;

                newItemValueMap.put(xmlNode.label, subNodeList.item(0).getTextContent());
            }

            // 2-2. get content id if exists ...
            String contentId = newItemValueMap.get(XMLNode.CONTENT_ID.label);
            if(contentId == null) continue;

            String oModifiedTime = newItemValueMap.get(XMLNode.MODIFIED_TIME.label);
            if(oModifiedTime != null && lastModifiedTime.compareTo(oModifiedTime) > 0) continue;

            // 2-3. add newly or merge with old value map ...
            Map<String, String> oldItemValueMap = travelDataMap.get(contentId);
            hasValidOnes = true;
            if(oldItemValueMap == null) {
                travelDataMap.put(contentId, newItemValueMap);
                continue;
            }
            oldItemValueMap.putAll(newItemValueMap);
        }

        return hasValidOnes;
    }

    private RestApiInfo buildAreaBasedListApiInfo(String authKey,
                                                  int numOfRows,
                                                  String appName,
                                                  String osName) {

        // make area-based-list restapi info ...
        Map<ParameterKey, String> parameters = new HashMap<>();
        parameters.put(SERVICE_KEY, authKey);
        parameters.put(PAGE_NO, "1");
        parameters.put(START_PAGE, "1");
        parameters.put(NUM_OF_ROWS, String.valueOf(numOfRows));
        parameters.put(MOBILE_APP, appName);
        parameters.put(MOBILE_OS, osName);
        parameters.put(ARRANGE, "Q");
        parameters.put(CAT1, "");
        parameters.put(CAT2, "");
        parameters.put(CAT3, "");
        parameters.put(AREA_CODE, "");
        parameters.put(SIGUNGU_CODE, "");
        parameters.put(LIST_YN, "Y");

        RestApiInfo apiInfo = new RestApiInfo(
                "http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList", Collections.unmodifiableMap(parameters));

        return apiInfo;
    }

    private RestApiInfo buildDetailCommonApiInfo(String authKey,
                                                 String appName,
                                                 String osName) {

        // make detail-common restapi info ...
        Map<ParameterKey, String> parameters = new HashMap<>();
        parameters.put(SERVICE_KEY, authKey);
        parameters.put(MOBILE_APP, appName);
        parameters.put(MOBILE_OS, osName);
        parameters.put(CONTENT_ID, "");
        parameters.put(CONTENT_TYPE_ID, "");
        parameters.put(DEFAULT_YN, "Y");
        parameters.put(FIRST_IMAGE_YN, "Y");
        parameters.put(AREA_CODE_YN, "Y");
        parameters.put(CAT_CODE_YN, "Y");
        parameters.put(ADDR_INFO_YN, "Y");
        parameters.put(MAP_INFO_YN, "Y");
        parameters.put(OVERVIEW_YN, "Y");

        RestApiInfo apiInfo = new RestApiInfo(
                "http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon", Collections.unmodifiableMap(parameters));

        return apiInfo;
    }

    private RestApiInfo buildIntroApiInfo(String authKey,
                                          String appName,
                                          String osName) {

        // make intro restapi info ...
        Map<ParameterKey, String> parameters = new HashMap<>();
        parameters.put(SERVICE_KEY, authKey);
        parameters.put(PAGE_NO, "1");
        parameters.put(NUM_OF_ROWS, "100");
        parameters.put(MOBILE_APP, appName);
        parameters.put(MOBILE_OS, osName);
        parameters.put(CONTENT_ID, "");
        parameters.put(CONTENT_TYPE_ID, "");
        parameters.put(INTRO_YN, "Y");

        RestApiInfo apiInfo = new RestApiInfo(
                "http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailIntro", Collections.unmodifiableMap(parameters));

        return apiInfo;
    }

    public static String getTimestampBefore(int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, days * -1);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(cal.getTimeInMillis()) + "000000";
    }

    public static void main(String[] args) throws Exception {

        // 1. reading related variables ...
        final String authKey =
                "Vwp9OmiFxxt9M2nremlH%2FAU2uOOGjbudaPl7HHd8hb1HYufbqGcYEtbdljMYWYB8KVhLPjCeJNG68O9WVOzZeA%3D%3D";

        final int numOfRows = 1000;
        final String appName = "geogigani";
        final String osName = "AND";

        // 2. writing related variables ...
        final String outDirectory = "D:/TravelLocationData";

        // 3. processing ..
        TravelDataCrawler tdc = new TravelDataCrawler();
        int startPage = 1;
        String lastModifiedTime = getTimestampBefore(1);
        tdc.execute(authKey, numOfRows, appName, osName, new File(outDirectory), startPage, startPage+100, lastModifiedTime);
    }

    private static class RestApiInfo {

        private final String baseUrl;
        private final Map<ParameterKey, String> parameters;

        public RestApiInfo(String baseUrl,
                           Map<ParameterKey, String> parameters) {
            this.baseUrl = baseUrl;
            this.parameters = parameters;
        }

        public String asUrlStringWith(Map<ParameterKey, String> newParams) {

            // 1. put pre-existing parameters into newParams ...
            for(ParameterKey key : parameters.keySet())
                newParams.putIfAbsent(key, parameters.get(key));

            // 2. make url (baseUrl + parameters)
            StringBuilder sb = new StringBuilder();
            sb.append(baseUrl).append('?');

            boolean isFirst = true;
            for(ParameterKey key : newParams.keySet()) {
                if(!isFirst) sb.append('&');
                else isFirst = false;

                sb.append(key.label).append('=').append(newParams.get(key));
            }

            return sb.toString();
        }
    }
}
