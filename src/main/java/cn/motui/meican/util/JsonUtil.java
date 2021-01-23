package cn.motui.meican.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author motui
 * @date 2021-01-16
 */
public class JsonUtil {
  private static final ObjectMapper MAPPER = new ObjectMapper();

  static {
    MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
  }

  public static String toString(Object obj) {
    try {
      return MAPPER.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Json Processing Exception", e);
    }
  }

  public static <M> M from(String value, Class<M> clazz) {
    try {
      return MAPPER.readValue(value, clazz);
    } catch (IOException e) {
      throw new RuntimeException("io exception", e);
    }
  }

  public static <M> M from(String value, TypeReference<M> clazz) {
    try {
      return MAPPER.readValue(value, clazz);
    } catch (IOException e) {
      throw new RuntimeException("io exception", e);
    }
  }

}
