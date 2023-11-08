package vip.mango2.mangocore.Entity.File;

import lombok.Getter;
import vip.mango2.mangocore.Entity.Configuration.MangoConfiguration;
import vip.mango2.mangocore.Manager.MangoWorkspace;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

@Getter
public class MangoDirectory extends MangoNode{


    public MangoDirectory(MangoWorkspace workSpace, String local_path) {
        super(workSpace, local_path);
    }

    protected MangoDirectory(MangoWorkspace workSpace, File file){
        super(workSpace, file);
    }

    @Override
    public void createNew() {
        if(file.isFile()){
            return;
        }
        if(!file.exists()){
            file.mkdir();
        }
    }

    public Set<MangoFile> listFiles() {
        Set<MangoFile> mfl = new HashSet<>();
        if(exists()){
            for(File f : file.listFiles()){
                if(f.isFile()){
                    mfl.add(new MangoFile(workSpace, f));
                }
            }
        }
        return mfl;
    }

    public Set<MangoDirectory> listDirectories() {
        Set<MangoDirectory> mfl = new HashSet<>();
        if(exists()){
            for(File f : file.listFiles()){
                if(f.isDirectory()){
                    mfl.add(new MangoDirectory(workSpace, f));
                }
            }
        }
        return mfl;
    }

    /**
     * 删库跑路(bushi)
     */
    public void delete(){
        if(exists()){
            for(MangoDirectory subdir : listDirectories()){
                subdir.delete();
            }
            for(MangoFile subdir : listFiles()){
                subdir.delete();
            }
            file.delete();
        }
    }

    @Override
    public boolean exists() {
        return file.exists() && file.isDirectory();
    }

}
