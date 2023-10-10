package vip.mango2.mangocore.Utils;

import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class YamlUtils {


    /**
     * 保存对象到配置文件
     * @param config 配置文件
     * @param obj 对象
     * @param parentPath 父路径
     */
    public static void saveObjectToConfig(FileConfiguration config, Object obj, String parentPath) {
        if (obj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) obj;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                String key = entry.getKey().toString();
                Object value = entry.getValue();
                String fullPath = parentPath.isEmpty() ? key : parentPath + "." + key;
                saveObjectToConfig(config, value, fullPath);
            }
        } else if (obj instanceof List) {
            List<?> list = (List<?>) obj;
            List<Object> newList = new ArrayList<>();
            for (Object item : list) {
                if (item instanceof String || isPrimitiveOrWrapper(item.getClass())) {
                    newList.add(item);
                } else {
                    Map<String, Object> serializedItem = serializeObject(item);
                    newList.add(serializedItem);
                }
            }
            config.set(parentPath, newList);
        } else if (isPrimitiveOrWrapper(obj.getClass()) || obj instanceof String) {
            config.set(parentPath, obj);
        } else {
            Map<String, Object> serializedObj = serializeObject(obj);
            for (Map.Entry<String, Object> entry : serializedObj.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                String fullPath = parentPath.isEmpty() ? key : parentPath + "." + key;
                saveObjectToConfig(config, value, fullPath);
            }
        }
    }

    public static Map<String, Object> serializeObject(Object obj) {
        Map<String, Object> serialized = new LinkedHashMap<>();
        Class<?> aClass = obj.getClass();
        for (Field field : aClass.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                if (value != null) {  // 只处理非null的属性
                    if (value instanceof List) {
                        // 如果序列化的值为List类型
                        List<?> originalList = (List<?>) value;
                        List<Object> serializedList = new ArrayList<>();
                        for (Object item : originalList) {
                            if (item instanceof String || isPrimitiveOrWrapper(item.getClass())) {
                                serializedList.add(item);
                            } else {
                                System.out.println(item);
                                serializedList.add(serializeObject(item));
                            }
                        }
                        serialized.put(field.getName(), serializedList);
                    } else if (value instanceof Map) {
                        // 如果序列化的值为Map类型
                        Map<?, ?> originalMap = (Map<?, ?>) value;
                        Map<Object, Object> newMap = new LinkedHashMap<>(originalMap);
                        serialized.put(field.getName(), newMap);
                    } else if (isPrimitiveOrWrapper(value.getClass()) || value instanceof String) {
                        // 如果值为常规对象的处理
                        serialized.put(field.getName(), value);
                    }
                }
            } catch (IllegalAccessException e) {
                MessageUtils.consoleMessage("&c保存配置文件时出现错误: " + e.getMessage());
            }
        }
        return serialized;
    }

    private static boolean isPrimitiveOrWrapper(Class<?> type) {
        return type.isPrimitive() ||
                type == Integer.class ||
                type == Long.class ||
                type == Double.class ||
                type == Float.class ||
                type == Boolean.class ||
                type == Byte.class ||
                type == Character.class ||
                type == Short.class;
    }

    public static <T> T loadObjectFromConfig(FileConfiguration config, String path, Class<T> clazz) {
        return null;
    }
}
