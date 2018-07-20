package dwayne.shim.geogigani.crawler;

import dwayne.shim.geogigani.crawler.apicaller.ApiCaller;
import dwayne.shim.geogigani.crawler.apicaller.DefaultApiCaller;
import lombok.extern.log4j.Log4j2;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.Map;

@Log4j2
public class DustDataCrawler {

    enum ParameterKey {
        SERVICE_KEY("serviceKey"),
        SEARCH_DATE("searchDate"),
        INFORM_CODE("InformCode");

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

    public void execute(String authKey,
                        String appName,
                        String osName,
                        String searchDate) throws Exception {

        // 2. api caller
        ApiCaller apiCaller = new DefaultApiCaller();
        // 3. xml parser
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
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
}
