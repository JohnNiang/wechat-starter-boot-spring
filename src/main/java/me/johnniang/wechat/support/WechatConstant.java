package me.johnniang.wechat.support;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Constant variable of wechat.
 *
 * @author johnniang
 */
public class WechatConstant {

    /**
     * Wechat charset.
     */
    public final static Charset WECHAT_CHARSET = StandardCharsets.ISO_8859_1;

    /**
     * UTF 8 charset.
     */
    public final static Charset UTF_8_CHARSET = StandardCharsets.UTF_8;

    /**
     * Media upload form data parameter name.
     */
    public final static String MEDIA_UPLOAD_PARAM_NAME = "media";

    /**
     * Wechat token key prefix.
     */
    public final static String WECHAT_TOKEN_KEY_PREFIX = "wechat_token_";

}
