package me.johnniang.wechat.controller;

import lombok.extern.slf4j.Slf4j;
import me.johnniang.wechat.service.WechatService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Base wechat controller.
 *
 * @author johnniang
 */
@Slf4j
public abstract class BaseWechatController {

    @Autowired
    private WechatService wechatService;

    /**
     * Validate signature
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echoStr
     * @return
     */
    public String validate(String signature,
                           String timestamp,
                           String nonce,
                           String echoStr) {

        // Check signature
        if (!wechatService.checkSignature(signature, timestamp, nonce)) {
            // Return empty string to wechat server
            return "";
        }

        // Return echo string to wechat server
        return echoStr;
    }

}
