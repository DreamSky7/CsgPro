package vip.mango2.mangocore.Entity.Configuration;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import vip.mango2.mangocore.Utils.ValidUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MangoYamlConfig extends MangoConfiguration {

    private final YamlConfiguration yamlConfig = new YamlConfiguration();

    @Override
    public void Load(File file) throws IOException {
        try {
            yamlConfig.load(file);
        } catch (Exception e) {
            throw new IOException("Yaml文件加载失败，文件名称 [" + file.getName() + "]", e);
        }
    }

    @Override
    public Object get(String path) {
        return yamlConfig.get(path);
    }


    @Override
    public <T> T get(String path, Class<T> clazz) {
        Object rawData = yamlConfig.get(path);
        return getObjectByMap(rawData, clazz);
    }

    public <T> List<T> getList(String path, Class<T> clazz) {
        Object rawData = yamlConfig.get(path);
        if (rawData == null) {
            return null;
        }

        if (!(rawData instanceof List<?>)) {
            throw new RuntimeException("配置项 " + path + " 不是一个列表");
        }

        List<T> resultList = new ArrayList<>();
        List<?> rawList = (List<?>) rawData;
        for (Object item : rawList) {
            if (item == null) {
                resultList.add(null);
            } else if (ValidUtils.isCustomObject(clazz)) {
                // 递归处理自定义对象
                resultList.add(getObjectByMap(item, clazz));
            } else {
                resultList.add(clazz.cast(item));
            }
        }
        return resultList;
    }


    @SuppressWarnings("unchecked")
    private <T> T getObjectByMap(Object rawData, Class<T> clazz) {
        if (rawData == null) {
            return null;
        }

        try {
            T instance = clazz.newInstance();
            Map<String, Object> dataMap;

            if (rawData instanceof ConfigurationSection) {
                ConfigurationSection section = (ConfigurationSection) rawData;
                dataMap = section.getValues(false);
            } else if (rawData instanceof Map<?, ?>) {
                dataMap = (Map<String, Object>) rawData;
            } else {
                throw new IllegalArgumentException("无法处理的原始数据类型: " + rawData.getClass().getName());
            }

            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                Object value = dataMap.get(field.getName());
                if (value != null) {
                    if (ValidUtils.isCustomObject(field.getType())) {
                        // 递归处理自定义对象
                        Object customObject = getObjectByMap(value, field.getType());
                        field.set(instance, customObject);
                    } else if (List.class.isAssignableFrom(field.getType())) {
                        // 处理 List 类型字段
                        List<Object> listValue = new ArrayList<>();
                        ParameterizedType listType = (ParameterizedType) field.getGenericType();
                        Class<?> listItemType = (Class<?>) listType.getActualTypeArguments()[0];

                        for (Object item : (List<?>) value) {
                            if (ValidUtils.isCustomObject(listItemType)) {
                                // 递归处理 List 中的自定义对象
                                Object listItem = getObjectByMap(item, listItemType);
                                listValue.add(listItem);
                            } else {
                                listValue.add(item);
                            }
                        }
                        field.set(instance, listValue);
                    } else {
                        // 处理普通字段
                        field.set(instance, value);
                    }
                }
            }

            return instance;
        } catch (Exception e) {
            throw new RuntimeException("无法从原始数据转换对象 " + clazz.getName(), e);
        }
    }

    @Override
    public void Save(File file) throws IOException {
        yamlConfig.save(file);
    }
//
//    @Override
//    public int getInt(String path) {
//        return yamlConfig.getInt(path);
//    }
//
//    @Override
//    public String getString(String path) {
//        return yamlConfig.getString(path);
//    }
//
//    @Override
//    public double getDouble(String path) {
//        return yamlConfig.getDouble(path);
//    }
//
//    @Override
//    public boolean getBoolean(String path) {
//        return yamlConfig.getBoolean(path);
//    }
//
//    @Override
//    public long getLong(String path) {
//        return yamlConfig.getLong(path);
//    }
//
//    @Override
//    public float getFloat(String path) {
//        return (float) yamlConfig.getDouble(path);
//    }
//
//    @Override
//    public char getChar(String path) {
//        return yamlConfig.getString(path).charAt(0);
//    }
//
//    @Override
//    public List<String> getStringList(String path) {
//        return yamlConfig.getStringList(path);
//    }
//
//    @Override
//    public List<Integer> getIntList(String path) {
//        return yamlConfig.getIntegerList(path);
//    }
//
//    @Override
//    public List<Double> getDoubleList(String path) {
//        return yamlConfig.getDoubleList(path);
//    }

    @Override
    public void set(String path, Object value) {
        yamlConfig.set(path, value);
    }

    @Override
    public <T> Map<String, T> getMap(String path, Class<T> clazz) {
        ConfigurationSection section = yamlConfig.getConfigurationSection(path);
        if (section == null) {
            return null;
        }

        Map<String, T> resultMap = new HashMap<>();
        for (String key : section.getKeys(false)) {
            T value = get(path + "." + key, clazz);
            resultMap.put(key, value);
        }
        return resultMap;
    }

    @Override
    public <T> Map<String, List<T>> getMapList(String path, Class<T> def) {
        ConfigurationSection section = yamlConfig.getConfigurationSection(path);
        if (section == null) {
            return null;
        }

        Map<String, List<T>> resultMap = new HashMap<>();
        for (String key : section.getKeys(false)) {
            List<T> value = getList(path + "." + key, def);
            resultMap.put(key, value);
        }
        return resultMap;
    }

    public void loadFromString(String string) throws InvalidConfigurationException {
        yamlConfig.loadFromString(string);
    }

    public String saveToString() {
        return yamlConfig.saveToString();
    }
}
