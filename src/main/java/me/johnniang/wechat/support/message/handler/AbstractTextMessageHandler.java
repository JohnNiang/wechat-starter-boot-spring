package me.johnniang.wechat.support.message.handler;

import me.johnniang.wechat.support.message.WechatMessage;
import me.johnniang.wechat.support.message.type.MessageType;

/**
 * Abstract text message handler.
 *
 * @author johnniang
 */
public abstract class AbstractTextMessageHandler implements WechatMessageHandler {

    @Override
    public boolean supportsMessage(WechatMessage wechatMessage) {
        return MessageType.text.equals(wechatMessage.getMsgType());
    }
}
