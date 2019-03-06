package me.johnniang.wechat.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.johnniang.wechat.support.WechatConstant;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.IOException;

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

    @Nullable
    public static <D, T> T request(@NonNull String requestUrl,
                                   @NonNull String method,
                                   @Nullable D data,
                                   @NonNull Class<T> responseType,
                                   @NonNull ObjectMapper objectMapper) {
        return HttpClientUtils.request(requestUrl, method, data, responseType, WechatConstant.WECHAT_CHARSET, objectMapper);
    }

}
