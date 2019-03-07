package me.johnniang.wechat.support.message.kefu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Video message.
 *
 * @author johnniang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class Video extends AbstractMedia {

    @JsonProperty("thumb_media_id")
    private String thumbMediaId;

    private String title;

    private String description;

    public Video(String mediaId, String thumbMediaId, String title, String description) {
        super(mediaId);
        this.thumbMediaId = thumbMediaId;
        this.title = title;
        this.description = description;
    }
}
