package me.johnniang.wechat.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Http client utilities test.
 *
 * @author johnniang
 */
@Slf4j
@ActiveProfiles("test")
class HttpClientUtilsTest {

    @Test
    void getUrlSchemeTest() {
        String url = "http://www.bing.com";

        URI uri = URI.create(url);

        log.debug("ToString: [{}]", uri.toString());
        log.debug("ToASCIIString: [{}]", uri.toASCIIString());
        log.debug("GetScheme: [{}]", uri.getScheme());
        log.debug("GetSchemeSpecificPart: [{}]", uri.getSchemeSpecificPart());
        log.debug("GetRawSchemeSpecificPart: [{}]", uri.getRawSchemeSpecificPart());

        Assertions.assertEquals("http", uri.getScheme());
    }

    @Test
    void convertToNvpParamsTest() throws IOException {
        TestA a = new TestA("test", 10);
        List<NameValuePair> params = HttpClientUtils.convertToNvpParams(a);

        log.debug("Params result: [{}]", params);

        Assertions.assertEquals(2, params.size());
        Assertions.assertEquals("a", params.get(0).getName());
        Assertions.assertEquals("test", params.get(0).getValue());

        Assertions.assertEquals("b", params.get(1).getName());
        Assertions.assertEquals("10", params.get(1).getValue());
    }

    @Test
    void getViaHttpTest() throws IOException {
        HttpResponse response = HttpClientUtils.requestViaHttp("http://bing.com", "get", null);
        log.debug("Response: [{}}", EntityUtils.toString(response.getEntity()));
    }

    @Test
    void doGetTest() throws IOException {
        HttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(new HttpGet("http://www.bing.com"));
        StatusLine statusLine = response.getStatusLine();
        Assertions.assertEquals(HttpStatus.SC_OK, statusLine.getStatusCode());
    }

    @Test
    void abortRequestTest() throws IOException {
        HttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet("http://www.bing.com");
        HttpResponse response = client.execute(request);
        try {
            log.debug("======================");
            log.debug(response.getStatusLine().toString());
            request.abort();
        } finally {
            if (response != null) {
                ((CloseableHttpResponse) response).close();
            }
        }
    }

    @Test
    void redirectTest() throws IOException {
        CloseableHttpClient client = HttpClientBuilder.create().disableRedirectHandling().build();
        CloseableHttpResponse response = client.execute(new HttpGet("http://bing.com"));

        Assertions.assertEquals(HttpStatus.SC_MOVED_PERMANENTLY, response.getStatusLine().getStatusCode());
    }

    @Test
    void setHeaderTest() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpUriRequest request = RequestBuilder.get()
            .setUri("http://bing.com")
            .setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString())
            .build();
        client.execute(request);
    }

    @Test
    void setDefaultHeaderTest() throws IOException {
        // Set default
        Header header = new BasicHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        ArrayList<Header> headers = Lists.newArrayList(header);

        CloseableHttpClient client = HttpClients.custom()
            .setDefaultHeaders(headers)
            .build();
        HttpUriRequest request = RequestBuilder.get("http://bing.com").build();
        client.execute(request);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class TestA {
        String a;
        Integer b;
    }

}