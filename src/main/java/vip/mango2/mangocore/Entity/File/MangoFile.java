package vip.mango2.mangocore.Entity.File;

import lombok.Getter;
import vip.mango2.mangocore.Entity.Configuration.MangoConfiguration;
import vip.mango2.mangocore.Manager.MangoConfigManager;
import vip.mango2.mangocore.Utils.MessageUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

enum ResourceType{
    LOCAL, HTTP, INVALID
}
public class MangoFile{

    //工作空间，即MangoFileManager。
    @Getter
    private final MangoConfigManager workSpace; //maybe only need a root path?

    //URL可以是本地文件或远程文件。
    @Getter
    private final URL file_url;

    //资源类型，应由URL解析而来。
    @Getter
    private final ResourceType resourceType;

    //如果为null，意味着没有load，否则意味着对应load类型。
    @Getter
    private MangoConfiguration configuration = null;

    public MangoFile(MangoConfigManager workSpace, URL url){
        this.workSpace = workSpace;
        this.file_url = url;
        this.resourceType = ResourceType.INVALID; //TODO: 通过url解析resourceType.
    }


    private void autoCreateFile(File file, boolean isDirectory){
        //if exist, then ignore.
        if(file.exists()){
            return;
        }
        //recursively create parent.
        if(!file.getParentFile().exists()){
            autoCreateFile(file.getParentFile(), true);
        }
        if(!file.exists() && isDirectory){
            file.mkdir();
            return;
        }
        //saveSource.
        if(!file.exists()){
            workSpace.plugin.saveResource(file.getName(), false);
        }
        //saveSource failed: create new.
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private File constructTempFile() throws IOException {
        String tmp_name = ".temp/."+file_url.toString().hashCode();
        File tmp = new File(workSpace.workPath, tmp_name);
        if(tmp.exists()){
            tmp.delete();
        }
        autoCreateFile(tmp,false);
        tmp.deleteOnExit();
        return tmp;
    }

    /**
     * 读取特定格式配置
     * @param def
     * @return
     * @param <T>
     */
    public <T extends MangoConfiguration> T load(Class<T> def){

        File file = null;
        switch(resourceType){
            case HTTP:
                file = readHTTP();
                break;
            case LOCAL:
                file = readLocal();
                break;
        }

        if(file == null){
            return null;
        }
        try {
            T instance = def.getConstructor().newInstance();
            instance.Load(file);
            return instance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据相对路径加载一个文件。
     */
    private File readLocal(){
        return new File(file_url.getFile());
    }

    /**
     * 根据HTTP资源URL加载一个文件。
     */
    private File readHTTP(){

        File file_construct;
        HttpURLConnection connection;

        try {
            file_construct = constructTempFile();

            connection = (HttpURLConnection) file_url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (
                FileWriter writer = new FileWriter(file_construct);
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))
        ) {
            String line;
            while ((line = in.readLine()) != null) {
                writer.append(line).append("\n");
            }
        } catch (IOException e) {
            MessageUtils.consoleMessage("&7从URL中读取配置文件时出现错误: &c" + e.getMessage());
        }

        return file_construct;
    }


    /**
     * 保存特定格式配置
     * @param conf
     * @param <T>
     * @throws IOException
     */
    public <T extends MangoConfiguration> void save(T conf) throws IOException {
        File file = null;

        switch(resourceType){
            case LOCAL:
                file = new File(file_url.getFile());
                break;
            case HTTP:
                file = constructTempFile();
                break;
            case INVALID:
                MessageUtils.consoleMessage("&7Try saving invalid resource: &c" + file_url);
        }
        if(file == null) return;

        conf.Save(file);

        if(resourceType == ResourceType.HTTP){
            //要不要把数据上传到云端, 你看着办
            MessageUtils.consoleMessage("&7Uploading to cloud not supported yet! &c" + file_url);
        }
    }

    /**
     * 删库跑路(bushi)
     */
    public void delete(){
        if(resourceType == ResourceType.LOCAL){
            File file = new File(file_url.getFile());
            if(file.exists()){
                file.delete();
            }
        }
    }
    /**
     * 重写equals便于Set.contains比较
     * @param o
     * @return
     */
    public boolean equals(Object o){
        if(o instanceof MangoFile){
            MangoFile oth = (MangoFile) o;
            return oth.workSpace == this.workSpace &&
                    oth.file_url.sameFile(this.file_url);
        }
        return false;
    }

}
