package me.johnniang.wechat.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.johnniang.wechat.exception.WechatException;
import me.johnniang.wechat.support.WechatBaseResponse;
import me.johnniang.wechat.support.WechatConstant;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.*;

/**
 * Utilities of wechat.
 *
 * @author johnniang
 */
@Slf4j
public class WechatUtils {

    /**
     * Private this constructor
     */
    private WechatUtils() {
    }

    /**
     * Request a resource (Default object mapper is JsonUtils.DEFAULT_JSON_MAPPER)
     *
     * @param requestUrl   request url must not be blank
     * @param method       http method name must not be blank
     * @param data         request data
     * @param responseType response type must not be null
     * @param <D>          request data type
     * @param <T>          response data type
     * @return respons data or null if there is not content in response
     */
    @Nullable
    public static <D, T> T request(@NonNull String requestUrl,
                                   @NonNull String method,
                                   @Nullable D data,
                                   @NonNull Class<T> responseType) {
        return HttpClientUtils.request(requestUrl, method, data, responseType, WechatConstant.WECHAT_CHARSET, JsonUtils.DEFAULT_JSON_MAPPER);
    }

    /**
     * Request a resource
     *
     * @param requestUrl   request url must not be blank
     * @param method       http method name must not be blank
     * @param data         request data
     * @param responseType response type must not be null
     * @param objectMapper object mapper must not be null
     * @param <D>          request data type
     * @param <T>          response data type
     * @return respons data or null if there is not content in response
     */
    @Nullable
    public static <D, T> T request(@NonNull String requestUrl,
                                   @NonNull String method,
                                   @Nullable D data,
                                   @NonNull Class<T> responseType,
                                   @NonNull ObjectMapper objectMapper) {
        return HttpClientUtils.request(requestUrl, method, data, responseType, WechatConstant.WECHAT_CHARSET, objectMapper);
    }


    /**
     * Check the response is successful or not.
     *
     * @param baseResponse base response data must not be null
     * @return true if response successfully and false otherwise
     */
    public static boolean isResponseSuccessfully(@NonNull WechatBaseResponse baseResponse) {
        Assert.notNull(baseResponse, "Base response must not be null");

        return baseResponse.getErrcode() == WechatBaseResponse.SUCCESS;

    }

    /**
     * Should response successfully.
     *
     * @param baseResponse wechat base response
     * @throws WechatException throws when contain error in wechat response
     */
    @Deprecated
    public static void shouldResponseSuccessfully(@NonNull WechatBaseResponse baseResponse) {
        Assert.notNull(baseResponse, "Base response must not be null");

        if (baseResponse.getErrcode() != WechatBaseResponse.SUCCESS) {
            log.error("Wechat response error: [{}]", baseResponse.printErrorDetail());
            throw new WechatException("Wechat response error").setData(baseResponse);
        }
    }

    /**
     * Should response successfully.
     *
     * @param baseResponse wechat base response
     * @throws WechatException throws when contain error in wechat response
     */
    public static void shouldResponseSuccessfully(@NonNull WechatBaseResponse baseResponse, @NonNull String message) {
        Assert.notNull(baseResponse, "Base response must not be null");
        Assert.hasText(message, "Error message must not be blank");

        if (baseResponse.getErrcode() != WechatBaseResponse.SUCCESS) {
            log.error("Wechat response error: [{}]", baseResponse.printErrorDetail());
            throw new WechatException(message).setData(baseResponse);
        }
    }

    /**
     * Gets current timestamp.
     *
     * @return current timestamp
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * Random a string specified length
     *
     * @param length random string length must not be less than 0
     * @return random string (Alpha and numeric)
     */
    public static String getRandomString(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }


    /**
     * Checks signature.
     *
     * @param signature signature must not be blank
     * @param datas     datas to check (Will be sort by nature)
     * @return true if the signature is correct, false otherwise
     */
    public static boolean checkSignature(@NonNull String signature, @NonNull String... datas) {
        Assert.hasText(signature, "signature must not be blank");
        Assert.notEmpty(datas, "To check data must not be empty");

        // Sort data to check
        Arrays.sort(datas);

        // Implode the data to a string
        StringBuilder stringBuilder = new StringBuilder();
        for (String data : datas) {
            stringBuilder.append(data);
        }

        // Sha1 that string
        String sha1Hex = DigestUtils.sha1Hex(stringBuilder.toString());

        // Check
        boolean matched = signature.equalsIgnoreCase(sha1Hex);

        if (!matched) {
            log.warn("Signature: [{}] is mismatch, actual signature: [{}]", signature, sha1Hex);
        } else {
            log.debug("Signature: [{}] is match the sha1hex: [{}]", signature, sha1Hex);
        }

        return matched;
    }

    /**
     * Gets sha1 signature.
     *
     * @param sortedMap  to be signed sorted map must not be null
     * @param ignoreKeys ignore keys
     * @return sha1 signature
     */
    @NonNull
    public static String getSha1Sign(SortedMap<String, Object> sortedMap, String... ignoreKeys) {
        Assert.notNull(sortedMap, "To be signed map must not be null");
        // Convert ignore keys to list
        List<String> ignoreKeyList = Arrays.asList(ignoreKeys);

        StringBuffer stringBuffer = new StringBuffer();

        sortedMap.forEach((key, value) -> {
            if (!ignoreKeyList.contains(key)) {
                // If this key is not contain in ignore keys
                stringBuffer.append(key).append("=").append(value).append('&');
            }
        });

        String toBeSignedString = stringBuffer.toString();

        // Remove the last '&' sign
        toBeSignedString = StringUtils.removeEnd(toBeSignedString, "&");

        log.debug("Before signing string: [{}]", toBeSignedString);

        // Sha1 the string
        String signature = DigestUtils.sha1Hex(toBeSignedString);

        log.debug("Signature of sha1: [{}]", signature);

        return signature;
    }

    /**
     * Gets MD5 signature with key.
     *
     * @param map sorted map must not be empty
     * @param key wechat secret key for signing
     * @return md5 signature (Lower case)
     */
    @NonNull
    public static String getMD5SignWithKey(SortedMap<?, ?> map, @NonNull String key) {
        List<String> ignoreKeys = Arrays.asList("sign", "key");

        return getMD5SignWithKey(map, key, ignoreKeys);
    }

    /**
     * Gets MD5 signature with key.
     *
     * @param map        map must not be empty
     * @param key        wechat secret key for signing
     * @param ignoreKeys key list which should be ignored
     * @return md5 signature (Lower case)
     */
    @NonNull
    public static String getMD5SignWithKey(@NonNull Map<?, ?> map, @NonNull String key, @Nullable List<?> ignoreKeys) {
        Assert.notEmpty(map, "Map to calculate must not be empty");
        Assert.hasText(key, "Key to calculate must not be blank");

        StringBuffer stringBuffer = new StringBuffer();

        map.forEach((mapKey, mapValue) -> {
            if (null != mapValue &&
                    (ignoreKeys == null || !ignoreKeys.contains(mapKey))) {
                // Append map key and map value
                stringBuffer.append(mapKey).append('=').append(mapValue).append('&');
            }
        });

        // Append key
        stringBuffer.append("key=").append(key);

        log.debug("Signing string: [{}]", stringBuffer.toString());

        // Sign that string
        String result = DigestUtils.md5Hex(stringBuffer.toString());

        log.debug("Signed result: [{}] for string: [{}]", result, stringBuffer.toString());

        return result;
    }

    /**
     * Gets MD5 signature with key.
     *
     * @param data data to sign
     * @param key  wechat secret key for signing
     * @return md5 signature (Lower case)
     */
    @NonNull
    public static String getMD5SignWithKey(@NonNull Object data, @NonNull String key, @NonNull ObjectMapper objectMapper) {
        // Convert data to sorted map
        SortedMap<?, ?> sortedMap = convertToSortedMap(data, objectMapper);

        return getMD5SignWithKey(sortedMap, key);
    }

    /**
     * Converts object data to sorted map.
     *
     * @param data         data to convert must not be null
     * @param objectMapper object maper must not be null
     * @return sorted map converting from object data
     */
    @NonNull
    private static SortedMap<?, ?> convertToSortedMap(@NonNull Object data, @NonNull ObjectMapper objectMapper) {
        // Convert data to sorted map
        try {
            String dataJson = JsonUtils.objectToJson(data, objectMapper);
            return JsonUtils.jsonToObject(dataJson, TreeMap.class);
        } catch (java.io.IOException e) {
            throw new WechatException("Object to map processing error", e).setData(data);
        }
    }
}
