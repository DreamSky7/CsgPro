package vip.mango2.mangocore.Entity.File;

import lombok.Getter;

import java.io.File;
import java.io.IOException;

public abstract  class MangoFile {

    protected File file;

    @Getter
    protected boolean loaded = false;

    public MangoFile(String filePath) {
        this.file = new File(filePath);
    }

    public String getFilePath() {
        return file.getPath();
    }

    /**
     * 加载配置文件
     * @throws IOException IO异常
     */
    public abstract void load() throws IOException;

    /**
     * 保存配置文件
     * @throws IOException IO异常
     */
    public abstract void save() throws IOException;

    /**
     * 获取配置项的值
     * @param path 配置项路径
     * @return 配置项的值
     */
    public abstract Object get(String path);

    /**
     * 设置配置项的值
     * @param path 配置项路径
     * @param value 配置项的值
     */
    public abstract void set(String path, Object value);

}
