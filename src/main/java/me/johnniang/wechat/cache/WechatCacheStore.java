package me.johnniang.wechat.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import me.johnniang.wechat.exception.WechatException;
import me.johnniang.wechat.util.JsonUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Wechat cache store.
 *
 * @author johnniang
 */
@Slf4j
public abstract class WechatCacheStore implements CacheStore<String, String> {

    public <T> void putForWechat(String key, T value, long timeout, TimeUnit timeUnit) {
        Assert.hasText(key, "Cache key must not be blank");
        Assert.notNull(value, "Cache value must not be null");
        Assert.isTrue(timeout > 0, "Timeout must not be less than 0");
        Assert.notNull(timeUnit, "Time unit must not be null");

        // Convert to second
        Long seconds = timeUnit.toSeconds(timeout);

        // Round the seconds
        if (seconds == 0) {
            seconds = 1L;
        }

        Date now = new Date();

        // Calculate expire at
        Date expireAt = DateUtils.addSeconds(now, seconds.intValue());

        // Build cache wrapper
        CacheWrapper<T> wrapper = new CacheWrapper<>();
        wrapper.setCreateAt(now);
        wrapper.setExpireAt(expireAt);
        wrapper.setKey(key);
        wrapper.setData(value);

        try {
            // Convert wrapper to json
            String valueJson = JsonUtils.objectToJson(wrapper);
            // Put the the value json to cache store
            put(key, valueJson, timeout, timeUnit);
        } catch (JsonProcessingException e) {
            throw new WechatException("Failed to convert object to json", e).setData(wrapper);
        }
    }

    @SuppressWarnings("unchecked")
    @NonNull
    public <T> Optional<T> getForWechat(@NonNull String key, @NonNull Class<T> type) {
        Assert.hasText(key, "Cache key must not be blank");
        Assert.notNull(type, "Cache type must not be null");

        return get(key).map(value -> {
            try {
                CacheWrapper<?> cacheWrapper = JsonUtils.jsonToObject(value, CacheWrapper.class);

                if (cacheWrapper == null) {
                    log.error("Cache wrapper is null, key: [{}]", key);
                    return null;
                }

                log.debug("Cache wrapper: [{}]", cacheWrapper);

                Date now = new Date();

                if (cacheWrapper.getExpireAt().before(now)) {
                    // Expired then delete it
                    log.debug("Cache key: [{}] has been expired", key);

                    delete(key);
                    return null;
                }

                Object data = cacheWrapper.getData();

                if (data != null && data.getClass().isAssignableFrom(type)) {
                    return (T) data;
                }

                log.error("Data type: [{}], but specified type: [{}]", data == null ? null : data.getClass(), type);
                throw new WechatException("Cache value type is not mismatch with the specified type");
            } catch (IOException e) {
                throw new WechatException("Failed to convert from json to object", e).setData(value);
            }
        });
    }

}
