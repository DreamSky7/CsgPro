package vip.mango2.mangocore.Manager;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import vip.mango2.mangocore.Entity.Configuration.MangoConfiguration;
import vip.mango2.mangocore.Entity.File.MangoFile;
import vip.mango2.mangocore.Utils.MessageUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class MangoConfigManager {

    @Getter
    public final JavaPlugin plugin;

    @Getter
    public final String workPath;

    private final Map<MangoFile, MangoConfiguration> file_cache = new HashMap();

    public MangoConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.workPath = plugin.getDataFolder().getAbsolutePath();
    }


    /**
     * 加载本地配置文件。
     * @param filePath 配置文件相对路径
     * @param type 配置文件类型
     * @return 配置文件
     */
    public <T extends MangoConfiguration> T loadConfig(String filePath, Class<T> type) {
        try {
            File f = new File(filePath);
            return loadConfig(f.toURI().toURL(), type);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 加载URL。
     * @param url 资源
     * @param type 配置文件类型
     * @return 配置文件
     */
    public <T extends MangoConfiguration> T loadConfig(URL url, Class<T> type) {
        MangoFile mangoFile = new MangoFile(this, url);

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
        T conf = mangoFile.load(type);
        file_cache.put(mangoFile, conf);
        return conf;

    }

    /**
     * 删除配置文件
     */
    public void deleteConfig(URL url) {
        MangoFile mangoFile = new MangoFile(this, url);
        mangoFile.delete();
        file_cache.remove(mangoFile);
    }

    /**
     * 保存配置文件
     */
    public void saveConfig(URL url) {
        MangoFile file = new MangoFile(this,url);
        if (file_cache.containsKey(file)) {
            try {
                file.save(file_cache.get(file));
            } catch (IOException e) {
                MessageUtils.consoleMessage("&c保存配置文件时出现错误");
                throw new RuntimeException(e);
            }
        }
    }

}
