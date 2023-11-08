package vip.mango2.mangocore.Entity.File;

import lombok.Getter;
import vip.mango2.mangocore.Entity.Configuration.MangoConfiguration;
import vip.mango2.mangocore.Manager.MangoWorkspace;

import java.io.*;
import java.nio.file.Files;

@Getter
public class MangoFile extends MangoNode{


    public MangoFile(MangoWorkspace workSpace, String local_path) {
        super(workSpace, local_path);
    }

    protected MangoFile(MangoWorkspace workSpace, File file){
        super(workSpace, file);
    }
    @Override
    public void createNew() {
        if(file.isDirectory()){
            return;
        }
        if(file.exists()){
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
                ir.close();
                return conf;
            }
        } catch (Exception ignored) {
        }
        return null;
    }
    public <T extends MangoConfiguration> void save(T conf) throws IOException {
        OutputStream wr = Files.newOutputStream(file.toPath());
        conf.Save(wr);
        wr.close();
    }

    /**
     * 删库跑路(bushi)
     */
    public void delete(){
        if(exists()){
            file.delete();
        }
    }

    @Override
    public boolean exists() {
        return file.exists() && file.isFile();
    }

}
