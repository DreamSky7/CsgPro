package vip.mango2.mangocore.Entity.File.Configuration;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;
import vip.mango2.mangocore.Entity.File.MangoConfiguration;
import vip.mango2.mangocore.Entity.File.MangoFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class MangoYamlFile extends MangoConfiguration {

    private final YamlConfiguration yamlConfig;


    public MangoYamlFile(String filePath) {
        super(filePath);
        this.yamlConfig = new YamlConfiguration();
    }

    @Override
    public void onLoad(File file) throws IOException {
        try {
            yamlConfig.load(file);
        } catch (Exception e) {
            throw new IOException("Yaml文件加载失败，文件名称 [" + file.getName() + "]", e);
        }
    }

    @Override
    public void onSave(File file) throws IOException {
        yamlConfig.save(file);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String path, Class<T> defClass) {
        Object value = yamlConfig.get(path);
        if (value != null) {
            if (defClass == null || defClass.isInstance(value)) {
                return (T) value;
            } else {
                throw new ClassCastException("无法将配置项 " + path + " 的值 " + value.getClass().getName() + " 转换为 " + defClass.getName());
            }
        }

        if (defClass != null) {
            try {
                return defClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException("无法创建 " + defClass.getName() + " 的实例", e);
            }
        }

        return null;
    }


    @Override
    public void set(String path, Object value) {
        yamlConfig.set(path, value);
    }


    public void loadFromString(String string) throws InvalidConfigurationException {
        yamlConfig.loadFromString(string);
    }
}
