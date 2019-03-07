package me.johnniang.wechat.support;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Wechat token.
 *
 * @author johnniang
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class WechatToken extends WechatBaseResponse {

    /**
     * Access token.
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * Token expired second.
     */
    @JsonProperty("expireds_in")
    private int expiresIn;

}
