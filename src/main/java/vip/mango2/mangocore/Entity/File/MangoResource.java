package vip.mango2.mangocore.Entity.File;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vip.mango2.mangocore.Entity.Configuration.MangoConfiguration;
import vip.mango2.mangocore.Manager.MangoWorkspace;
import vip.mango2.mangocore.Utils.MessageUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

enum ResourceType{
    LOCAL, HTTP, PLUGIN, INVALID
}
@Getter
public class MangoResource{

    private final MangoWorkspace workSpace;

    //URL可以是本地文件或远程文件。
    private final URL file_url;

    //资源类型，应由URL解析而来。
    private final ResourceType resourceType;

    //如果为null，意味着没有load，否则意味着对应load类型。
    private final MangoConfiguration configuration = null;

    public MangoResource(@NotNull MangoWorkspace workSpace, @Nullable URL url){
        this.workSpace = workSpace;
        this.file_url = url;
        this.resourceType = parseResourceType(file_url);
    }

    public MangoResource(MangoWorkspace workSpace, String localPath){
        try {
            this.workSpace = workSpace;
            this.file_url = new File(workSpace.workPath,localPath).toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        this.resourceType = parseResourceType(file_url);
    }

    private ResourceType parseResourceType(URL url){
        System.out.println(url.toString());
        if(url == null){
            return ResourceType.INVALID;
        }
        if(url.getProtocol().equals("file")){
            if(url.getPath().contains("jar!/")){
                return ResourceType.PLUGIN;
            }else{
                return ResourceType.LOCAL;
            }

        }
        if(url.getProtocol().equals("http") || url.getProtocol().equals("https")){
            return ResourceType.HTTP;
        }
        return ResourceType.INVALID;
    }

    /**
     * 读取特定格式配置
     * 提醒：返回值不会为null，但可能为初始对象。
     * @param def
     * @return
     * @param <T>
     */
    public <T extends MangoConfiguration> T load(Class<T> def){
        T instance;

        try {
            instance = def.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            InputStream file_stream = null;
            switch(resourceType){
                case HTTP:
                    file_stream = readHTTP();
                    break;
                case LOCAL:
                    file_stream = readLocal();
                    break;
                case PLUGIN:
                    file_stream = workSpace.plugin.getResource(file_url.getPath().split("jar!/")[1]);
                    //workSpace.plugin.saveResource(file_url.getPath().split("jar!/")[1], false);
            }
            if(file_stream != null){
                instance.Load(file_stream);
            }
        } catch (Exception ignored) {
        }

        return instance;
    }

    /**
     * 根据相对路径加载一个数据流。
     */
    private InputStream readLocal() throws IOException {
        System.out.println("获取的文件:" + file_url.getFile());
        return file_url.openStream();
    }

    /**
     * 根据HTTP资源URL加载一个数据流。
     */
    private InputStream readHTTP() throws IOException {
        HttpURLConnection connection;

        try {
            connection = (HttpURLConnection) file_url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
        } catch (IOException e) {
            MessageUtils.consoleMessage("加载配置文件失败: "+file_url);
            throw new RuntimeException(e);
        }

        return connection.getInputStream();

    }


    /**
     * 重写equals便于Set.contains比较
     * @param o 比较对象
     * @return 是否相等
     */
    public boolean equals(Object o){
        if(o instanceof MangoResource){
            MangoResource oth = (MangoResource) o;
            return oth.workSpace == this.workSpace &&
                    oth.file_url.sameFile(this.file_url);
        }
        return false;
    }
    /**
     * equals前会先检查hashCode，因此重写。
     * @return
     */
    @Override
    public int hashCode() {
        return 0;
    }
}
