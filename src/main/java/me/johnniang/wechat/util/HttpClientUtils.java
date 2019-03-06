package me.johnniang.wechat.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Http client utilities.
 *
 * @author johnniang
 */
@Slf4j
public class HttpClientUtils {

    private static final String HTTP = "http";

    private static final String HTTPS = "https";

    /**
     * Timeout (Default is 5s).
     */
    private final static int TIMEOUT = 5000;

    /**
     * Private this constructor
     */
    private HttpClientUtils() {
    }

    /**
     * Http client container.
     */
    private static class HttpClientContainer {

        /**
         * Http client.
         */
        private final static CloseableHttpClient HTTP_CLIENT;

        /**
         * Https client.
         */
        private final static CloseableHttpClient HTTPS_CLIENT;

        static {
            HTTP_CLIENT = createHttpClient();

            try {
                HTTPS_CLIENT = createHttpsClient();
            } catch (KeyStoreException | NoSuchAlgorithmException | KeyManagementException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to create https client", e);
            }
        }

        /**
         * Creates http client.
         *
         * @return http client
         */
        @NonNull
        public static CloseableHttpClient createHttpClient() {
            return HttpClients.custom().setDefaultRequestConfig(getReqeustConfig()).build();
        }

        /**
         * Creates https client.
         *
         * @return https client
         * @throws KeyStoreException        key store exception
         * @throws NoSuchAlgorithmException no such algorithm exception
         * @throws KeyManagementException   key management exception
         */
        @NonNull
        public static CloseableHttpClient createHttpsClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
            // TODO Set key store in production environment
            SSLContext sslContext = new SSLContextBuilder()
                    .loadTrustMaterial(null, (certificate, authType) -> true)
                    .build();

            return HttpClients.custom()
                    .setSSLContext(sslContext)
                    .setSSLHostnameVerifier(new NoopHostnameVerifier())
                    .setDefaultRequestConfig(getReqeustConfig())
                    .build();
        }

        /**
         * Gets request config.
         *
         * @return request config
         */
        private static RequestConfig getReqeustConfig() {
            return RequestConfig.custom()
                    .setConnectTimeout(TIMEOUT)
                    .setConnectionRequestTimeout(TIMEOUT)
                    .setSocketTimeout(TIMEOUT)
                    .build();
        }
    }

    /**
     * Gets a http client.
     *
     * @return http client
     */
    @NonNull
    public static HttpClient getHttpClient() {
        return HttpClientContainer.HTTP_CLIENT;
    }

    /**
     * Gets a https client.
     *
     * @return https client
     */
    @NonNull
    public static HttpClient getHttpsClient() {
        return HttpClientContainer.HTTPS_CLIENT;
    }

    /**
     * Requests with http schema
     *
     * @param requestUrl      request url must not be blank
     * @param method          http method name must not be blank
     * @param entity          http entity
     * @param responseCharset response charset (Default is ISO_8859_1)
     * @param params          name value pair parameters
     * @return response string result
     * @throws IOException in case of a problem or the connection was aborted
     */
    @Nullable
    public static String requestViaHttp(@NonNull String requestUrl,
                                        @NonNull String method,
                                        @Nullable HttpEntity entity,
                                        @Nullable Charset responseCharset,
                                        NameValuePair... params) throws IOException {
        return request(requestUrl, method, entity, responseCharset, getHttpClient(), params);
    }

    /**
     * Requests with https schema.
     *
     * @param requestUrl      request url must not be blank
     * @param method          http method name must not be blank
     * @param entity          http entity
     * @param responseCharset response charset (Default is ISO_8859_1)
     * @param params          name value pair parameters
     * @return response string result
     * @throws IOException in case of a problem or the connection was aborted
     */
    @Nullable
    public static String requestViaHttps(@NonNull String requestUrl,
                                         @NonNull String method,
                                         @Nullable HttpEntity entity,
                                         @Nullable Charset responseCharset,
                                         NameValuePair... params) throws IOException {
        return request(requestUrl, method, entity, responseCharset, getHttpsClient(), params);
    }

    /**
     * Requests a resource.
     *
     * @param requestUrl      request url must not be blank
     * @param method          http method name must not be blank
     * @param entity          http entity
     * @param responseCharset response charset (Default is ISO_8859_1)
     * @param httpClient      http client must not be null
     * @param params          name value pair parameters
     * @return response string result
     * @throws IOException in case of a problem or the connection was aborted
     */
    @Nullable
    private static String request(@NonNull String requestUrl,
                                  @NonNull String method,
                                  @Nullable HttpEntity entity,
                                  @Nullable Charset responseCharset,
                                  @NonNull HttpClient httpClient,
                                  NameValuePair... params) throws IOException {
        Assert.hasText(requestUrl, "Request url must not be blank");
        Assert.hasText(method, "Http method name must not be blank");
        Assert.notNull(httpClient, "Http client must not be null");

        RequestBuilder requestBuilder = RequestBuilder.create(method).setUri(requestUrl);
        if (params != null) {
            // Set params
            requestBuilder.addParameters(params);
        }
        requestBuilder.setEntity(entity);
        // Build request
        HttpUriRequest request = requestBuilder.build();

        // Execute
        HttpResponse response = httpClient.execute(request);

        // Convert to string
        return EntityUtils.toString(response.getEntity(), responseCharset);
    }

    /**
     * Converts param object to name value pair.
     *
     * @param paramObject param object must not be null
     * @return name value pair list
     * @throws IOException throws when failed to convert param object to map
     */
    @NonNull
    public static List<NameValuePair> convertToNvpParams(@NonNull Object paramObject) throws IOException {
        // Convert to param object to param map
        Map<?, ?> paramMap = JsonUtils.objectToMap(paramObject);

        // Create params list
        List<NameValuePair> nvpParams = new LinkedList<>();

        paramMap.forEach((key, value) -> nvpParams.add(new BasicNameValuePair(key.toString(), value.toString())));

        return nvpParams;
    }

}
