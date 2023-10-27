package vip.mango2.mangocore.Manager;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import vip.mango2.mangocore.Entity.File.MangoConfiguration;
import vip.mango2.mangocore.Entity.File.Configuration.MangoYamlFile;
import vip.mango2.mangocore.Enum.FileType;
import vip.mango2.mangocore.Utils.MessageUtils;
import vip.mango2.mangocore.Utils.ValidUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class MangoFileManager {

    private final JavaPlugin plugin;

    @Getter
    private final Map<String, MangoConfiguration> files = new ConcurrentHashMap<>();

    public MangoFileManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * 加载配置文件
     * @param fileName 配置文件名
     * @param type 配置文件类型
     * @return 配置文件
     */
    public MangoConfiguration loadFile(String fileName, FileType type) {
        return loadFile(fileName, type, null);
    }

    /**
     * 加载配置文件
     * @param fileName 配置文件名
     * @param fileType 配置文件类型
     * @param subDirectory 子目录
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public MangoConfiguration loadFile(String fileName, FileType fileType, String subDirectory) {

        File dataFolder = Optional.ofNullable(subDirectory)
                            .map(sd -> new File(plugin.getDataFolder(), sd))
                            .orElse(plugin.getDataFolder());
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        File file = new File(dataFolder, fileName);
        if (!file.exists()) {
            // 尝试从插件的资源中复制文件
            plugin.saveResource(fileName, false);
        }
        return files.computeIfAbsent(fileName, path -> {
            MangoConfiguration mangoFile = fileType.createFile(file.getPath());
            try {
                mangoFile.load();
            } catch (IOException e) {
                // 使用日志记录错误或者重新抛出异常
                throw new RuntimeException("文件加载失败: " + path, e);
            }
            return mangoFile;
        });
    }

    /**
     * 从URL中加载配置文件
     * @param name 配置文件名
     * @return 配置文件
     */
    public void loadConfigFromURL(String name, String urlString) {
        /* 先获取指定链接的文件 */
        try {

            if (!ValidUtils.isValidURL(urlString)) {
                MessageUtils.consoleMessage("&c请输入正确的URL地址");
                return;
            }
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);

            StringBuilder content = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = in.readLine()) != null) {
                    content.append(line).append("\n");
                }
            } catch (IOException e) {
                MessageUtils.consoleMessage("&7从URL中读取配置文件时出现错误: &c" + e.getMessage());
            }

            // 使用 MangoFileManager 来管理文件
            MangoConfiguration mangoFile = loadFile(name + ".yml", FileType.YAML);
            if (mangoFile instanceof MangoYamlFile) {
                MangoYamlFile yamlFile = (MangoYamlFile) mangoFile;
                yamlFile.loadFromString(content.toString()); // 加载内容
                yamlFile.save(); // 保存到本地
            }

            MessageUtils.consoleMessage("&7成功从URL中加载配置文件: &a" + name);

        } catch (Exception e) {

            MessageUtils.consoleMessage("&7从URL中加载配置文件时出现错误: &c" + e.getMessage());
            MessageUtils.consoleMessage("&7从本地加载配置文件: &a" + name);
            // 网络异常则从本体加载
            loadFile(name + ".yml", FileType.YAML);

            throw new RuntimeException(e);
        }
    }

    /**
     * 获取配置文件
     * @param fileName 配置文件名
     */
    public MangoConfiguration getFile(String fileName) {
        return files.get(fileName);
    }

    /**
     * 保存配置文件
     * @param fileName
     */
    public void saveFile(String fileName) {
        MangoConfiguration file = files.get(fileName);
        if (file != null) {
            try {
                file.save();
            } catch (IOException e) {
                MessageUtils.consoleMessage("&c保存配置文件时出现错误: " + fileName);
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 删除配置文件
     * @param fileName 配置文件名
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void deleteFile(String fileName) {
        MangoConfiguration file = files.get(fileName);
        if (file != null) {
            try {
                File diskFile = new File(file.getFilePath());
                if (diskFile.exists()) {
                    diskFile.delete();
                }
            } catch (Exception e) {
                MessageUtils.consoleMessage("&c删除配置文件时出现错误: " + fileName);
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 重新加载配置文件
     * @param fileName 配置文件名
     */
    public void reloadFile(String fileName) {
        MangoConfiguration file = files.get(fileName);
        if (file != null) {
            try {
                file.load();
            } catch (IOException e) {
                MessageUtils.consoleMessage("&c重载配置文件时出现错误: " + fileName);
                throw new RuntimeException(e);
            }
        }
    }
}
