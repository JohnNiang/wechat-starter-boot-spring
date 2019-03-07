package me.johnniang.wechat.service;

import me.johnniang.wechat.support.token.WechatToken;
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

    /**
     * Checks signature.
     *
     * @param signature signature must not be blank
     * @param timestamp timestamp must not be blank
     * @param nonce     nonce must not be blank
     * @return true if the signature is correct, or false
     */
    boolean checkSignature(@NonNull String signature, @NonNull String timestamp, @NonNull String nonce);
}
