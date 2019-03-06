package me.johnniang.wechat.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;

@Slf4j
@ActiveProfiles("test")
public class WechatUtilsTest {

    @Test
    public void requestAndResponseStringTest() {
        String responseContent = WechatUtils.request("http://www.bing.com", "get", null, String.class, JsonUtils.DEFAULT_JSON_MAPPER);

        log.debug("Response content: [{}]", responseContent);
    }

    @Test
    public void requestAndResponseByteArrasyTest() {
        byte[] responseContent = WechatUtils.request("http://www.bing.com", "get", null, byte[].class, JsonUtils.DEFAULT_JSON_MAPPER);

        log.debug("Response content: [{}]", responseContent);
    }

    @Test
    public void getCurrentTimestampTest() {
        long curTimestamp = WechatUtils.getCurrentTimestamp();

        assertThat(curTimestamp, greaterThanOrEqualTo(System.currentTimeMillis() / 1000));
    }
}