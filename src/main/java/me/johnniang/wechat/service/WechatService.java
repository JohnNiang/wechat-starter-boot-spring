package me.johnniang.wechat.service;

import me.johnniang.wechat.support.WechatToken;
import org.springframework.lang.NonNull;

/**
 * Wechat service interface.
 *
 * @author johnniang
 */
public interface WechatService {

    /**
     * Gets wechat token.
     *
     * @return wechat token
     */
    @NonNull
    WechatToken getWechatToken();

    /**
     * Gets wechat token string.
     *
     * @return wechat token string
     */
    @NonNull
    String getWechatTokenString();
}
