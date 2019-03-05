package me.johnniang.wechat;

import me.johnniang.wechat.properties.WechatProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * Runner of wechat auto configuration.
 *
 * @author johnniang
 */
@Component
public class WechatAutoConfigRunner implements CommandLineRunner {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final WechatProperties wechat;

    public WechatAutoConfigRunner(WechatProperties wechat) {
        this.wechat = wechat;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Wechat has been configured automatically.\n" + wechat);
    }
}
