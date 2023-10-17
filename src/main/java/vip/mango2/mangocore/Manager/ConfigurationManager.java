package vip.mango2.mangocore.Manager;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import vip.mango2.mangocore.Utils.MessageUtils;
import vip.mango2.mangocore.Utils.ValidUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ConfigurationManager {

    private final JavaPlugin plugin;

    public final Map<String, YamlConfiguration> configs = new HashMap<>();

    public final Map<String, File> configFiles = new HashMap<>();


    public ConfigurationManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * 加载配置文件
     * @param name 配置文件名
     */
    public void loadConfig(String name) {
        String fileName = name + ".yml";
        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }
        configs.put(name, YamlConfiguration.loadConfiguration(file));
        configFiles.put(name, file);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void loadConfig(String name, String path) {
        String fileName = name + ".yml";
        File file = new File(plugin.getDataFolder() + path, fileName);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdir();
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        configs.put(name, YamlConfiguration.loadConfiguration(file));
        configFiles.put(name, file);
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

            YamlConfiguration configuration = new YamlConfiguration();
            configuration.loadFromString(content.toString());
            configs.put(name, configuration);
            // 保存一份到本地
            File file = new File(plugin.getDataFolder(), name + ".yml");
            configuration.save(file);

            MessageUtils.consoleMessage("&7成功从URL中加载配置文件: &a" + name);

        } catch (Exception e) {

            MessageUtils.consoleMessage("&7从URL中加载配置文件时出现错误: &c" + e.getMessage());
            MessageUtils.consoleMessage("&7从本地加载配置文件: &a" + name);
            // 网络异常则从本体加载
            loadConfig(name);
            throw new RuntimeException(e);
        }
    }

    public YamlConfiguration getConfig(String name) {
        if (Optional.ofNullable(configs.get(name)).isPresent()) {
            return configs.get(name);
        } else {
            return null;
        }
    }


    /**
     * 保存配置文件
     * @param name 配置文件名
     */
    public void saveConfig(String name) {
        if (configFiles.containsKey(name) && configs.containsKey(name)) {
            try {
                configs.get(name).save(configFiles.get(name));
            } catch (IOException e) {
                MessageUtils.senderMessage(plugin.getServer().getConsoleSender(), "&c保存配置文件时出现错误: " + e.getMessage());
            }
        }
    }

    /**
     * 重载配置文件
     * @param name 配置文件名
     */
    public void reloadConfig(String name) {
        loadConfig(name);
    }

    /**
     * 重载所有配置文件
     */
    public void reloadAllConfig() {
        for (String name : configs.keySet()) {
            reloadConfig(name);
        }
    }
}
