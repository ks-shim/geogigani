package dwayne.shim.geogigani.batch;

import dwayne.shim.geogigani.core.keyword.KeywordExtractor;
import dwayne.shim.geogigani.crawler.TravelDataCrawler;
import dwayne.shim.geogigani.crawler.apicaller.ApiCaller;
import dwayne.shim.geogigani.crawler.apicaller.DefaultApiCaller;
import dwayne.shim.geogigani.indexing.IndexingExecutor;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class BatchExecutor {

    public void executeStep1(String authKey,
                             String appName,
                             String osName,
                             String locationDataDir,
                             int daysBefore) throws Exception {

        TravelDataCrawler crawler = new TravelDataCrawler();
        final int numOfRows = 1000;
        final int startPage = 1;
        final int endPage = 100;
        String lastModifiedTime = TravelDataCrawler.getTimestampBefore(daysBefore);
        crawler.execute(
                authKey, numOfRows, appName,
                osName, new File(locationDataDir), startPage, endPage, lastModifiedTime);
    }

    public void executeStep2(String inDir,
                             String outDir,
                             String keyExtractorConfigPath) throws Exception {

        final int docSizeLimit = 1000;

        // 관광지, 문화시설, 숙박, 쇼핑, 음식점, 축제(15), 여행코스(25)
        final int[] contentTypes = {12, 14, 28, 32, 38, 39};
        final Set<String> allowedContentTypes = new HashSet<>();
        for(int contentType : contentTypes)
            allowedContentTypes.add(String.valueOf(contentType));

        new IndexingExecutor(new KeywordExtractor(keyExtractorConfigPath)).execute(inDir, docSizeLimit, allowedContentTypes, outDir);
    }

    public static void main(String[] args) throws Exception {

        if(args.length < 1) {
            System.err.println("Usage : java BatchExecutor <property file path>");
            System.exit(1);
        }

        //-------------------------------------------------------------------------------
        // 1. read properties file ...
        //-------------------------------------------------------------------------------
        System.out.println("Start reading properties ...");
        String propFilePath = args[0].trim();
        Properties prop = new Properties();
        try (InputStream in = new FileInputStream(propFilePath)){
            prop.load(in);
        }


        final String appName = "geogigani";
        final String osName = "AND";
        String authKey = prop.getProperty("tourapi.authkey");
        String locationDataDir = prop.getProperty("location.original.dir");
        int daysBefore = Integer.parseInt(prop.getProperty("crawl.days.before"));

        System.out.println("End reading properties ...");

        //-------------------------------------------------------------------------------
        // 2. execute batch step 1
        //    - crawl location data using tour-api
        //-------------------------------------------------------------------------------
        System.out.println("\n\nStart executing step-1 ...");
        BatchExecutor batchExecutor = new BatchExecutor();
        batchExecutor.executeStep1(authKey, appName, osName, locationDataDir, daysBefore);
        System.out.println("End executing step-1 ...");

        //-------------------------------------------------------------------------------
        // 3. get to-be index dir path ...
        //-------------------------------------------------------------------------------
        System.out.println("\n\nStart getting to-be index path ...");
        String url = prop.getProperty("index.rest.to-be-path");
        String keyExtractorConfigPath = prop.getProperty("key-extractor.config.path");
        String toBePath = "";
        try (ApiCaller apiCaller = new DefaultApiCaller()) {
            toBePath = apiCaller.callAsGet(url);
            if(StringUtils.isBlank(toBePath)) throw new RuntimeException("Got empty TO-BE-PATH from data-service.");
            System.out.println("TO-BE path : " + toBePath);
        }
        System.out.println("End getting to-be index path ...");

        //-------------------------------------------------------------------------------
        // 4. get to-be index dir path ...
        //-------------------------------------------------------------------------------
        System.out.println("\n\nStart executing step-2 ...");
        batchExecutor.executeStep2(locationDataDir, toBePath, keyExtractorConfigPath);
        System.out.println("End executing step-2 ...");

        //-------------------------------------------------------------------------------
        // 5. force data-service to switch index path ...
        //-------------------------------------------------------------------------------
        System.out.println("\n\nStart forcing to switch index path ...");
        url = prop.getProperty("index.rest.switch-path");
        try (ApiCaller apiCaller = new DefaultApiCaller()) {
            apiCaller.callAsPut(url);
        }
        System.out.println("End forcing to switch index path ...");
    }
}
