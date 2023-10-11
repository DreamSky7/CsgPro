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
    public static void saveObjectToConfig(FileConfiguration config, String parentPath, Object obj) {

        if (obj instanceof Map) {
            // 如果对象为Map类型
            Map<?, ?> map = (Map<?, ?>) obj;
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                String key = entry.getKey().toString();
                Object value = entry.getValue();
                String fullPath = parentPath.isEmpty() ? key : parentPath + "." + key;
                saveObjectToConfig(config, fullPath, value);
            }
        } else if (obj instanceof List) {
            // 如果对象为List类型
            List<?> originalList = (List<?>) obj;
            List<Object> serializedList = new ArrayList<>();
            for (Object item : originalList) {
                if (item instanceof String || isPrimitiveOrWrapper(item.getClass())) {
                    serializedList.add(item);
                } else if (item instanceof Map) {
                    // 如果元素为Map类型
                    serializedList.add(handleMap((Map<?, ?>) item));
                } else {
                    serializedList.add(serializeObject(item));
                }
            }
            config.set(parentPath, serializedList);
        } else if (isPrimitiveOrWrapper(obj.getClass()) || obj instanceof String) {
            // 如果对象为基本类型或者String类型
            config.set(parentPath, obj);
        } else {
            // 如果为自定义对象
            Map<String, Object> serializedObj = serializeObject(obj);
            config.set(parentPath, serializedObj);
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
                                serializedList.add(serializeObject(item));
                            }
                        }
                        serialized.put(field.getName(), serializedList);
                    } else if (value instanceof Map) {
                        // 如果序列化的值为Map类型
                        serialized.put(field.getName(), handleMap((Map<?, ?>) value));
                    } else if (isPrimitiveOrWrapper(value.getClass()) || value instanceof String) {
                        // 如果值为常规对象的处理
                        serialized.put(field.getName(), value);
                    } else {
                        serialized.put(field.getName(), serializeObject(value));
                    }
                }
            } catch (IllegalAccessException e) {
                MessageUtils.consoleMessage("&c保存配置文件时出现错误: " + e.getMessage());
            }
        }
        return serialized;
    }

    private static Map<String, Object> handleMap(Map<?, ?> value) {
        Map<String, Object> serializedMap = new LinkedHashMap<>();
        for (Map.Entry<?, ?> entry : value.entrySet()) {
            String key = entry.getKey().toString();
            Object itemValue = entry.getValue();
            if (itemValue instanceof String || isPrimitiveOrWrapper(itemValue.getClass())) {
                serializedMap.put(key, itemValue);
            } else {
                serializedMap.put(key, serializeObject(itemValue));
            }
        }
        return serializedMap;
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
