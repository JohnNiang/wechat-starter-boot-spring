package me.johnniang.wechat.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.johnniang.wechat.properties.WechatProperties;
import me.johnniang.wechat.service.WechatService;
import me.johnniang.wechat.support.token.WechatToken;
import me.johnniang.wechat.util.WechatUtils;
import org.springframework.util.Assert;

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

    public DefaultWechatServiceImpl(WechatProperties wechatProperties) {
        this.wechatProperties = wechatProperties;
    }

    @Override
    public WechatToken getWechatToken() {
        Assert.hasText(wechatProperties.getAccessTokenUrl(), "Wechat access token url must not be blank");
        Assert.hasText(wechatProperties.getAppId(), "Wechat app id must not be blank");
        Assert.hasText(wechatProperties.getAppSecret(), "Wechat app secret must not be blank");

        // TODO Get wechat token from cache

        // Build access token url
        String accessTokenUrl = String.format(wechatProperties.getAccessTokenUrl(),
                "client_credential",
                wechatProperties.getAppId(),
                wechatProperties.getAppSecret());
        // Get wechat token
        WechatToken wechatToken = request(accessTokenUrl, "get", null, WechatToken.class);

        // Check response
        shouldResponseSuccessfully(wechatToken);

        // TODO Create a cache

        log.debug("Wechat token: [{}]", wechatToken);

        return wechatToken;
    }

    @Override
    public String getWechatTokenString() {
        return getWechatToken().getAccessToken();
    }

    @Override
    public boolean checkSignature(String signature, String timestamp, String nonce) {
        Assert.hasText(signature, "Signature must not be blank");
        Assert.hasText(timestamp, "Timestamp must not be blank");
        Assert.hasText(nonce, "Nonce must not be blank");
        Assert.hasText(wechatProperties.getValidationToken(), "Wechat validation token must not be blank");

        return WechatUtils.checkSignature(signature, timestamp, nonce, wechatProperties.getValidationToken());
    }
}
