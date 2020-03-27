package me.johnniang.wechat.util;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

/**
 * JsonUtils test.
 *
 * @author johnniang
 */
@Slf4j
@ActiveProfiles("test")
class JsonUtilsTest {

    @Test
    void jsonToIntegerTest() throws IOException {
        String json = "100";

        Integer number = JsonUtils.jsonToObject(json, Integer.class);

        Assertions.assertEquals(100, number);
    }

    @Test
    void jsonToByteArrayTest() {
        String json = "234123512";
        Assertions.assertThrows(MismatchedInputException.class, () -> JsonUtils.jsonToObject(json, byte[].class));
    }
}