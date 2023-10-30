package vip.mango2.mangocore.Entity.File.Configuration;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import vip.mango2.mangocore.Entity.File.MangoConfiguration;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

public class MangoJsonFile extends MangoConfiguration {

    private JSONObject jsonConfig;

    public MangoJsonFile(String filePath) {
        super(filePath);
        this.jsonConfig = new JSONObject();
    }

    @Override
    public void onLoad(File file) throws IOException {
        try (FileReader reader = new FileReader(file)) {
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

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String path, Class<T> def) {
        if (def == null) {
            return (T) jsonConfig.get(path);
        }

        T value = (T) jsonConfig.getObject(path, def);
        if (value != null) {
            return value;
        }

        try {
            return def.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException("无法创建 " + def.getName() + " 的实例", e);
        }
    }

    public String getString(String path) {
        return jsonConfig.getString(path);
    }
}
