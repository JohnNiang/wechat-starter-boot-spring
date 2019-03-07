package me.johnniang.wechat.support.message.kefu;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Image media.
 *
 * @author johnniang
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class Image extends AbstractMedia {

    public Image(String mediaId) {
        super(mediaId);
    }

}
