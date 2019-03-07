package me.johnniang.wechat.support.message;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import me.johnniang.wechat.support.message.type.EventType;
import me.johnniang.wechat.support.message.type.MessageType;

/**
 * Wechat message. (Xml format)
 *
 * @author johnniang
 */
@JacksonXmlRootElement(localName = "xml")
public class WechatMessage {

    /**
     * 消息id，64位整型.
     */
    @JacksonXmlProperty(localName = "MsgId")
    @JacksonXmlCData
    private String msgId;

    /**
     * 开发者微信号.
     */
    @JacksonXmlProperty(localName = "ToUserName")
    @JacksonXmlCData
    private String toUserName;

    /**
     * 发送方帐号（一个OpenID）
     */
    @JacksonXmlProperty(localName = "FromUserName")
    @JacksonXmlCData
    private String fromUserName;

    /**
     * 消息创建时间 （整型）
     */
    @JacksonXmlProperty(localName = "CreateTime")
    @JacksonXmlCData
    private Long createTime;

    /**
     * Message type.
     */
    @JacksonXmlProperty(localName = "MsgType")
    @JacksonXmlCData
    private MessageType msgType;

    /**
     * 文本消息内容
     */
    @JacksonXmlProperty(localName = "Content")
    @JacksonXmlCData
    private String content;

    /**
     * 图片消息媒体id，可以调用多媒体文件下载接口拉取数据.
     */
    @JacksonXmlProperty(localName = "MediaId")
    @JacksonXmlCData
    private String mediaId;

    /**
     * 消息标题.
     */
    @JacksonXmlProperty(localName = "Title")
    @JacksonXmlCData
    private String title;

    /**
     * 消息描述.
     */
    @JacksonXmlProperty(localName = "Description")
    @JacksonXmlCData
    private String description;

    /**
     * 音乐链接.
     */
    @JacksonXmlProperty(localName = "MusicURL")
    @JacksonXmlCData
    private String musicURL;

    /**
     * 高质量音乐链接，WIFI环境优先使用该链接播放音乐.
     */
    @JacksonXmlProperty(localName = "HQMusicUrl")
    @JacksonXmlCData
    private String hQMusicUrl;

    /**
     * 缩略图的媒体id，通过素材管理中的接口上传多媒体文件，得到的id.
     */
    @JacksonXmlProperty(localName = "ThumbMediaId")
    @JacksonXmlCData
    private String thumbMediaId;

    /**
     * 图文消息个数；当用户发送文本、图片、视频、图文、地理位置这五种消息时，开发者只能回复1条图文消息；其余场景最多可回复8条图文消息.
     */
    @JacksonXmlProperty(localName = "ArticleCount")
    @JacksonXmlCData
    private String articleCount;

    /**
     * 图文消息信息，注意，如果图文数超过限制，则将只发限制内的条数.
     */
    @JacksonXmlProperty(localName = "Articles")
    @JacksonXmlCData
    private String articles;

    /**
     * 图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200.
     */
    @JacksonXmlProperty(localName = "PicUrl")
    @JacksonXmlCData
    private String picUrl;

    /**
     * 点击图文消息跳转链接.
     */
    @JacksonXmlProperty(localName = "Url")
    @JacksonXmlCData
    private String url;

    /**
     * 事件类型.
     */
    @JacksonXmlProperty(localName = "Event")
    @JacksonXmlCData
    private EventType event;

    /**
     * 事件KEY值.
     */
    @JacksonXmlProperty(localName = "EventKey")
    @JacksonXmlCData
    private String eventKey;

    /**
     * 二维码的ticket，可用来换取二维码图片.
     */
    @JacksonXmlProperty(localName = "Ticket")
    @JacksonXmlCData
    private String ticket;

    /**
     * 地理位置纬度.
     */
    @JacksonXmlProperty(localName = "Latitude")
    @JacksonXmlCData
    private Double latitude;

    /**
     * 地理位置经度.
     */
    @JacksonXmlProperty(localName = "Longitude")
    @JacksonXmlCData
    private Double longitude;

    /**
     * 地理位置精度.
     */
    @JacksonXmlProperty(localName = "Precision")
    @JacksonXmlCData
    private Double precision;
}