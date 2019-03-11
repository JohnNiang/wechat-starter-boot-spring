package me.johnniang.wechat.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.johnniang.wechat.cache.WechatCacheStore;
import me.johnniang.wechat.exception.WechatException;
import me.johnniang.wechat.properties.WechatProperties;
import me.johnniang.wechat.service.WechatService;
import me.johnniang.wechat.support.WechatBaseResponse;
import me.johnniang.wechat.support.WechatConstant;
import me.johnniang.wechat.support.message.kefu.KfMessage;
import me.johnniang.wechat.support.token.WechatOAuth2Token;
import me.johnniang.wechat.support.token.WechatToken;
import me.johnniang.wechat.support.user.WechatUser;
import me.johnniang.wechat.util.WechatUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

import static me.johnniang.wechat.support.WechatConstant.EXPIRED_SECOND_GAP;
import static me.johnniang.wechat.util.WechatUtils.request;
import static me.johnniang.wechat.util.WechatUtils.shouldResponseSuccessfully;

/**
 * Default wechat service implementation.
 *
 * @author johnniang
 */
@Slf4j
public class DefaultWechatServiceImpl implements WechatService {

    private final WechatProperties wechatProperties;

    private final WechatCacheStore cacheStore;

    public DefaultWechatServiceImpl(WechatProperties wechatProperties,
                                    WechatCacheStore cacheStore) {
        this.wechatProperties = wechatProperties;
        this.cacheStore = cacheStore;
    }

    @Override
    public WechatToken getWechatToken() {
        Assert.hasText(wechatProperties.getAccessTokenUrl(), "Wechat access token url must not be blank");
        Assert.hasText(wechatProperties.getAppId(), "Wechat app id must not be blank");
        Assert.hasText(wechatProperties.getAppSecret(), "Wechat app secret must not be blank");

        String tokenKey = WechatConstant.WECHAT_TOKEN_KEY_PREFIX + wechatProperties.getAppId();

        // Get wechat token from cache
        return cacheStore.getForWechat(tokenKey, WechatToken.class).orElseGet(() -> {
            // Build access token url
            String accessTokenUrl = String.format(wechatProperties.getAccessTokenUrl(),
                    "client_credential",
                    wechatProperties.getAppId(),
                    wechatProperties.getAppSecret());
            // Get wechat token
            WechatToken wechatToken = request(accessTokenUrl, "get", null, WechatToken.class);

            // Check response
            shouldResponseSuccessfully(wechatToken, "Failed to get wechat token");

            // Create a cache
            cacheStore.putForWechat(tokenKey, wechatToken, wechatToken.getExpiresIn() - EXPIRED_SECOND_GAP, TimeUnit.SECONDS);

            log.debug("Wechat token: [{}]", wechatToken);

            return wechatToken;
        });
    }

    @Override
    public String getWechatTokenString() {
        return getWechatToken().getAccessToken();
    }

    @Override
    public WechatOAuth2Token getWechatOAuth2Token(String code) {
        Assert.notNull(code, "Authorization code must not be blank");
        Assert.hasText(wechatProperties.getOAuth2AccessTokenUrl(), "Wechat OAuth2 access token url must not be blank");

        // Build request url
        String requestUrl = String.format(wechatProperties.getOAuth2AccessTokenUrl(), wechatProperties.getAppId(), wechatProperties.getAppSecret(), code, "authorization_code");

        // Request it
        WechatOAuth2Token wechatOAuth2Token = request(requestUrl, "get", null, WechatOAuth2Token.class);

        // Check response
        shouldResponseSuccessfully(wechatOAuth2Token, "Failed to get wechat OAuth2 access token");

        return wechatOAuth2Token;
    }

    @Override
    public boolean checkSignature(String signature, String timestamp, String nonce) {
        Assert.hasText(signature, "Signature must not be blank");
        Assert.hasText(timestamp, "Timestamp must not be blank");
        Assert.hasText(nonce, "Nonce must not be blank");
        Assert.hasText(wechatProperties.getValidationToken(), "Wechat validation token must not be blank");

        return WechatUtils.checkSignature(signature, timestamp, nonce, wechatProperties.getValidationToken());
    }

    @Override
    public WechatUser getWechatUser(String openid) {
        Assert.hasText(openid, "Openid must not be blank");
        Assert.hasText(wechatProperties.getUserInfoUrl(), "Wechat get user info url must not be blank");

        return getWechatUserBy(String.format(wechatProperties.getUserInfoUrl(), getWechatTokenString(), openid));
    }

    @Override
    public WechatUser getWechatUserViaSns(String openid, String oAuth2AccessToken) {
        Assert.hasText(openid, "openid must not be blank");
        Assert.hasText(oAuth2AccessToken, "OAuth2 access token must not be blank");
        Assert.hasText(wechatProperties.getSnsUserInfoUrl(), "Wechat get user info via sns url must not be blank");

        return getWechatUserBy(String.format(wechatProperties.getSnsUserInfoUrl(), oAuth2AccessToken, openid));
    }

    @Override
    public void sendKfMessage(KfMessage message) {
        Assert.notNull(message, "Kefu message must not be null");
        Assert.hasText(wechatProperties.getMessageSendingUrl(), "Wechat message sending url must not be blank");

        // Build message sending url
        String messageSendingUrl = String.format(wechatProperties.getMessageSendingUrl(), getWechatToken());

        // Request for it
        WechatBaseResponse wechatResponse = request(messageSendingUrl, "post", message, WechatBaseResponse.class);

        // Should responses successfully
        shouldResponseSuccessfully(wechatResponse, "Failed to send kf message");

        // TODO Retry to send kefu message
    }

    /**
     * Gets wechat user info
     *
     * @param requestUserUrl request user url must not be blank
     * @return actual wechat user
     */
    private WechatUser getWechatUserBy(@NonNull String requestUserUrl) {
        Assert.hasText(requestUserUrl, "Request user url must not be blank");

        // Request for user info
        WechatUser wechatUser = WechatUtils.request(requestUserUrl, "get", null, WechatUser.class);

        // Check is error response
        if (!WechatUtils.isResponseSuccessfully(wechatUser)) {
            // If response is error
            throw new WechatException("Failed to get wechat user").setData(wechatUser);
        }

        log.debug("Got wechat user: [{}]", wechatUser);

        return wechatUser;
    }
}
