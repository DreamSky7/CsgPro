package vip.mango2.mangocore.Entity.File;

import lombok.Getter;

import java.io.File;
import java.io.IOException;

public abstract class MangoFile {

    private final File file;

    public MangoFile(String filePath) {
        this.file = new File(filePath);
    }

    public String getFilePath() {
        return file.getPath();
    }

    public void load() throws IOException {
        onLoad(file);
    }

    /**
     * 加载配置文件
     * @throws IOException IO异常
     */
    public abstract void onLoad(File file) throws IOException;


    public void save() throws IOException {
        onSave(file);
    }

    /**
     * 保存配置文件
     * @throws IOException IO异常
     */
    public abstract void onSave(File file) throws IOException;



}
