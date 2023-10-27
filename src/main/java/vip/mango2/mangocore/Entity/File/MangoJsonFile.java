package vip.mango2.mangocore.Entity.File;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MangoJsonFile extends MangoFile {

    private JSONObject jsonConfig;

    public MangoJsonFile(String filePath) {
        super(filePath);
        this.jsonConfig = new JSONObject();
    }

    @Override
    public void load() throws IOException {
        try (FileReader reader = new FileReader(file)) {
            jsonConfig = JSON.parseObject(reader, JSONObject.class);
            loaded = true;
        }
    }

    @Override
    public void save() throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(jsonConfig.toJSONString());
            writer.flush();
        }
    }

    @Override
    public Object get(String path) {
        return jsonConfig.get(path);
    }

    @Override
    public void set(String path, Object value) {
        jsonConfig.put(path, value);
    }
}
