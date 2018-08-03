package dwayne.shim.geogigani.crawler.apicaller;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                .setSocketTimeout(30000)
                .setConnectTimeout(30000)
                .setConnectionRequestTimeout(30000)
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

    @Override
    public String callAsPost(String url, Map<String, String> params) throws Exception {
        if (url == null || url.trim().length() == 0)
            throw new IllegalArgumentException("The parameter <url> is needed.");

        // Response handler doing not much ...
        ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
            @Override
            public String handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException {
                int status = httpResponse.getStatusLine().getStatusCode();
                if(status < 200 || status >= 300)
                    return "ERROR";
                return EntityUtils.toString(httpResponse.getEntity());
            }
        };

        try {
            HttpPost httpPost = new HttpPost(url);
            addBody(httpPost, params);
            String responseBody = httpClient.execute(httpPost, responseHandler);
            return responseBody;
        } finally {

        }
    }

    private void addBody(HttpPost httpPost, Map<String, String> params) throws UnsupportedEncodingException {
        final String encoding = "UTF-8";
        if(params == null || params.size() == 0) return;

        List<NameValuePair> body = new ArrayList<>();
        for(String key : params.keySet())
            body.add(new BasicNameValuePair(key, URLEncoder.encode(params.get(key), encoding)));
        httpPost.setEntity(new UrlEncodedFormEntity(body));
    }

}
