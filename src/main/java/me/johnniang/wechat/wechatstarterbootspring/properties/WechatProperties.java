package me.johnniang.wechat.wechatstarterbootspring.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Wechat properties.
 *
 * @author johnniang
 */
@ConfigurationProperties("johnniang.wechat")
public class WechatProperties {


    /**
     * App id.
     */
    private String appId;

    /**
     * App secret.
     */
    private String appSecret;

    /**
     * Wechat payment key.
     */
    private String key;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "WechatProperties{" +
                "appId='" + appId + '\'' +
                ", appSecret='" + appSecret + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
