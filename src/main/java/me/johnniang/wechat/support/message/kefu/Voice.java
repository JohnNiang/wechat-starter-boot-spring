package me.johnniang.wechat.support.message.kefu;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Voice message.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class Voice extends AbstractMedia {

    public Voice(String mediaId) {
        super(mediaId);
    }
}
