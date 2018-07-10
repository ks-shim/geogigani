package dwayne.shim.geogigani.crawler;

import dwayne.shim.geogigani.crawler.apicaller.ApiCaller;
import dwayne.shim.geogigani.crawler.apicaller.DefaultGetApiCaller;
import dwayne.shim.geogigani.crawler.exception.OutOfAuthKeyException;
import lombok.extern.log4j.Log4j2;
import org.codehaus.jackson.map.ObjectMapper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static dwayne.shim.geogigani.crawler.TravelDataCrawler.ParameterKey.*;

import java.io.File;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;

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
        OVERVIEW("overview");

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
                        int endPage) throws Exception {

        // 0. ready to write the results ...
        outDir.mkdirs();

        // 1. declare data store (map)
        Map<String, Map<String, String>> travelDataMap = new HashMap<>();
        // 2. api caller
        ApiCaller apiCaller = new DefaultGetApiCaller();
        // 3. xml parser
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        // 4. etc
        ObjectMapper objectMapper = new ObjectMapper();
        Map<ParameterKey, String> parameters = new HashMap<>();

        // 5. api info
        RestApiInfo areaBaseListApi = buildAreaBasedListApiInfo(authKey, numOfRows, appName, osName);
        RestApiInfo detailCommonApi = buildDetailCommonApiInfo(authKey, appName, osName);

        // 6. call areaBasedList api & detailCommon api sequentially ...
        boolean keepCrawling = true;
        int pageNo = 0;
        while(true) {

            ++pageNo;
            if(pageNo < startPage) continue;
            else if (pageNo > endPage) break;

            // 5-1. call areaBasedList and extract data ...
            keepCrawling = readAreaBasedListTravelData(travelDataMap, areaBaseListApi, parameters, apiCaller, dBuilder, pageNo);
            if(!keepCrawling) break;

            //if(pageNo <= 1) continue;

            // 5-2. call detailCommon and extract data ...
            readDetailedTravelData(travelDataMap, detailCommonApi, parameters,
                    apiCaller, dBuilder);

            // 5-3. write to files ...
            writeToFile(travelDataMap, objectMapper, outDir);

            // 5-4. empty travel data map
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
                                                int pageNo) throws Exception {
        parameters.clear();
        parameters.put(PAGE_NO, String.valueOf(pageNo));
        return readTravelData(travelDataMap, areaBaseListApi, parameters, apiCaller, dBuilder);
    }

    private void readDetailedTravelData(Map<String, Map<String, String>> travelDataMap,
                                        RestApiInfo detailCommonApi,
                                        Map<ParameterKey, String> parameters,
                                        ApiCaller apiCaller,
                                        DocumentBuilder dBuilder) throws Exception {

        for(String contentId : travelDataMap.keySet()) {
            parameters.clear();
            parameters.put(CONTENT_ID, contentId);
            readTravelData(travelDataMap, detailCommonApi, parameters, apiCaller, dBuilder);
            //Thread.sleep(500);
        }
    }

    private boolean readTravelData(Map<String, Map<String, String>> travelDataMap,
                                   RestApiInfo apiInfo,
                                   Map<ParameterKey, String> parameters,
                                   ApiCaller apiCaller,
                                   DocumentBuilder dBuilder) throws Exception {
        String url = apiInfo.asUrlStringWith(parameters);
        log.info(url);

        String xml = apiCaller.call(url);
        return putDataInfoInto(travelDataMap, dBuilder.parse(new InputSource(new StringReader(xml))));
    }

    private boolean putDataInfoInto(Map<String, Map<String, String>> travelDataMap,
                                    Document doc) {

        // 1. get all item list ...
        NodeList nodeList = doc.getElementsByTagName(XMLNode.ITEM.label);
        int nodeLen = nodeList.getLength();
        if(nodeLen <= 0) return false;

        // 2. traverse all item nodes ...
        for(int i=0; i<nodeLen; i++) {
            Element element = (Element)nodeList.item(i);

            // 2-1. read all data for a item ...
            Map<String, String> newItemValueMap = new HashMap<>();
            for(XMLNode xmlNode : XMLNode.values()) {
                NodeList subNodeList = element.getElementsByTagName(xmlNode.label);
                if(subNodeList.getLength() <= 0) continue;

                newItemValueMap.put(xmlNode.label, subNodeList.item(0).getTextContent());
            }

            // 2-2. get content id if exists ...
            String contentId = newItemValueMap.get(XMLNode.CONTENT_ID.label);
            if(contentId == null) continue;

            // 2-3. add newly or merge with old value map ...
            Map<String, String> oldItemValueMap = travelDataMap.get(contentId);
            if(oldItemValueMap == null) {
                travelDataMap.put(contentId, newItemValueMap);
                continue;
            }

            oldItemValueMap.putAll(newItemValueMap);
        }

        return true;
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
        parameters.put(ARRANGE, "R");
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

    public static void main(String[] args) throws Exception {

        // 1. reading related variables ...
        final String[] authKeys = {
                "yUZKBeVTETcH8r9aE%2FHv1uNYXav0MmW%2B37mxGP5A%2FR2fMtWVbGPiKue9KAYV16WKfjiiqmYcOljfYSgpzMdylw%3D%3D",
                "F7YBdXZHx6vqnUZ9woYUfR%2BB9Te%2Fzsx%2BhZLZ5txWbFzjSGgVM8fSgmEb8%2F7fRf1TSXE8MAE98rXn%2Bqy8tHY64w%3D%3D",
                "bESXjwhur523SfTeKHpjpmmU4gDJ4ynWR7YlMC4BfcQagWP%2FkMNQmYiEU%2BiaZ40LtAHMJm%2FaBv0jXOmFLb5dYA%3D%3D"
        };

        final int numOfRows = 1000;
        final String appName = "geogigani";
        final String osName = "AND";

        // 2. writing related variables ...
        final String outDirectory = "D:/TravelLocationData";

        // 3. processing ..
        TravelDataCrawler tdc = new TravelDataCrawler();
        int startPage = 20;
        for(int i=0; i<authKeys.length; i++)
            tdc.execute(authKeys[i], numOfRows, appName, osName, new File(outDirectory), startPage+i, startPage+i);
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
