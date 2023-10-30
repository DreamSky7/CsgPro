package vip.mango2.mangocore.Entity.File;

import java.math.BigDecimal;

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


    public abstract <T> T get(String path, Class<T> def);

    public int getInt(String path) {
        Integer value = get(path);
        return value != null ? value : 0;
    }

    public String getString(String path) {
        String value = get(path);
        return value != null ? value : "";
    }

    public double getDouble(String path) {
        Object value = get(path);
        if (value instanceof Double) {
            return ((Double) value).floatValue();
        } else if (value instanceof Float) {
            return (Float) value;
        } else if (value instanceof BigDecimal) {
            return ((BigDecimal) value).floatValue();
        } else {
            return 0F;
        }
    }

    public boolean getBoolean(String path) {
        Boolean value = get(path);
        return value != null ? value : false;
    }

    public long getLong(String path) {
        Object value = get(path);
        if (value instanceof Integer) {
            return ((Integer) value).longValue();
        } else if (value instanceof Long) {
            return (Long) value;
        } else {
            return 0L;
        }
    }

    public float getFloat(String path) {
        Object value = get(path);
        if (value instanceof Double) {
            return ((Double) value).floatValue();
        } else if (value instanceof Float) {
            return (Float) value;
        } else if (value instanceof BigDecimal) {
            return ((BigDecimal) value).floatValue();
        } else {
            return 0F;
        }
    }

    public char getChar(String path) {
        Object value = get(path);
        if (value instanceof String) {
            String stringValue = (String) value;
            if (stringValue.length() == 1) {
                return stringValue.charAt(0);
            }
        }
        return '\u0000';
    }

    public boolean contains(String path) {
        return get(path) != null;
    }

}
