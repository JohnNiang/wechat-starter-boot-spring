package me.johnniang.wechat.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.johnniang.wechat.support.WechatConstant;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.Arrays;

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
     * Gets current timestamp.
     *
     * @return current timestamp
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis() / 1000;
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
}
