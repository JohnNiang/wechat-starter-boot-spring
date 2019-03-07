package me.johnniang.wechat.support.message.handler;

import me.johnniang.wechat.support.message.WechatMessage;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Strategy interface for handling wechat message.
 *
 * @author johnniang
 */
public interface WechatMessageHandler {

    /**
     * Whether the given message is supported by this handler.
     *
     * @param wechatMessage wechat message to check must not be null
     * @return true if this handler supports the given message; false otherwise
     */
    boolean supportsMessage(@NonNull WechatMessage wechatMessage);

    /**
     * Handles a wechat message and return an passive response message.
     *
     * @param wechatMessage wechat message to handle must not be null
     * @return passive response message or null if response nothing
     */
    @Nullable
    WechatMessage handleMessage(@NonNull WechatMessage wechatMessage);
}
