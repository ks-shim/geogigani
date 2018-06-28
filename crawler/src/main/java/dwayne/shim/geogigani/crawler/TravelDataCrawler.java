package dwayne.shim.geogigani.crawler;

import static dwayne.shim.geogigani.crawler.TravelDataCrawler.ParameterKey.*;

import java.io.File;
import java.util.*;

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

    public void execute(String authKey,
                        String appName,
                        String osName,
                        File outDir) throws Exception {

    }

    private List<RestApiInfo> buildApiInfos(String authKey,
                                            String appName,
                                            String osName) {
        List<RestApiInfo> apiInfoList = new ArrayList<>();

        // 1. make area-based-list restapi info ...
        RestApiInfo apiInfo = new RestApiInfo("http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList");
        apiInfo.addParameter(SERVICE_KEY, authKey);
        apiInfo.addParameter(PAGE_NO, 1);
        apiInfo.addParameter(START_PAGE, 1);
        apiInfo.addParameter(NUM_OF_ROWS, 50);
        apiInfo.addParameter(MOBILE_APP, appName);
        apiInfo.addParameter(MOBILE_OS, osName);
        apiInfo.addParameter(ARRANGE, "R");
        apiInfo.addParameter(CAT1, "");
        apiInfo.addParameter(CAT2, "");
        apiInfo.addParameter(CAT3, "");
        apiInfo.addParameter(AREA_CODE, "");
        apiInfo.addParameter(SIGUNGU_CODE, "");
        apiInfo.addParameter(LIST_YN, "Y");
        apiInfoList.add(apiInfo);

        // 2. make detail-common restapi info ...
        apiInfo = new RestApiInfo("http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon");
        return apiInfoList;
    }

    public static void main(String[] args) throws Exception {

        final String authKey = "yUZKBeVTETcH8r9aE%2FHv1uNYXav0MmW%2B37mxGP5A%2FR2fMtWVbGPiKue9KAYV16WKfjiiqmYcOljfYSgpzMdylw%3D%3D";
        final String appName = "geogigani";
        final String osName = "AND";

        final String outDirectory = "D:/TravelData";

        // 1. make api info list ...

    }

    private static class RestApiInfo {

        private final String baseUrl;
        private final Map<String, String> parameters;

        public RestApiInfo(String baseUrl) {
            this.baseUrl = baseUrl;
            this.parameters = new HashMap<>();
        }

        public <T> void addParameter(ParameterKey key, T value) {
            addParameter(key.label, String.valueOf(value));
        }

        public void addParameter(String key, String value) {
            parameters.put(key, value);
        }
    }
}
