package me.johnniang.wechat.support.message.kefu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Abstract media. (Json format)
 *
 * @author johnniang
 */
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractMedia {

    /**
     * Media id
     */
    @JsonProperty("media_id")
    protected String mediaId;
}
