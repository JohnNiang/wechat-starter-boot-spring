package me.johnniang.wechat.config;

import me.johnniang.wechat.cache.InMemoryCacheStore;
import me.johnniang.wechat.cache.WechatCacheStore;
import me.johnniang.wechat.properties.WechatProperties;
import me.johnniang.wechat.service.WechatService;
import me.johnniang.wechat.service.impl.DefaultWechatServiceImpl;
import me.johnniang.wechat.support.message.handler.WechatMessageHandler;
import me.johnniang.wechat.support.message.handler.WechatMessageHandlerComposite;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

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
    public WechatCacheStore cacheStore() {
        return new InMemoryCacheStore();
    }

    @Bean
    @ConditionalOnMissingBean
    public WechatService wechatService(WechatProperties wechatProperties,
                                       WechatCacheStore cacheStore) {
        return new DefaultWechatServiceImpl(wechatProperties, cacheStore);
    }

    @Bean
    public WechatMessageHandlerComposite handlerComposite(final ApplicationContext applicationContext) {
        // Create an empty composite
        WechatMessageHandlerComposite handlerComposite = new WechatMessageHandlerComposite();

        // Get all wechat message handlers
        Map<String, WechatMessageHandler> wechatMessageHandlerMap = applicationContext.getBeansOfType(WechatMessageHandler.class);

        // Add all wechat message handlers to composite
        return handlerComposite.addHanlders(wechatMessageHandlerMap.values());
    }
}
