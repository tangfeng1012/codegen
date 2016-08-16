package com.hhly.codgen.util;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;


public class JSONUtils {

    private final static ObjectMapper objectMapper = new ObjectMapper();


    private JSONUtils() {

    }

    static {
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static ObjectMapper getInstance() {

        return objectMapper;
    }

    /**
     * javaBean,list,array convert to json string
     */
    public static String obj2json(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }


    /**
     * javaBean,list,array convert to json string
     */
    public static String obj2json(Object obj, Class view) throws Exception {

        return objectMapper.writerWithView(view).writeValueAsString(obj);
    }


    /**
     * json string convert to javaBean
     */
    public static <T> T json2pojo(String jsonStr, Class<T> clazz)
            throws Exception {
        return objectMapper.readValue(jsonStr, clazz);
    }

    /**
     * json string convert to javaBean
     */
    public static <T> T safeJson2pojo(String jsonStr, Class<T> clazz)
            throws Exception {
        if (jsonStr != null) {
            return json2pojo(jsonStr, clazz);
        } else {
            return null;
        }
    }

    /**
     * json string convert to map
     */
    public static <T> Map<String, Object> json2map(String jsonStr)
            throws Exception {
        return objectMapper.readValue(jsonStr, Map.class);
    }

    /**
     * json string convert to map with javaBean
     */
    public static <T> Map<String, T> json2map(String jsonStr, Class<T> clazz)
            throws Exception {
        Map<String, Map<String, Object>> map = objectMapper.readValue(jsonStr,
                new TypeReference<Map<String, T>>() {
                });
        Map<String, T> result = new HashMap<String, T>();
        for (Entry<String, Map<String, Object>> entry : map.entrySet()) {
            result.put(entry.getKey(), map2pojo(entry.getValue(), clazz));
        }
        return result;
    }

    /**
     * json string convert to map with javaBean
     */
    public static <T> Map<String, T> json2LinkMap(String jsonStr, Class<T> clazz)
            throws Exception {
        Map<String, Map<String, Object>> map = objectMapper.readValue(jsonStr,
                new TypeReference<Map<String, T>>() {
                });
        Map<String, T> result = new LinkedHashMap<>();
        for (Entry<String, Map<String, Object>> entry : map.entrySet()) {
            result.put(entry.getKey(), map2pojo(entry.getValue(), clazz));
        }
        return result;
    }


    public static <T> Map<String, List<T>> json2mapList(String jsonStr, Class<T> clazz) throws Exception {
        Map<String, List<T>> maps = new LinkedHashMap<String, List<T>>();
        Map<String, Object> map = json2map(jsonStr);
        for (String key : map.keySet()) {
            Object value = map.get(key);
            List<Object> list = (List<Object>) value;
            List<T> tt = new ArrayList<T>();
            for (Object l : list) {
                T t = map2pojo((Map) l, clazz);
                tt.add(t);
            }
            maps.put(key, tt);
        }
        return maps;
    }

    public static <T> Map<Integer, List<T>> json2mapIntList(String jsonStr, Class<T> clazz) throws Exception {
        Map<Integer, List<T>> maps = new LinkedHashMap<Integer, List<T>>();
        Map<String, Object> map = json2map(jsonStr);
        for (String key : map.keySet()) {
            Object value = map.get(key);
            List<Object> list = (List<Object>) value;
            List<T> tt = new ArrayList<T>();
            for (Object l : list) {
                T t = map2pojo((Map) l, clazz);
                tt.add(t);
            }
            maps.put(Integer.parseInt(key), tt);
        }
        return maps;
    }

    public static <T> List<T> safeJson2list(String jsonArrayStr, Class<T> clazz) throws Exception {
        if (jsonArrayStr != null) {
            return json2list(jsonArrayStr, clazz);
        } else {
            return null;
        }
    }

    /**
     * json array string convert to list with javaBean
     */
    public static <T> List<T> json2list(String jsonArrayStr, Class<T> clazz)
            throws Exception {
        List<Map<String, Object>> list = objectMapper.readValue(jsonArrayStr,
                new TypeReference<List<T>>() {
                });
        List<T> result = new ArrayList<T>();
        for (Map<String, Object> map : list) {
            result.add(map2pojo(map, clazz));
        }
        return result;
    }

    /**
     * json array string convert to list with javaBean
     */
    public static <T> List<T> json2ListString(String jsonArrayStr, Class<T> clazz)
            throws Exception {
        List<T> list = objectMapper.readValue(jsonArrayStr,
                new TypeReference<List<T>>() {
                });
        return list;
    }

    /**
     * map convert to javaBean
     */
    public static <T> T map2pojo(Map map, Class<T> clazz) {
        return objectMapper.convertValue(map, clazz);
    }


    public static void obj2Writer(Writer writer, Object jsonObj, Class jsonView) throws JsonGenerationException, JsonMappingException, IOException {
        if (jsonView == null) {
            objectMapper.writeValue(writer, jsonObj);
        } else {
            objectMapper.writerWithView(jsonView).writeValue(writer, jsonObj);
        }
    }


    public static byte[] obj2Byte(Object jsonObj, Class jsonView) throws JsonGenerationException, JsonMappingException, IOException {
        if (jsonView == null) {
            return objectMapper.writeValueAsBytes(jsonObj);
        } else {
            return objectMapper.writerWithView(jsonView).writeValueAsBytes(jsonObj);
        }
    }


    public static byte[] obj2Byte(Object jsonObj) throws JsonGenerationException, JsonMappingException, IOException {
        return obj2Byte(jsonObj, null);
    }

}
