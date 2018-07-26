package dwayne.shim.geogigani.crawler.apicaller;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class DefaultApiCaller implements ApiCaller {

    private final PoolingHttpClientConnectionManager poolConnManager;
    private final CloseableHttpClient httpClient;

    public DefaultApiCaller() {
        poolConnManager = new PoolingHttpClientConnectionManager();
        poolConnManager.setMaxTotal(200);
        httpClient = HttpClients.custom().setConnectionManager(poolConnManager).build();
    }

    private final ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
        @Override
        public String handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
            int status = httpResponse.getStatusLine().getStatusCode();
            if(status < 200 || status >= 300) throw new RuntimeException("Error occurred. check status : " + status);

            return EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
        }
    };

    @Override
    public String callAsGet(String url) throws Exception {

        HttpGet httpGet = new HttpGet(url);

        RequestConfig config = RequestConfig.custom()
                .setSocketTimeout(10000)
                .setConnectTimeout(10000)
                .setConnectionRequestTimeout(10000)
                .build();

        httpGet.setConfig(config);
        String responseBody = httpClient.execute(httpGet, responseHandler);
        return responseBody;
    }

    @Override
    public void close() {
        try {
            if(httpClient != null) httpClient.close();
            if(poolConnManager != null) poolConnManager.close();
        } catch (Exception e) {}
    }

    @Override
    public String callAsPut(String url) throws Exception {
        HttpPut httpPut = new HttpPut(url);
        String responseBody = httpClient.execute(httpPut, responseHandler);
        return responseBody;
    }
}
