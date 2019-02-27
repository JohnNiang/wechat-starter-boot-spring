package me.johnniang.wechat.wechatstarterbootspring;

import me.johnniang.wechat.wechatstarterbootspring.properties.WechatProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Runner of wechat auto configuration.
 *
 * @author johnniang
 */
@Component
public class WechatAutoConfigRunner implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final WechatProperties wechat;

    public WechatAutoConfigRunner(WechatProperties wechat) {
        this.wechat = wechat;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Wechat has been configured automatically.\n[{}]", wechat);
    }
}
