package vip.mango2.mangocore.Entity.File;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class MangoConfiguration extends MangoFile{
    public MangoConfiguration(String filePath) {
        super(filePath);
    }

    /**
     * 设置配置项的值
     * @param path 配置项路径
     * @param value 配置项的值
     */
    public abstract void set(String path, Object value);

    public abstract Object get(String path);


    public abstract <T> T get(String path, Class<T> def);

    public abstract int getInt(String path);

    public abstract String getString(String path);

    public abstract double getDouble(String path);

    public abstract boolean getBoolean(String path);

    public abstract long getLong(String path);

    public abstract float getFloat(String path);

    public abstract char getChar(String path);

    public abstract List<String> getStringList(String path);

    public abstract List<Integer> getIntList(String path);

    public abstract List<Double> getDoubleList(String path);

    public abstract <T> List<T> getList(String path, Class<T> def);

    public abstract <T> Map<String, T> getStringMap(String path, Class<T> def);

    public abstract <T> Map<String, List<T>> getStringMapList(String path, Class<T> def);

    public boolean contains(String path) {
        return get(path) != null;
    }

}
