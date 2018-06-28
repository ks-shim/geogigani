package dwayne.shim.geogigani.crawler.apicaller;

import java.io.Closeable;

public interface ApiCaller extends Closeable {

    String call(String url) throws Exception;
}
