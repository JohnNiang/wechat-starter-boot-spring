package me.johnniang.wechat.support.message.kefu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * News message.
 *
 * @author johnniang
 */
@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class News {

    private List<Article> articles;

    /**
     * 图文内容
     *
     * @author johnniang
     */
    @Data
    @EqualsAndHashCode
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Article {

        private String title;

        private String description;

        private String url;

        @JsonProperty("picurl")
        private String picUrl;
    }
}
