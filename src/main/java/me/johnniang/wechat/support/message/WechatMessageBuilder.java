package me.johnniang.wechat.support.message;

import me.johnniang.wechat.support.message.type.MessageType;
import org.springframework.lang.NonNull;

/**
 * Wechat message builder.
 */
public class WechatMessageBuilder {

    private WechatMessageBuilder() {
    }

    /**
     * Build wechat text message.
     *
     * @param toUserName   official account owner.
     * @param fromUserName subscriber
     * @param createTime   create time
     * @param content      content
     * @return wechat text message
     */
    public static WechatMessage buildTextMessage(@NonNull String toUserName,
                                                 @NonNull String fromUserName,
                                                 @NonNull Long createTime,
                                                 @NonNull String content) {
        WechatMessage textMessage = new WechatMessage(toUserName, fromUserName, createTime, MessageType.text);
        textMessage.setContent(content);
        return textMessage;
    }
}
