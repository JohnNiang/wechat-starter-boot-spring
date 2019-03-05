package me.johnniang.wechat.config;

import me.johnniang.wechat.service.impl.DefaultWechatServiceImpl;
import me.johnniang.wechat.properties.WechatProperties;
import me.johnniang.wechat.service.WechatService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Wechat auto configuration.
 *
 * @author johnniang
 */
@Configuration
@EnableConfigurationProperties(WechatProperties.class)
@ConditionalOnClass(WechatService.class)
public class WechatAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public WechatService wechatService() {
        return new DefaultWechatServiceImpl();
    }

}
