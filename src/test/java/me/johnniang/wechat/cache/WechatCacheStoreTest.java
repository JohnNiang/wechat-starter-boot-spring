package me.johnniang.wechat.cache;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

/**
 * Wechat cache store test.
 *
 * @author johnniang
 */
public class WechatCacheStoreTest {

    private WechatCacheStore cacheStore;

    @Before
    public void setUp() {
        cacheStore = new InMemoryCacheStore();
    }

    @Test
    public void putAndGetTest() {
        String key = "test_key";
        cacheStore.putForWechat(key, "test_value", 10, TimeUnit.SECONDS);

        Optional<String> value = cacheStore.getForWechat(key, String.class);

        assertTrue(value.isPresent());
        assertThat(value.get(), equalTo("test_value"));
    }

    @Test
    public void deleteTest() {
        String key = "test_key";

        cacheStore.putForWechat(key, "test_value", 10, TimeUnit.SECONDS);

        Optional<String> value = cacheStore.getForWechat(key, String.class);

        assertTrue(value.isPresent());
        assertThat(value.get(), equalTo("test_value"));

        cacheStore.delete(key);

        value = cacheStore.getForWechat(key, String.class);
        assertFalse(value.isPresent());
    }

    @Test
    public void expireTest() throws InterruptedException {
        String key = "test_key";

        cacheStore.putForWechat(key, "test_value", 1, TimeUnit.SECONDS);

        TimeUnit.MILLISECONDS.sleep(1010);

        Optional<String> value = cacheStore.getForWechat(key, String.class);


        assertTrue(!value.isPresent());
    }

}