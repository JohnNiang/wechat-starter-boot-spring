package me.johnniang.wechat.service;

import me.johnniang.wechat.support.token.WechatToken;
import me.johnniang.wechat.support.user.WechatUser;
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
     * @return true if the signature is correct, false otherwise
     */
    boolean checkSignature(@NonNull String signature, @NonNull String timestamp, @NonNull String nonce);

    /**
     * Gets wechat user info.
     *
     * @param openid wechat openid must not be blank
     * @return actual wechat user info
     */
    @NonNull
    WechatUser getWechatUser(@NonNull String openid);

    /**
     * Gets wechat user info via sns.
     *
     * @param openid            wechat openid must not be blank
     * @param oAuth2AccessToken oauth2 access token
     * @return actual wechat user info
     */
    @NonNull
    WechatUser getWechatUserViaSns(@NonNull String openid, @NonNull String oAuth2AccessToken);
}
