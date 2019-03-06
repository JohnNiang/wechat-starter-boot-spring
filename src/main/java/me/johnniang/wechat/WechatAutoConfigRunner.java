package me.johnniang.wechat;

import lombok.extern.slf4j.Slf4j;
import me.johnniang.wechat.properties.WechatProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Runner of wechat auto configuration.
 *
 * @author johnniang
 */
@Component
@Slf4j
public class WechatAutoConfigRunner implements CommandLineRunner {

    private final WechatProperties wechat;

    public WechatAutoConfigRunner(WechatProperties wechat) {
        this.wechat = wechat;
    }

    @Override
    public void run(String... args) {
        log.trace("Wechat has been configured automatically which content is: [{}]", wechat);
    }
}
