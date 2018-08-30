package dwayne.shim.geogigani.crawler;

import dwayne.shim.geogigani.crawler.apicaller.ApiCaller;
import dwayne.shim.geogigani.crawler.apicaller.DefaultApiCaller;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.FileAlreadyExistsException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BlogDataCrawler implements Closeable {

    enum ParameterKey {
        QUERY("query"),
        X_NAVER_CLIENT_ID("X-Naver-Client-Id"),
        X_NAVER_CLIENT_SECRET("X-Naver-Client-Secret");

        private final String label;
        private ParameterKey(String _label) {
            label = _label;
        }

        public String labe() {
            return label;
        }
    }

    private final String clientId;
    private final String clientSecret;
    private final Map<String, String> headerMap;
    private final ApiCaller apiCaller;
    private final ObjectMapper objectMapper;
    private final RestApiInfo blogApi;
    private final File outDir;
    public BlogDataCrawler(String clientId,
                           String clientSecret,
                           File outDir) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.outDir = outDir;
        outDir.mkdirs();
        this.apiCaller = new DefaultApiCaller();
        this.objectMapper = new ObjectMapper();
        this.blogApi = buildBlogApiInfo();

        this.headerMap = new HashMap<>();
        this.headerMap.put(ParameterKey.X_NAVER_CLIENT_ID.label, clientId);
        this.headerMap.put(ParameterKey.X_NAVER_CLIENT_SECRET.label, clientSecret);
    }

    private Map<ParameterKey, String> params = new HashMap<>();
    public void execute(String query,
                        String outFileName) throws Exception {

        File outFile = new File(outDir, outFileName);
        if(outFile.exists()) throw new FileAlreadyExistsException(outFileName);

        params.clear();
        params.put(ParameterKey.QUERY, query);

        String json = apiCaller.callAsGet(blogApi.asUrlStringWith(params), headerMap);
        Map<String, Object> jsonMap = objectMapper.readValue(json, Map.class);

        // validation ...
        Integer total = (Integer)jsonMap.get("total");
        if(total == null) throw new RuntimeException("Not having result ...");

        objectMapper.writeValue(new File(outDir, outFileName), jsonMap);
    }

    @Override
    public void close() {
        try {
            apiCaller.close();
        } catch (Exception e) {}
    }

    private RestApiInfo buildBlogApiInfo() {

        Map<ParameterKey, String> parameters = new HashMap<>();
        parameters.put(ParameterKey.QUERY, "");

        RestApiInfo apiInfo = new RestApiInfo(
                "https://openapi.naver.com/v1/search/blog", Collections.unmodifiableMap(parameters));

        return apiInfo;
    }

    public static void main(String[] args) throws Exception {
        final String clientId = "Oz4o8gYg6bG1EH6h78Qk";
        final String clientSecret = "XobPUMb6gz";
        final String outDirPath = "D:/TravelBlogData";

        BlogDataCrawler bdc = new BlogDataCrawler(clientId, clientSecret, new File(outDirPath));
        bdc.execute(URLEncoder.encode("경기도 쌈도둑", "UTF-8"), "1");
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
