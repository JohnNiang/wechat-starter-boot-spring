package me.johnniang.wechat.controller;

import lombok.extern.slf4j.Slf4j;
import me.johnniang.wechat.service.WechatService;
import me.johnniang.wechat.support.message.WechatMessage;
import me.johnniang.wechat.support.message.handler.WechatMessageHandlerComposite;
import me.johnniang.wechat.util.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Base wechat controller.
 *
 * @author johnniang
 */
@Slf4j
public abstract class DefaultWechatController {

    @Autowired
    protected WechatService wechatService;

    @Autowired
    protected WechatMessageHandlerComposite messageHandlerComposite;

    /**
     * Validate signature from wechat.
     *
     * @param signature signature from wechat
     * @param timestamp timestamp from wechat
     * @param nonce     nonce from wechat
     * @param echoStr   echoStr from wechat
     * @return echoStr from wechat
     */
    @GetMapping
    public String validate(@RequestParam("signature") String signature,
                           @RequestParam("timestamp") String timestamp,
                           @RequestParam("nonce") String nonce,
                           @RequestParam("echostr") String echoStr) {

        // Check signature
        if (!wechatService.checkSignature(signature, timestamp, nonce)) {
            // Return empty string to wechat server
            return "";
        }

        // Return echo string to wechat server
        return echoStr;
    }

    /**
     * Handle wechat message.
     *
     * @param request http servlet request (Auto inject)
     * @return passive wechat message
     * @throws IOException throws when HttpServletRequest#getInputStream() invokes error
     */
    @PostMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public WechatMessage handleWechatMessage(HttpServletRequest request) throws IOException {
        // Convert to wechat message
        WechatMessage wechatMessage = XmlUtils.inputStreamToObject(request.getInputStream(), WechatMessage.class);

        log.debug("Received wechat message: [{}]", wechatMessage);

        // handle the message.
        WechatMessage result = messageHandlerComposite.handleMessage(wechatMessage);

        log.debug("Handled message result: [{}]", result);

        return result;
    }

}
