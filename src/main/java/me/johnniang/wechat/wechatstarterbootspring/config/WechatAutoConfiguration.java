package me.johnniang.wechat.wechatstarterbootspring.config;

import me.johnniang.wechat.wechatstarterbootspring.properties.WechatProperties;
import me.johnniang.wechat.wechatstarterbootspring.service.WechatService;
import me.johnniang.wechat.wechatstarterbootspring.service.impl.DefaultWechatServiceImpl;
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
