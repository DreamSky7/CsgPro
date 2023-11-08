package vip.mango2.mangocore.Manager;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import vip.mango2.mangocore.Entity.Configuration.MangoConfiguration;
import vip.mango2.mangocore.Entity.File.MangoDirectory;
import vip.mango2.mangocore.Entity.File.MangoFile;
import vip.mango2.mangocore.Entity.File.MangoResource;
import vip.mango2.mangocore.Utils.MessageUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class MangoWorkspace {

    private static final Set<MangoWorkspace> workspaces = new HashSet<>();

    /**
     * 获取插件对应的MangoWorkspace单例
     * @param p
     * @return
     */
    public static MangoWorkspace getWorkspace(JavaPlugin p){
        for(MangoWorkspace space : workspaces){
            if(space.plugin.getName().equals(p.getName())){
                return space;
            }
        }
        MangoWorkspace new_sp = new MangoWorkspace(p);
        workspaces.add(new_sp);
        return new_sp;
    }

    @Getter
    public final JavaPlugin plugin;

    @Getter
    public final String workPath;

    private final Map<MangoFile, MangoConfiguration> file_cache = new HashMap<>();

    private MangoWorkspace(JavaPlugin plugin) {
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
     * 加载一个资源，该加载不会在本地留下任何痕迹。
     * @param type 配置文件类型
     * @return 配置文件
     */
    public <T extends MangoConfiguration> T loadJarResource(String filename, Class<T> type) {
        String url = "file:///jar!/"+filename;
        url = url.replace('\\','/');
        try {
            return loadResource(new URL(url),type);
        } catch (MalformedURLException e) {
            System.out.println("Invalid URL: "+url);
            throw new RuntimeException(e);
        }
    }

        /**
         * 加载本地文件，如果文件不存在则从插件JAR获取。
         * @param localPath 配置文件相对路径
         * @param type 配置文件类型
         * @return 配置文件
         */
    public <T extends MangoConfiguration> T loadFile(String localPath, Class<T> type) {
        String url = "file:///jar!/"+localPath;
        url = url.replace('\\','/');
        return loadFile(localPath, type, url);
    }

    /**
     * 加载本地文件夹。
     * @param localPath 相对路径
     * @return 配置文件
     */
    public MangoDirectory loadDirectory(String localPath) {
        return new MangoDirectory(this, localPath);
    }

    /**
     * 加载本地可配置文件，如果文件不存在则从指定URL获取。
     * @param localPath 配置文件相对路径
     * @param type 配置文件类型
     * @param source 如果配置文件不存在，从这里获取
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T extends MangoConfiguration> T loadFile(String localPath, Class<T> type, String source) {
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
                MangoResource mango_source = new MangoResource(this,new URL(source));
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
        }else{
            System.out.println("Saving a non-exist file.");
        }
    }

}
