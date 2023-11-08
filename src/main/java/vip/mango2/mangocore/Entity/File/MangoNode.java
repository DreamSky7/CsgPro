package vip.mango2.mangocore.Entity.File;

import vip.mango2.mangocore.Manager.MangoWorkspace;

import java.io.File;

public abstract class MangoNode {

    //工作空间，即MangoFileManager。
    protected final MangoWorkspace workSpace; //maybe only need a root path?

    //本地文件(文件夹)。
    protected final File file;

    public MangoNode(MangoWorkspace workSpace, String local_path) {
        this.workSpace = workSpace;
        this.file = new File(workSpace.workPath, local_path);
    }

    protected MangoNode(MangoWorkspace workSpace, File file){
        this.workSpace = workSpace;
        this.file = file;
    }

    public abstract void createNew();

    public abstract void delete();

    public abstract boolean exists();

    /**
     * 重写equals便于Set.contains比较
     * @param o 比较对象
     * @return 是否相等
     */
    public boolean equals(Object o){
        if(o.getClass() == this.getClass()){
            MangoNode oth = (MangoNode) o;
            return oth.workSpace == this.workSpace &&
                    oth.file.getAbsolutePath().equals(this.file.getAbsolutePath());
        }
        return false;
    }

    /**
     * equals前会先检查hashCode，因此重写。
     * @return
     */
    @Override
    public int hashCode() {
        return file.getAbsolutePath().hashCode();
    }
}
