package me.johnniang.wechat.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.SortedMap;
import java.util.TreeMap;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void checkSignatureSuccessTest() {
        boolean matched = WechatUtils.checkSignature("c579d4e4f8a46938ab6373f467162d645244b2c4", "timestamp", "nonce");

        assertTrue(matched);
    }

    @Test
    public void checkSignatureFailureTest() {
        boolean matched = WechatUtils.checkSignature("c579d4e4f8a46938ab6373f467162d645244b2c4", "another_timestamp", "another_nonce");

        assertTrue(!matched);
    }

    @Test
    public void getSha1Sign() {
        SortedMap<String, Object> toBeSignMap = new TreeMap<>();
        toBeSignMap.put("noncestr", "Wm3WZYTPz0wzccnW");
        toBeSignMap.put("jsapi_ticket", "sM4AOVdWfPE4DxkXGEs8VMCPGGVi4C3VM0P37wVUCFvkVAy_90u5h9nbSlYy3-Sl-HhTdfl2fzFy1AOcHKP7qg");
        toBeSignMap.put("timestamp", "1414587457");
        toBeSignMap.put("url", "http://mp.weixin.qq.com?params=value");

        String signature = WechatUtils.getSha1Sign(toBeSignMap);

        Assert.assertEquals("0f9de62fce790f9a083d5c99e95740ceb90c27ed", signature);
    }

    @Test
    public void getSignBySortedMapTest() {
        SortedMap<String, Object> sortedMap = new TreeMap<>();
        sortedMap.put("test_key", "test_value");
        sortedMap.put("test_key2", "test_value2");
        sortedMap.put("sign", "test_sign");
        sortedMap.put("key", "key_in_sorted_map");

        String signResult = WechatUtils.getMD5SignWithKey(sortedMap, "get_sign_key");

        System.out.println("Sign result: " + signResult.toUpperCase());
        assertThat(signResult, equalToIgnoringCase("EECF53EEF0BBF7FFE888BCE380FAD674"));
    }

}