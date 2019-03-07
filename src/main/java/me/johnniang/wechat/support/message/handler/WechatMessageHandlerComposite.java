package me.johnniang.wechat.support.message.handler;

import lombok.extern.slf4j.Slf4j;
import me.johnniang.wechat.support.message.WechatMessage;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Resolves wechat messages by delegating to a list of registered WechatMessageHandler.
 * Previously wechat message handlers are cached for faster lookups.
 *
 * @author johnniang
 */
@Slf4j
public class WechatMessageHandlerComposite implements WechatMessageHandler {

    private final List<WechatMessageHandler> messageHandlers = new LinkedList<>();

    private WechatMessageHandler postMessageHandler;

    private final Map<WechatMessage, WechatMessageHandler> messageHandlersCache = new ConcurrentHashMap<>(256);

    @Override
    public boolean supportsMessage(WechatMessage wechatMessage) {
        return getWechatMessageHandler(wechatMessage) != null;
    }

    @Override
    public WechatMessage handleMessage(WechatMessage wechatMessage) {
        WechatMessageHandler handler = getWechatMessageHandler(wechatMessage);

        WechatMessage passiveMessage = null;

        if (handler != null) {
            // Handle message
            passiveMessage = handler.handleMessage(wechatMessage);
        } else {
            log.warn("Unsupported wechat message: [{}]", wechatMessage);
        }

        // Post handling message
        if (postMessageHandler != null && postMessageHandler.supportsMessage(wechatMessage)) {
            postMessageHandler.handleMessage(wechatMessage);
        }

        return passiveMessage;
    }

    public void setPostMessageHandler(WechatMessageHandler postMessageHandler) {
        this.postMessageHandler = postMessageHandler;
    }

    public WechatMessageHandler getPostMessageHandler() {
        return postMessageHandler;
    }

    public WechatMessageHandlerComposite addHandler(@NonNull WechatMessageHandler handler) {
        Assert.notNull(handler, "Wechat message handler must not be null");

        this.messageHandlers.add(handler);
        return this;
    }

    public WechatMessageHandlerComposite addHandlers(@NonNull WechatMessageHandler... handlers) {
        for (WechatMessageHandler handler : handlers) {
            addHandler(handler);
        }

        return this;
    }

    public WechatMessageHandlerComposite addHanlders(@NonNull Collection<? extends WechatMessageHandler> handlers) {
        if (!CollectionUtils.isEmpty(handlers)) {
            handlers.forEach(this::addHandler);
        }

        return this;
    }

    public List<WechatMessageHandler> getMessageHandlers() {
        return Collections.unmodifiableList(messageHandlers);
    }

    public void clear() {
        messageHandlersCache.clear();
        messageHandlers.clear();
    }

    private WechatMessageHandler getWechatMessageHandler(WechatMessage message) {
        Assert.notNull(message, "Wechat message must not be null");

        // Get cached handler
        WechatMessageHandler cachedHandler = this.messageHandlersCache.get(message);

        if (cachedHandler == null) {
            log.debug("Getting new handler for wechat message");
            for (WechatMessageHandler handler : messageHandlers) {
                if (handler.supportsMessage(message)) {
                    // Cache this handler
                    cachedHandler = handler;
                    this.messageHandlersCache.put(message, cachedHandler);
                    break;
                }
            }
        } else {
            log.debug("Got cached handler: [{}]", cachedHandler);
        }

        return cachedHandler;
    }
}
