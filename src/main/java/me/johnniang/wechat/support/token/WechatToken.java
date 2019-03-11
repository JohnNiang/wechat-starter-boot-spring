package me.johnniang.wechat.support.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import me.johnniang.wechat.support.WechatBaseResponse;

/**
 * Wechat token.
 *
 * @author johnniang
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
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
