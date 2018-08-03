package dwayne.shim.geogigani.crawler.apicaller;

import java.io.Closeable;
import java.util.Map;

public interface ApiCaller extends Closeable {

    String callAsGet(String url) throws Exception;

    String callAsPut(String url) throws Exception;

    String callAsPost(String url, Map<String, String> params) throws Exception;
}
