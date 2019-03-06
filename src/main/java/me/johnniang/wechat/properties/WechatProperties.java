package me.johnniang.wechat.properties;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Wechat properties.
 *
 * @author johnniang
 */
@Data
@ToString
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

    /**
     * Wechat validation token
     */
    private String validationToken;

    /**
     * Mch Id.
     */
    private String mchId;

    /**
     * Payment notification url.
     */
    private String paymentNotificationUrl;

    /**
     * Access token url.
     */
    private String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=%s&appid=%s&secret=%s";

    /**
     * Unified order url. <br>
     * (Default is sandbox url "https://api.mch.weixin.qq.com/sandboxnew/pay/unifiedorder", you have to specify the url with "https://api.mch.weixin.qq.com/pay/unifiedorder" in production env)
     */
    private String unifiedOrderUrl = "https://api.mch.weixin.qq.com/sandboxnew/pay/unifiedorder";

    /**
     * Authorization url. (For login)
     */
    private String authorizationUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=%s&scope=%s&state=%s#wechat_redirect";

    /**
     * OAuth2 access token url.
     */
    private String oAuth2AccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=%s";

    /**
     * Custom message sending url.
     */
    private String messageSendingUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=%s";

    /**
     * UserInfo url.
     */
    private String userInfoUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN";

    /**
     * Sns user info url.
     */
    private String snsUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";

    /**
     * QrCode showing url.
     */
    private String qrcodeShowingUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=%s";

    /**
     * QrCode Creating url.
     */
    private String qrcodeCreatingUrl = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=%s";

    /**
     * Media uploading url.
     */
    private String mediaUploadingUrl = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=%s&type=%s";

    /**
     * Ticket Getting url.
     */
    private String ticketGettingUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=%s";

}
