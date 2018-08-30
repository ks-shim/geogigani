package dwayne.shim.geogigani.batch;

import dwayne.shim.geogigani.crawler.BlogDataCrawler;
import dwayne.shim.geogigani.crawler.apicaller.ApiCaller;
import dwayne.shim.geogigani.crawler.apicaller.DefaultApiCaller;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.FileAlreadyExistsException;
import java.util.Map;
import java.util.Properties;

public class BatchBlogExecutor {

    public static void main(String[] args) throws Exception {

        if(args.length < 1) {
            System.err.println("Usage : java BatchBlogExecutor <property file path>");
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

        String clientId = prop.getProperty("blog.client.id");
        String clientSecret = prop.getProperty("blog.client.secret");
        String inDirPath = prop.getProperty("location.original.dir");
        String outDirPath = prop.getProperty("blog.out.dir");
        System.out.println("End reading properties ...");

        //-------------------------------------------------------------------------------
        // 2. execute batch step 1
        //    - crawl location data using tour-api
        //-------------------------------------------------------------------------------
        try (BlogDataCrawler crawler = new BlogDataCrawler(clientId, clientSecret, new File(outDirPath))) {

            int count = 0;
            ObjectMapper mapper = new ObjectMapper();
            File inDir = new File(inDirPath);
            for (File file : inDir.listFiles()) {
                String fileName = file.getName();
                try {
                    Map<String, String> doc = mapper.readValue(file, Map.class);
                    String address = doc.get("addr1");
                    String title = doc.get("title");
                    if (address == null || address.trim().isEmpty()) continue;
                    if (title == null || title.trim().isEmpty()) continue;

                    String query = address.split("\\s+")[0] + ' ' + title;
                    crawler.execute(URLEncoder.encode(query, "UTF-8"), fileName);

                    System.out.println(++count + " : " + query);
                } catch (FileAlreadyExistsException fae) {
                    // ignore ...
                } catch (Exception e) {
                    System.err.println(e);
                }
            }
        }
    }
}
