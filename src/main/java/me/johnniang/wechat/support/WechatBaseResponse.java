package me.johnniang.wechat.support;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Wechat base response.
 *
 * @author johnniang
 */
@Data
@EqualsAndHashCode
@ToString
public class WechatBaseResponse {

    public final static int SYSTEM_BUSY = -1;

    public final static int SUCCESS = 0;

    public final static int ACCESS_TOKEN_ERROR = 40001;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private int errcode;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String errmsg;

    public String printErrorDetail() {
        return "errorcode=" + errcode + ", errmsg=" + errmsg;
    }
}
