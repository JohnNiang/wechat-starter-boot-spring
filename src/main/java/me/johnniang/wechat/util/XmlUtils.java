package me.johnniang.wechat.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Xml utilities.
 *
 * @author johnniang
 */
public class XmlUtils {

    /**
     * Default xml mapper
     */
    public final static XmlMapper DEFAULT_XML_MAPPER = createDefaultXmlMapper();

    private XmlUtils() {
    }

    /**
     * Creates default xml mapper.
     *
     * @return xml mapper
     */
    public static XmlMapper createDefaultXmlMapper() {
        return createDefaultXmlMapper(null);
    }

    /**
     * Creates default xml mapper.
     *
     * @param strategy property naming strategy
     * @return xml mapper
     */
    public static XmlMapper createDefaultXmlMapper(@Nullable PropertyNamingStrategy strategy) {
        // Create xml mapper
        XmlMapper mapper = new XmlMapper();
        // Configure
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        // Set property naming strategy
        if (strategy != null) {
            mapper.setPropertyNamingStrategy(strategy);
        }

        return mapper;
    }

    /**
     * Converts xml to the object specified type.
     *
     * @param xml  xml content must not be blank
     * @param type object type must not be null
     * @param <T>  target object type
     * @return object specified type
     * @throws IOException throws when fail to convert
     */
    @NonNull
    public static <T> T xmlToObject(@NonNull String xml, @NonNull Class<T> type) throws IOException {
        return xmlToObject(xml, type, DEFAULT_XML_MAPPER);
    }

    /**
     * Converts xml to the object specified type.
     *
     * @param xml       xml content must not be blank
     * @param type      object type must not be null
     * @param xmlMapper xml mapper must not be null
     * @param <T>       target object type
     * @return object specified type
     * @throws IOException throws when fail to convert
     */
    @NonNull
    public static <T> T xmlToObject(@NonNull String xml, @NonNull Class<T> type, @NonNull XmlMapper xmlMapper) throws IOException {
        return JsonUtils.jsonToObject(xml, type, xmlMapper);
    }

    /**
     * Converts input stream to object specified type.
     *
     * @param inputStream input stream must not be null
     * @param type        object type must not be null
     * @param <T>         target object type
     * @return object specified type
     * @throws IOException throws when fail to convert
     */
    @NonNull
    public static <T> T inputStreamToObject(@NonNull InputStream inputStream, @NonNull Class<T> type) throws IOException {
        return inputStreamToObject(inputStream, type, DEFAULT_XML_MAPPER);
    }

    /**
     * Converts input stream to object specified type.
     *
     * @param inputStream input stream must not be null
     * @param type        object type must not be null
     * @param xmlMapper   xml mapper must not be null
     * @param <T>         target object type
     * @return object specified type
     * @throws IOException throws when fail to convert
     */
    @NonNull
    public static <T> T inputStreamToObject(@NonNull InputStream inputStream, @NonNull Class<T> type, @NonNull XmlMapper xmlMapper) throws IOException {
        return JsonUtils.inputStreamToObject(inputStream, type, xmlMapper);
    }

    /**
     * Converts object to xml format.
     *
     * @param source source object must not be null
     * @return xml format of the source object
     * @throws JsonProcessingException throws when fail to convert
     */
    @NonNull
    public static String objectToXml(@NonNull Object source) throws JsonProcessingException {
        return objectToXml(source, DEFAULT_XML_MAPPER);
    }

    /**
     * Converts object to xml format.
     *
     * @param source    source object must not be null
     * @param xmlMapper xml mapper must not be null
     * @return xml format of the source object
     * @throws JsonProcessingException throws when fail to convert
     */
    @NonNull
    public static String objectToXml(@NonNull Object source, @NonNull XmlMapper xmlMapper) throws JsonProcessingException {
        return JsonUtils.objectToJson(source, xmlMapper);
    }

    /**
     * Converts a map to the object specified type.
     *
     * @param sourceMap source map must not be empty
     * @param type      object type must not be null
     * @param <T>       target object type
     * @return the object specified type
     * @throws IOException throws when fail to convert
     */
    @NonNull
    public static <T> T mapToObject(@NonNull Map<String, ?> sourceMap, @NonNull Class<T> type) throws IOException {
        return mapToObject(sourceMap, type, DEFAULT_XML_MAPPER);
    }

    /**
     * Converts a map to the object specified type.
     *
     * @param sourceMap source map must not be empty
     * @param type      object type must not be null
     * @param xmlMapper xml mapper must not be null
     * @param <T>       target object type
     * @return the object specified type
     * @throws IOException throws when fail to convert
     */
    @NonNull
    public static <T> T mapToObject(@NonNull Map<String, ?> sourceMap, @NonNull Class<T> type, @NonNull XmlMapper xmlMapper) throws IOException {
        return JsonUtils.mapToObject(sourceMap, type, xmlMapper);
    }

    /**
     * Converts a source object to a map
     *
     * @param source    source object must not be null
     * @param xmlMapper xml mapper must not be null
     * @return a map
     * @throws IOException throws when fail to convert
     */
    @NonNull
    public static Map<?, ?> objectToMap(@NonNull Object source, @NonNull XmlMapper xmlMapper) throws IOException {
        return JsonUtils.objectToMap(source, xmlMapper);
    }
}
