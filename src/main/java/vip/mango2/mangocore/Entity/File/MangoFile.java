package vip.mango2.mangocore.Entity.File;

import lombok.Getter;
import vip.mango2.mangocore.Entity.Configuration.MangoConfiguration;
import vip.mango2.mangocore.Manager.MangoWorkspace;

import java.io.*;
import java.nio.file.Files;

@Getter
public class MangoFile{

    //工作空间，即MangoFileManager。
    private final MangoWorkspace workSpace; //maybe only need a root path?

    //URL可以是本地文件或远程文件。
    private final File file;

    public MangoFile(MangoWorkspace workSpace, String local_path) {
        this.workSpace = workSpace;
        this.file = new File(workSpace.workPath, local_path);
    }

    /**
     * 提醒：返回值可能为null。
     * @param def
     * @return
     * @param <T>
     * @throws IOException
     */
    public <T extends MangoConfiguration> T load(Class<T> def) throws IOException {

        try {
            T conf = def.getConstructor().newInstance();
            //文件不存在则会返回null
            if(file.exists()){
                InputStream ir = Files.newInputStream(file.toPath());
                conf.Load(ir);
                return conf;
            }
        } catch (Exception ignored) {
        }
        return null;
    }
    public <T extends MangoConfiguration> void save(T conf) throws IOException {
        OutputStream wr = Files.newOutputStream(file.toPath());
        conf.Save(wr);
    }

    /**
     * 删库跑路(bushi)
     */
    public void delete(){
        if(file.exists()){
            file.delete();
        }
    }
    /**
     * 重写equals便于Set.contains比较
     * @param o 比较对象
     * @return 是否相等
     */
    public boolean equals(Object o){
        if(o instanceof MangoFile){
            MangoFile oth = (MangoFile) o;
            return oth.workSpace == this.workSpace &&
                    oth.file.getAbsolutePath().equals(this.file.getAbsolutePath());
        }
        return false;
    }

}
