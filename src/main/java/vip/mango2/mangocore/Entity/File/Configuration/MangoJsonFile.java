package vip.mango2.mangocore.Entity.File.Configuration;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import vip.mango2.mangocore.Entity.File.MangoConfiguration;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MangoJsonFile extends MangoConfiguration {

    private JSONObject jsonConfig;

    public MangoJsonFile(String filePath) {
        super(filePath);
        this.jsonConfig = new JSONObject();
    }

    @Override
    public void onLoad(File file) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(Files.newInputStream(file.toPath()), StandardCharsets.UTF_8)) {
            jsonConfig = JSON.parseObject(reader, JSONObject.class);
        }
    }

    @Override
    public void onSave(File file) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(jsonConfig.toJSONString());
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

    @Override
    public int getInt(String path) {
        return jsonConfig.getInteger(path);
    }

    @Override
    public String getString(String path) {
        return jsonConfig.getString(path);
    }

    @Override
    public double getDouble(String path) {
        return jsonConfig.getDouble(path);
    }

    @Override
    public boolean getBoolean(String path) {
        return jsonConfig.getBoolean(path);
    }

    @Override
    public long getLong(String path) {
        return jsonConfig.getLong(path);
    }

    @Override
    public float getFloat(String path) {
        return jsonConfig.getFloat(path);
    }

    @Override
    public char getChar(String path) {
        return jsonConfig.getString(path).charAt(0);
    }

    @Override
    public <T> List<T> getList(String path, Class<T> def) {
        return jsonConfig.getJSONArray(path).toJavaList(def);
    }

    @Override
    public List<String> getStringList(String path) {
        return jsonConfig.getJSONArray(path).toJavaList(String.class);
    }

    @Override
    public List<Integer> getIntList(String path) {
        return jsonConfig.getJSONArray(path).toJavaList(Integer.class);
    }

    @Override
    public List<Double> getDoubleList(String path) {
        return jsonConfig.getJSONArray(path).toJavaList(Double.class);
    }

    @Override
    public <T> Map<String, T> getStringMap(String path, Class<T> def) {
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
}
