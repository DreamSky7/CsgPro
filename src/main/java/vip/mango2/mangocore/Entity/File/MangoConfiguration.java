package vip.mango2.mangocore.Entity.File;

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

    public <T> T get(String path) {
        return get(path, null);
    }


    public abstract <T> T get(String path, T def);

    public int getInt(String path) {
        Integer value = get(path);
        return value != null ? value : 0;
    }

    public String getString(String path) {
        String value = get(path);
        return value != null ? value : "";
    }

    public double getDouble(String path) {
        Double value = get(path);
        return value != null ? value : 0.0;
    }

    public boolean getBoolean(String path) {
        Boolean value = get(path);
        return value != null ? value : false;
    }

    public long getLong(String path) {
        Long value = get(path);
        return value != null ? value : 0L;
    }

    public float getFloat(String path) {
        Float value = get(path);
        return value != null ? value : 0F;
    }

    public short getShort(String path) {
        Short value = get(path);
        return value != null ? value : 0;
    }

    public byte getByte(String path) {
        Byte value = get(path);
        return value != null ? value : 0;
    }

    public char getChar(String path) {
        Character value = get(path);
        return value != null ? value : 0;
    }

    public boolean contains(String path) {
        return get(path) != null;
    }

}
