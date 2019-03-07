package me.johnniang.wechat.support.message.kefu;

import lombok.extern.slf4j.Slf4j;
import me.johnniang.wechat.support.message.WechatMessage;
import me.johnniang.wechat.support.message.type.MessageType;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.List;

import static me.johnniang.wechat.support.message.kefu.News.Article;

/**
 * Kefu message builder.
 *
 * @author johnniang
 */
@Slf4j
public class KfMessageBuilder {

    private KfMessageBuilder() {
    }

    /**
     * 构建文本客服消息
     *
     * @param touser  接收者
     * @param content 内容
     * @return 返回文本客服消息
     */
    @NonNull
    public static KfMessage buildTextMessage(@NonNull String touser, @NonNull String content) {
        Assert.hasText(content, "content must not be blank");

        KfMessage kfMessage = new KfMessage(touser, MessageType.text);
        Text text = new Text(content);
        kfMessage.setText(text);
        return kfMessage;
    }

    /**
     * 构建图片客服消息
     *
     * @param touser  接收者
     * @param mediaId 图片媒体id
     * @return 图片客服消息
     */
    @NonNull
    public static KfMessage buildImageMessage(@NonNull String touser, @NonNull String mediaId) {
        Assert.hasText(mediaId, "media id must not be blank");

        KfMessage kfMessage = new KfMessage(touser, MessageType.image);
        Image image = new Image(mediaId);
        kfMessage.setImage(image);
        return kfMessage;
    }

    /**
     * Builds voice message.
     *
     * @param touser  to user openid must not be blank
     * @param mediaId media id must not be blank
     * @return voice message
     */
    @NonNull
    public static KfMessage buildVoiceMessage(@NonNull String touser, @NonNull String mediaId) {
        Assert.hasText(mediaId, "media id must not be blank");

        KfMessage kfMessage = new KfMessage(touser, MessageType.voice);
        Voice voice = new Voice(mediaId);
        kfMessage.setVoice(voice);
        return kfMessage;
    }

    /**
     * Builds video message.
     *
     * @param touser       to user openid must not be blank
     * @param mediaId      media id must not be blank
     * @param thumbMediaId thumb media id must not be blank
     * @param title        title
     * @param description  description
     * @return video message
     */
    @NonNull
    public static KfMessage buildVideoMessage(@NonNull String touser, @NonNull String mediaId, @NonNull String thumbMediaId, String title, String description) {
        Assert.hasText(mediaId, "media id must not be blank");
        Assert.hasText(thumbMediaId, "Thumb media id must not be blank");

        KfMessage kfMessage = new KfMessage(touser, MessageType.video);
        kfMessage.setVideo(new Video(mediaId, thumbMediaId, title, description));
        return kfMessage;
    }

    /**
     * 构建图文客服消息
     *
     * @param touser   接收者
     * @param articles 文章列表
     * @return 返回图文客服消息
     */
    @NonNull
    public static KfMessage buildNewsMessage(@NonNull String touser, @NonNull List<Article> articles) {
        Assert.hasText(touser, "to user openid must not be empty");
        Assert.notEmpty(articles, "articles must not be empty");

        KfMessage kfMessage = new KfMessage(touser, MessageType.news);
        News news = new News(articles);
        kfMessage.setNews(news);
        return kfMessage;
    }

    /**
     * Builds kf message from wechat message.
     *
     * @param touser        to user openid must not be blank.
     * @param wechatMessage wechat message must not be null
     * @return kf message building from wechat message; null if not support the wechat message type
     */
    @Nullable
    public static KfMessage buildFromWechatMessage(@NonNull String touser, @NonNull WechatMessage wechatMessage) {
        Assert.notNull(wechatMessage, "Wechat message must not be null");

        KfMessage kfMessage;

        switch (wechatMessage.getMsgType()) {
            case text:
                kfMessage = buildTextMessage(touser, wechatMessage.getContent());
                break;
            case image:
                kfMessage = buildImageMessage(touser, wechatMessage.getMediaId());
                break;
            case voice:
                kfMessage = buildVoiceMessage(touser, wechatMessage.getMediaId());
                break;
            case video:
                kfMessage = buildVideoMessage(touser,
                        wechatMessage.getMediaId(),
                        wechatMessage.getThumbMediaId(),
                        wechatMessage.getTitle(),
                        wechatMessage.getDescription());
                break;
            // TODO Add other kf message builder
            default:
                log.warn("Temporarily not support kf message building from wechat message which type is " + wechatMessage.getMsgType());
                kfMessage = null;
        }

        return kfMessage;
    }

}