package me.johnniang.wechat.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.johnniang.wechat.exception.RequestFailureException;
import me.johnniang.wechat.exception.ResponseFailureException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.UnsupportedSchemeException;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
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
     * @param requestUrl request url must not be blank
     * @param method     http method name must not be blank
     * @param entity     http entity
     * @param params     name value pair parameters
     * @return response string result
     * @throws IOException in case of a problem or the connection was aborted
     */
    @NonNull
    public static HttpResponse requestViaHttp(@NonNull String requestUrl,
                                              @NonNull String method,
                                              @Nullable HttpEntity entity,
                                              NameValuePair... params) throws IOException {
        return requestForResponse(requestUrl, method, entity, getHttpClient(), params);
    }

    /**
     * Requests with https schema.
     *
     * @param requestUrl request url must not be blank
     * @param method     http method name must not be blank
     * @param entity     http entity
     * @param params     name value pair parameters
     * @return response string result
     * @throws IOException in case of a problem or the connection was aborted
     */
    @NonNull
    public static HttpResponse requestViaHttps(@NonNull String requestUrl,
                                               @NonNull String method,
                                               @Nullable HttpEntity entity,
                                               NameValuePair... params) throws IOException {
        return requestForResponse(requestUrl, method, entity, getHttpsClient(), params);
    }

    /**
     * Requests for http response automatically.
     *
     * @param requestUrl request url must not be blank
     * @param method     http method name must not be blank
     * @param entity     http entity
     * @param params     name value pair parameters
     * @return http response
     * @throws IOException in case of a problem or the connection was aborted
     */
    @NonNull
    public static HttpResponse requestAutoForResponse(@NonNull String requestUrl,
                                                      @NonNull String method,
                                                      @Nullable HttpEntity entity,
                                                      NameValuePair... params) throws IOException {
        HttpClient client;
        String scheme = URI.create(requestUrl).getScheme();
        if (HTTPS.equals(scheme)) {
            client = getHttpsClient();
        } else if (HTTP.equals(scheme)) {
            client = getHttpClient();
        } else {
            log.error("Unsupported scheme: [{}], url: [{}]", scheme, requestUrl);
            throw new UnsupportedSchemeException("Unsupported scheme: " + scheme);
        }
        return requestForResponse(requestUrl, method, entity, client, params);
    }

    /**
     * Requests a resource and return a http response.
     *
     * @param requestUrl request url must not be blank
     * @param method     http method name must not be blank
     * @param entity     http entity
     * @param httpClient http client must not be null
     * @param params     name value pair parameters
     * @return http response
     * @throws IOException in case of a problem or the connection was aborted
     */
    @NonNull
    public static HttpResponse requestForResponse(@NonNull String requestUrl,
                                                  @NonNull String method,
                                                  @Nullable HttpEntity entity,
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
        return httpClient.execute(request);
    }

    /**
     * Reqeusts a resource.
     *
     * @param requestUrl   request url must not be null
     * @param method       http method name must not be null
     * @param data         request data
     * @param responseType response type must not be null
     * @param charset      charset (Default is ISO_8859_1)
     * @param objectMapper object mapper must not be null
     * @param <D>          request data type
     * @param <T>          response data type
     * @return response data or null if no response data but response success
     * @throws ResponseFailureException throws when response status is less than 200 or more than 300
     */
    @Nullable
    @SuppressWarnings("uncheck")
    public static <D, T> T request(@NonNull String requestUrl,
                                   @NonNull String method,
                                   @Nullable D data,
                                   @NonNull Class<T> responseType,
                                   @Nullable Charset charset,
                                   @NonNull ObjectMapper objectMapper) {
        Assert.notNull(objectMapper, "Object mapper must not be null");

        try {
            StringEntity stringEntity = null;

            if (isNeedConvert(data)) {
                // Convert data to json or xml and Create string entity
                stringEntity = new StringEntity(objectMapper.writeValueAsString(data), charset);
            } else if (data != null) {
                // Create string entity
                stringEntity = new StringEntity(data.toString(), charset);
            }

            // Request it and get response
            HttpResponse response = HttpClientUtils.requestAutoForResponse(requestUrl, method, stringEntity);

            // Handle response failure
            if (response.getStatusLine().getStatusCode() < 200 || response.getStatusLine().getStatusCode() >= 300) {
                throw new ResponseFailureException("Failed to response " + requestUrl).setResponse(response);
            }

            log.debug("Response status: [{}]", response);

            if (byte[].class.isAssignableFrom(responseType)) {
                return (T) IOUtils.toByteArray(response.getEntity().getContent());
            }

            // Convert content to string
            String responseContent = EntityUtils.toString(response.getEntity(), charset);

            if (String.class.isAssignableFrom(responseType)) {
                return (T) responseContent;
            }

            if (StringUtils.isNotBlank(responseContent)) {
                // Convert response content to object
                return objectMapper.readValue(responseContent, responseType);
            }

            return null;
        } catch (JsonProcessingException e) {
            throw new RequestFailureException("Failed to parse object to xml or json or failed to parse xml or json to object", e)
                    .setRequestUrl(requestUrl).setMethod(method);
        } catch (UnsupportedEncodingException e) {
            throw new RequestFailureException("Unsupported encoding", e).setRequestUrl(requestUrl).setMethod(method);
        } catch (IOException e) {
            throw new RequestFailureException("Failed to request " + requestUrl).setRequestUrl(requestUrl).setMethod(method);
        }
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

    /**
     * Whether need to convert or not.
     *
     * @param data data to check
     * @return true if need to convert or the data is null
     */
    private static boolean isNeedConvert(@Nullable Object data) {
        if (data == null) {
            return false;
        }

        return isNeedConvert(data.getClass());
    }

    /**
     * Whether need to convert or not.
     *
     * @param clazz class type must not be null
     * @return true if need to convert
     */
    private static boolean isNeedConvert(Class<?> clazz) {
        Assert.notNull(clazz, "Clazz type must not be null");

        boolean ignorable = clazz.isPrimitive() ||
                ClassUtils.isPrimitiveArray(clazz) ||
                ClassUtils.isPrimitiveWrapper(clazz) ||
                ClassUtils.isPrimitiveWrapperArray(clazz) ||
                clazz.equals(String.class);

        return !ignorable;
    }
}
