package me.johnniang.wechat.cache;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * In-memory cache store.
 *
 * @author johnniang
 */
public class InMemoryCacheStore extends WechatCacheStore {

    private final static ConcurrentHashMap<String, String> cacheContainer = new ConcurrentHashMap<>();

    @Override
    public Optional<String> get(String key) {
        return Optional.ofNullable(cacheContainer.get(key));
    }

    @Override
    public void put(String key, String value, long timeout, TimeUnit timeUnit) {
        cacheContainer.put(key, value);
    }

    @Override
    public void delete(String key) {
        cacheContainer.remove(key);
    }
}
