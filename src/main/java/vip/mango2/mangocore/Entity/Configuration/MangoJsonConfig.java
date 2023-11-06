package vip.mango2.mangocore.Entity.Configuration;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MangoJsonConfig extends MangoConfiguration {

    private JSONObject jsonConfig = new JSONObject();

    @Override
    public void Load(InputStream stream) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            jsonConfig = JSON.parseObject(reader, JSONObject.class);
        }
    }

    @Override
    public void Save(OutputStream stream) throws IOException {
        String prettyJsonString = JSON.toJSONString(jsonConfig, JSONWriter.Feature.PrettyFormat);
        try (OutputStreamWriter writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8)) {
            writer.write(prettyJsonString);
            writer.flush();
        }
    }

    @Override
    public void set(String path, Object value) {
        jsonConfig.put(path, value);
    }

    @Override
    public Object get(String path) {
        return jsonConfig.get(path);
    }

    @Override
    public <T> T get(String path, Class<T> def) {
        return jsonConfig.getObject(path, def);
    }
//
//    @Override
//    public int getInt(String path) {
//        return jsonConfig.getInteger(path);
//    }
//
//    @Override
//    public String getString(String path) {
//        return jsonConfig.getString(path);
//    }
//
//    @Override
//    public double getDouble(String path) {
//        return jsonConfig.getDouble(path);
//    }
//
//    @Override
//    public boolean getBoolean(String path) {
//        return jsonConfig.getBoolean(path);
//    }
//
//    @Override
//    public long getLong(String path) {
//        return jsonConfig.getLong(path);
//    }
//
//    @Override
//    public float getFloat(String path) {
//        return jsonConfig.getFloat(path);
//    }
//
//    @Override
//    public char getChar(String path) {
//        return jsonConfig.getString(path).charAt(0);
//    }

    @Override
    public <T> List<T> getList(String path, Class<T> def) {
        if (jsonConfig.getJSONArray(path) != null) {
            return jsonConfig.getJSONArray(path).toJavaList(def);
        }
        return null;
    }
//
//    @Override
//    public List<String> getStringList(String path) {
//        return jsonConfig.getJSONArray(path).toJavaList(String.class);
//    }
//
//    @Override
//    public List<Integer> getIntList(String path) {
//        return jsonConfig.getJSONArray(path).toJavaList(Integer.class);
//    }
//
//    @Override
//    public List<Double> getDoubleList(String path) {
//        return jsonConfig.getJSONArray(path).toJavaList(Double.class);
//    }

    @Override
    public <T> Map<String, T> getMap(String path, Class<T> def) {
        JSONObject jsonObject = jsonConfig.getJSONObject(path);
        if (jsonObject != null) {
            Map<String, T> resultMap = new HashMap<>();
            for (String key : jsonObject.keySet()) {
                T value = jsonObject.getObject(key, def);
                resultMap.put(key, value);
            }
            return resultMap;
        }
        return null;
    }

    @Override
    public <T> Map<String, List<T>> getMapList(String path, Class<T> def) {
        JSONObject jsonObject = jsonConfig.getJSONObject(path);
        if (jsonObject != null) {
            Map<String, List<T>> resultMap = new HashMap<>();
            for (String key : jsonObject.keySet()) {
                List<T> value = jsonObject.getJSONArray(key).toJavaList(def);
                resultMap.put(key, value);
            }
            return resultMap;
        }
        return null;
    }
}
