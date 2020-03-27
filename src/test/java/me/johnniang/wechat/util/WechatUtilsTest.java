package me.johnniang.wechat.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.SortedMap;
import java.util.TreeMap;

@Slf4j
@ActiveProfiles("test")
class WechatUtilsTest {

    @Test
    void requestAndResponseStringTest() {
        String responseContent = WechatUtils.request("https://cn.bing.com/", "get", null, String.class, JsonUtils.DEFAULT_JSON_MAPPER);

        log.debug("Response content: [{}]", responseContent);
    }

    @Test
    void requestAndResponseByteArrasyTest() {
        byte[] responseContent = WechatUtils.request("https://cn.bing.com/", "get", null, byte[].class, JsonUtils.DEFAULT_JSON_MAPPER);

        log.debug("Response content: [{}]", responseContent);
    }

    @Test
    void getCurrentTimestampTest() {
        long curTimestamp = WechatUtils.getCurrentTimestamp();

        Assertions.assertTrue(curTimestamp >= System.currentTimeMillis() / 1000);
    }

    @Test
    void checkSignatureSuccessTest() {
        boolean matched = WechatUtils.checkSignature("c579d4e4f8a46938ab6373f467162d645244b2c4", "timestamp", "nonce");

        Assertions.assertTrue(matched);
    }

    @Test
    void checkSignatureFailureTest() {
        boolean matched = WechatUtils.checkSignature("c579d4e4f8a46938ab6373f467162d645244b2c4", "another_timestamp", "another_nonce");

        Assertions.assertFalse(matched);
    }

    @Test
    void getSha1Sign() {
        SortedMap<String, Object> toBeSignMap = new TreeMap<>();
        toBeSignMap.put("noncestr", "Wm3WZYTPz0wzccnW");
        toBeSignMap.put("jsapi_ticket", "sM4AOVdWfPE4DxkXGEs8VMCPGGVi4C3VM0P37wVUCFvkVAy_90u5h9nbSlYy3-Sl-HhTdfl2fzFy1AOcHKP7qg");
        toBeSignMap.put("timestamp", "1414587457");
        toBeSignMap.put("url", "http://mp.weixin.qq.com?params=value");

        String signature = WechatUtils.getSha1Sign(toBeSignMap);

        Assertions.assertEquals("0f9de62fce790f9a083d5c99e95740ceb90c27ed", signature);
    }

    @Test
    void getSignBySortedMapTest() {
        SortedMap<String, Object> sortedMap = new TreeMap<>();
        sortedMap.put("test_key", "test_value");
        sortedMap.put("test_key2", "test_value2");
        sortedMap.put("sign", "test_sign");
        sortedMap.put("key", "key_in_sorted_map");

        String signResult = WechatUtils.getMD5SignWithKey(sortedMap, "get_sign_key");

        System.out.println("Sign result: " + signResult.toUpperCase());

        Assertions.assertEquals("EECF53EEF0BBF7FFE888BCE380FAD674", signResult.toUpperCase());
    }

}