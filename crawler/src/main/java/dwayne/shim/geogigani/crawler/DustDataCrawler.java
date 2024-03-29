package dwayne.shim.geogigani.crawler;

import dwayne.shim.geogigani.common.code.AreaCode;
import dwayne.shim.geogigani.common.data.DustData;
import dwayne.shim.geogigani.crawler.apicaller.ApiCaller;
import dwayne.shim.geogigani.crawler.apicaller.DefaultApiCaller;
import lombok.extern.log4j.Log4j2;
import org.codehaus.jackson.map.ObjectMapper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.*;

import static dwayne.shim.geogigani.crawler.DustDataCrawler.ParameterKey.*;

@Log4j2
public class DustDataCrawler {

    enum ParameterKey {
        SERVICE_KEY("serviceKey"),
        SEARCH_DATE("searchDate");

        private final String label;
        private ParameterKey(String _label) {
            label = _label;
        }

        public String label() {
            return label;
        }
    }

    enum XMLNode {
        ITEM("item"),

        DATA_TIME("dataTime"),
        INFORM_CODE("informCode"),
        INFORM_OVERALL("informOverall"),
        INFORM_CAUSE("informCause"),
        INFORM_GRADE("informGrade"),
        IMAGE_URL1("imageUrl1"),
        IMAGE_URL2("imageUrl2"),
        IMAGE_URL3("imageUrl3"),
        IMAGE_URL4("imageUrl4"),
        IMAGE_URL5("imageUrl5"),
        IMAGE_URL6("imageUrl6"),
        INFORM_DATA("informData");


        private final String label;
        private XMLNode(String _label) {
            label = _label;
        }

        public String labe() {
            return label;
        }
    }

    public DustData execute(String authKey,
                            String today) throws Exception {
        // 1. api caller
        try (ApiCaller apiCaller = new DefaultApiCaller();) {
            // 2. xml parser
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            // 3. etc
            ObjectMapper objectMapper = new ObjectMapper();
            Map<ParameterKey, String> parameters = new HashMap<>();

            RestApiInfo dustForecastApi = buildDustForecastApiInfo(authKey);
            parameters.put(SEARCH_DATE, today);

            String url = dustForecastApi.asUrlStringWith(parameters);
            log.info(url);

            String xml = apiCaller.callAsGet(url);
            DustData dustData = new DustData(today);
            putDataInfoInto(dustData, dBuilder.parse(new InputSource(new StringReader(xml))), today);

            return dustData;
        } finally {}
    }

    public DustData execute(String authKey,
                            Date today) throws Exception {
        return execute(authKey, DustData.todayAsString(today));
    }

    private RestApiInfo buildDustForecastApiInfo(String authKey) {

        // make dust-forecast restapi info ...
        Map<ParameterKey, String> parameters = new HashMap<>();
        parameters.put(SERVICE_KEY, authKey);
        parameters.put(SEARCH_DATE, "");

        RestApiInfo apiInfo = new RestApiInfo(
                "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getMinuDustFrcstDspth", Collections.unmodifiableMap(parameters));

        return apiInfo;
    }

    private void putDataInfoInto(DustData dustData,
                                 Document doc,
                                 String todayStr) {

        // 1. get all item list ...
        NodeList nodeList = doc.getElementsByTagName(XMLNode.ITEM.label);
        int nodeLen = nodeList.getLength();
        if(nodeLen <= 0) return;

        Map<String, Map<String, String>> regionDustMap = new TreeMap<>();

        // 2. traverse all item nodes ...
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
            String informCode = newItemValueMap.get(XMLNode.INFORM_CODE.label);
            String informData = newItemValueMap.get(XMLNode.INFORM_DATA.label);
            String informGrade = newItemValueMap.get(XMLNode.INFORM_GRADE.label);
            if(informCode == null || informData == null || informGrade == null || !informData.equals(todayStr)) continue;

            informCode = informCode.toLowerCase();
            informData = informData.toLowerCase();
            informGrade = informGrade.replaceAll("\\s+", "");
            String[] informGrades = informGrade.split(",");
            for(String ig : informGrades) {
                String[] pieces = ig.split(":");
                if(pieces == null || pieces.length < 2) continue;

                String regionName = pieces[0];
                if(regionName.isEmpty()) continue;
                String regionCode;
                DustData.DustStatus dustStatus;
                try {
                    regionCode = AreaCode.getAreaCodeByLabel(regionName).code();
                    dustStatus = DustData.DustStatus.getDustStatusByLabel(pieces[1]);
                } catch (Exception e) {
                    log.error(e);
                    continue;
                }

                Map<String, String> oldDustMap = regionDustMap.get(regionName);
                if(oldDustMap == null) {
                    oldDustMap = new HashMap<>();
                    oldDustMap.put("regionname", regionName);
                    oldDustMap.put("regioncode", regionCode);
                    regionDustMap.put(regionName, oldDustMap);
                }

                oldDustMap.put(informCode + "status", dustStatus.label());
                oldDustMap.put(informCode + "color", dustStatus.color());
            }
        }

        for(Map<String, String> regionValue : regionDustMap.values())
            dustData.addRegionDustData(regionValue);
    }

    private static class RestApiInfo {

        private final String baseUrl;
        private final Map<DustDataCrawler.ParameterKey, String> parameters;

        public RestApiInfo(String baseUrl,
                           Map<DustDataCrawler.ParameterKey, String> parameters) {
            this.baseUrl = baseUrl;
            this.parameters = parameters;
        }

        public String asUrlStringWith(Map<DustDataCrawler.ParameterKey, String> newParams) {

            // 1. put pre-existing parameters into newParams ...
            for(DustDataCrawler.ParameterKey key : parameters.keySet())
                newParams.putIfAbsent(key, parameters.get(key));

            // 2. make url (baseUrl + parameters)
            StringBuilder sb = new StringBuilder();
            sb.append(baseUrl).append('?');

            boolean isFirst = true;
            for(DustDataCrawler.ParameterKey key : newParams.keySet()) {
                if(!isFirst) sb.append('&');
                else isFirst = false;

                sb.append(key.label).append('=').append(newParams.get(key));
            }

            return sb.toString();
        }
    }

    public static void main(String[] args) throws Exception {
        // 1. reading related variables ...
        final String authKey =
                "yUZKBeVTETcH8r9aE%2FHv1uNYXav0MmW%2B37mxGP5A%2FR2fMtWVbGPiKue9KAYV16WKfjiiqmYcOljfYSgpzMdylw%3D%3D";

        DustDataCrawler crawler = new DustDataCrawler();
        DustData dustData = crawler.execute(authKey, new Date());
        System.out.println(dustData);
    }
}
