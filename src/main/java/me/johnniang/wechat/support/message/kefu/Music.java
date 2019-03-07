package me.johnniang.wechat.support.message.kefu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Music message. (Json format)
 */
@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Music {

    private String title;

    private String description;

    @JsonProperty("musicurl")
    private String musicUrl;

    @JsonProperty("hqmusicurl")
    private String hqMusicUrl;

    @JsonProperty("thumb_media_id")
    private String thumbMediaId;
}
