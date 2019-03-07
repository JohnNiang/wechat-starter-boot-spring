package me.johnniang.wechat.support.message.kefu;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.johnniang.wechat.support.message.type.MessageType;
import org.springframework.util.Assert;

/**
 * Customer service message.
 *
 * @author johnniang
 */
@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class KfMessage {

    private String touser;

    private MessageType msgtype;

    private Text text;

    private Image image;

    private Voice voice;

    private Video video;

    private Music music;

    private News news;

    public KfMessage(String touser, MessageType msgtype) {
        Assert.hasText(touser, "To user openid must not be blank");
        Assert.notNull(msgtype, "message type must not be null");

        this.touser = touser;
        this.msgtype = msgtype;
    }
}
