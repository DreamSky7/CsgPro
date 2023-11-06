package vip.mango2.mangocore.Manager;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import vip.mango2.mangocore.Entity.Configuration.MangoConfiguration;
import vip.mango2.mangocore.Entity.Configuration.MangoJsonConfig;
import vip.mango2.mangocore.Entity.Configuration.MangoYamlConfig;
import vip.mango2.mangocore.Entity.File.MangoFile;
import vip.mango2.mangocore.Entity.File.MangoResource;
import vip.mango2.mangocore.Utils.MessageUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class MangoWorkspace {

    @Getter
    public final JavaPlugin plugin;

    @Getter
    public final String workPath;

    private final Map<MangoFile, MangoConfiguration> file_cache = new HashMap<>();

    public MangoWorkspace(JavaPlugin plugin) {
        this.plugin = plugin;
        this.workPath = plugin.getDataFolder().getAbsolutePath();
    }

    /**
     * 加载一个资源，该加载不会在本地留下任何痕迹。
     * @param source 资源URL
     * @param type 配置文件类型
     * @return 配置文件
     */
    public <T extends MangoConfiguration> T loadResource(URL source, Class<T> type) {
        MangoResource msource = new MangoResource(this,source);
        return msource.load(type);
    }

        /**
         * 加载本地文件，如果文件不存在则从插件JAR获取。
         * @param localPath 配置文件相对路径
         * @param type 配置文件类型
         * @return 配置文件
         */
    public <T extends MangoConfiguration> T loadFile(String localPath, Class<T> type) {
        try {
            return loadFile(localPath, type, new URL("jar:///"+localPath));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加载本地可配置文件，如果文件不存在则从指定URL获取。
     * @param localPath 配置文件相对路径
     * @param type 配置文件类型
     * @param source 如果配置文件不存在，从这里获取
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends MangoConfiguration> T loadFile(String localPath, Class<T> type, URL source) {
        MangoFile mangoFile = new MangoFile(this, localPath);

        //如果缓存中已存在且一致，直接返回。
        if(file_cache.containsKey(mangoFile)){
            MangoConfiguration cached = file_cache.get(mangoFile);
            if(type == cached.getClass()){
                return (T)cached;
            }else{
                file_cache.remove(mangoFile);
            }
        }

        //如果缓存中不存在，创建、添加缓存、返回。
        try {
            T conf = mangoFile.load(type);
            if(conf == null) {
                MangoResource mango_source = new MangoResource(this,source);
                conf = mango_source.load(type);
            }

            if(conf!=null){
                file_cache.put(mangoFile, conf);
            }

            return conf;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除配置文件
     */
    public void deleteFile(String path) {
        MangoFile mangoFile = new MangoFile(this, path);
        mangoFile.delete();
        file_cache.remove(mangoFile);
    }

    /**
     * 保存配置文件
     */
    public void saveFile(String path) {
        MangoFile file = new MangoFile(this,path);
        if (file_cache.containsKey(file)) {
            try {
                file.save(file_cache.get(file));
            } catch (IOException e) {
                MessageUtils.consoleMessage("&c保存配置文件时出现错误");
                throw new RuntimeException(e);
            }
        }
    }


    public static void main(String[] args){
        try {

            MangoWorkspace space = new MangoWorkspace(null);
            space.loadFile("config.yml", MangoYamlConfig.class, new URL(
                    "https://portrait.gitee.com/MangoRabbit/mango-core/blob/45aabfc0c642ec3ec8cc7169a9ecd8b4612e6974/src/main/java/vip/mango2/mangocore/Entity/Configuration/MangoJsonConfig.java"
            ));
            space.saveFile("config.yml");

            space.loadResource(new URL("jar:///config.json"), MangoJsonConfig.class);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }
}
