package dwayne.shim.geogigani.crawler.apicaller;

import java.io.Closeable;

public interface ApiCaller extends Closeable {

    String callAsGet(String url) throws Exception;

    String callAsPut(String url) throws Exception;
}
