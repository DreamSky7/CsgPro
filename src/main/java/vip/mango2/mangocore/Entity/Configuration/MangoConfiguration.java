package vip.mango2.mangocore.Entity.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class MangoConfiguration{

    /**
     * 设置配置项的值
     * @param path 配置项路径
     * @param value 配置项的值
     */
    public abstract void set(String path, Object value);

    public abstract Object get(String path);


    public abstract <T> T get(String path, Class<T> def);

    public int getInt(String path){
        return get(path, int.class);
    }

    public String getString(String path){
        return get(path, String.class);
    }

    public double getDouble(String path){
        return get(path, double.class);
    }

    public boolean getBoolean(String path){
        return get(path, boolean.class);
    }

    public long getLong(String path){
        return get(path, long.class);
    }

    public float getFloat(String path){
        return get(path, float.class);
    }

    public char getChar(String path){
        return get(path, char.class);
    }

    public abstract <T> List<T> getList(String path, Class<T> def);


    public List<String> getStringList(String path){
        return getList(path, String.class);
    }

    public List<Integer> getIntList(String path){
        return getList(path, Integer.class);
    }

    public List<Double> getDoubleList(String path){
        return getList(path, Double.class);
    }


    public abstract <T> Map<String, T> getMap(String path, Class<T> def);

    public abstract <T> Map<String, List<T>> getMapList(String path, Class<T> def);

    public boolean contains(String path) {
        return get(path) != null;
    }

    /**
     * 加载配置文件
     * @param file
     * @throws IOException IO异常
     */
    public abstract void Load(File file) throws IOException;

    /**
     * 保存配置文件
     * @param file
     * @throws IOException
     */
    public abstract void Save(File file) throws IOException;

}
