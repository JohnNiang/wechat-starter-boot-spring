package me.johnniang.wechat.util;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * JsonUtils test.
 *
 * @author johnniang
 */
@Slf4j
@ActiveProfiles("test")
public class JsonUtilsTest {

    @Test
    public void jsonToIntegerTest() throws IOException {
        String json = "100";

        Integer number = JsonUtils.jsonToObject(json, Integer.class);

        assertThat(number, equalTo(100));
    }

    @Test(expected = MismatchedInputException.class)
    public void jsonToByteArrayTest() throws IOException {
        String json = "234123512";

        byte[] bytes = JsonUtils.jsonToObject(json, byte[].class);
    }
}