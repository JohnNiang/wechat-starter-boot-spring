package me.johnniang.wechat.cache;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Wechat cache store test.
 *
 * @author johnniang
 */
class WechatCacheStoreTest {

    private WechatCacheStore cacheStore;

    @BeforeEach
    public void setUp() {
        cacheStore = new InMemoryCacheStore();
    }

    @Test
    public void putAndGetTest() {
        String key = "test_key";
        cacheStore.putForWechat(key, "test_value", 10, TimeUnit.SECONDS);

        Optional<String> value = cacheStore.getForWechat(key, String.class);

        Assertions.assertTrue(value.isPresent());
        Assertions.assertEquals("test_value", value.get());
    }

    @Test
    public void deleteTest() {
        String key = "test_key";

        cacheStore.putForWechat(key, "test_value", 10, TimeUnit.SECONDS);

        Optional<String> value = cacheStore.getForWechat(key, String.class);

        Assertions.assertTrue(value.isPresent());
        Assertions.assertEquals("test_value", value.get());

        cacheStore.delete(key);

        value = cacheStore.getForWechat(key, String.class);
        Assertions.assertFalse(value.isPresent());
    }

    @Test
    public void expireTest() throws InterruptedException {
        String key = "test_key";

        cacheStore.putForWechat(key, "test_value", 1, TimeUnit.SECONDS);

        TimeUnit.MILLISECONDS.sleep(1010);
        Optional<String> value = cacheStore.getForWechat(key, String.class);

        Assertions.assertFalse(value.isPresent());
    }

}