package vip.mango2.mangocore.Utils;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.*;

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

    private static Map<String, Object> serializeObject(Object obj) {
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

    /**
     * 从配置文件中加载对象
     * @param yamlConfiguration 配置文件
     * @param path 路径
     * @param clazz 类型
     * @return 对象
     * @param <T> 类型
     */
    public static <T> T loadObjectFromConfig(YamlConfiguration yamlConfiguration, String path, Class<T> clazz) {
        ConfigurationSection section = yamlConfiguration.getConfigurationSection(path);
        if (section == null) {
            return null;
        }
        Map<String, Object> values = section.getValues(false);
        return loadObjectFromMap(values, clazz);
    }

    /**
     * 从Map中加载对象
     * @param yamlConfiguration 配置文件
     * @param path 路径
     * @param clazz 类型
     * @return 对象
     * @param <T> 类型
     */
    public static <T> Map<String, T> loadMapFromConfig(YamlConfiguration yamlConfiguration, String path, Class<T> clazz) {
        if (!yamlConfiguration.isConfigurationSection(path)) {
            return null;
        }
        ConfigurationSection section = yamlConfiguration.getConfigurationSection(path);
        Map<String, T> resultMap = new HashMap<>();
        for (String key : section.getKeys(false)) {
            Object obj = section.get(key);
            if (obj instanceof MemorySection) {
                MemorySection memorySection = (MemorySection) obj;
                Map<?, ?> mapData = memorySection.getValues(false);
                T item = loadObjectFromMap(mapData, clazz);
                if (item != null) {
                    resultMap.put(key, item);
                }
            }
        }
        return resultMap;
    }

    /**
     * 从配置文件中加载列表对象
     * @param yamlConfiguration 配置文件
     * @param path 路径
     * @param clazz 类型
     * @return 对象
     * @param <T> 类型
     */
    public static <T> List<?> loadListFromConfig(YamlConfiguration yamlConfiguration, String path, Class<T> clazz, boolean isMapStructure) {
        List<?> objList = yamlConfiguration.getList(path);
        if (objList == null) {
            return null;
        }

        List<T> resultList = new ArrayList<>();
        for (Object obj : objList) {
            if (obj instanceof Map) {
                if (isMapStructure) {
                    // 处理 List<Map<String, TClass>> 结构
                    Map<String, T> mapResult = new HashMap<>();
                    Map<?, ?> mapObj = (Map<?, ?>) obj;
                    for (Map.Entry<?, ?> entry : mapObj.entrySet()) {
                        mapResult.put((String) entry.getKey(), loadObjectFromMap((Map<?, ?>) entry.getValue(), clazz));
                    }
                    resultList.add((T) mapResult);
                } else {
                    // 处理 List<TClass> 结构
                    T item = loadObjectFromMap((Map<?, ?>) obj, clazz);
                    if (item != null) {
                        resultList.add(item);
                    }
                }
            }
        }
        return resultList;
    }

    private static <T> T loadObjectFromMap(Map<?, ?> map, Class<T> clazz) {
        try {
            T obj = clazz.newInstance();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                if (map.containsKey(fieldName)) {
                    Object value = map.get(fieldName);
                    if (value != null) {
                        if (value instanceof Map) {
                            // 如果值是一个Map，则递归调用loadObjectFromMap
                            field.set(obj, loadObjectFromMap((Map<?, ?>) value, field.getType()));
                        } else if (value instanceof List) {
                            // 如果值是一个List
                            List<?> listValue = (List<?>) value;
                            List<Object> newList = new ArrayList<>();
                            for (Object listItem : listValue) {
                                if (listItem instanceof Map) {
                                    // 获取List的泛型类型
                                    Class<?> genericType = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                                    newList.add(loadObjectFromMap((Map<?, ?>) listItem, genericType));
                                } else {
                                    newList.add(listItem);
                                }
                            }
                            field.set(obj, newList);
                        } else {
                            field.set(obj, value);
                        }
                    }
                }
            }
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtils.consoleMessage("&c从Map中加载对象时出现错误: " + e.getMessage());
            return null;
        }
    }
}
