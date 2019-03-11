package me.johnniang.wechat.support.token;

import lombok.*;
import me.johnniang.wechat.support.WechatBaseResponse;

/**
 * Wechat OAuth2 token (Get by authorized code).
 *
 * @author johnniang
 * @date 12/15/18
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class WechatOAuth2Token extends WechatBaseResponse {

    /**
     * Access token.
     */
    private String accessToken;

    /**
     * Expire seconds.
     */
    private int expiresIn;

    /**
     * Refresh token.
     */
    private String refreshToken;

    /**
     * wechat user openid.
     */
    private String openid;
    /**
     * OAuth2 scope.
     */
    private String scope;

}
