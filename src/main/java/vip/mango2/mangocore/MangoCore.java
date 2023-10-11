package vip.mango2.mangocore;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;
import vip.mango2.mangocore.Annotation.impl.Command.CommandRegister;
import vip.mango2.mangocore.Entity.TClass;
import vip.mango2.mangocore.Entity.TStudent;
import vip.mango2.mangocore.Manager.ConfigurationManager;
import vip.mango2.mangocore.Test.CommandTest;
import vip.mango2.mangocore.Utils.YamlUtils;

import java.util.*;

public final class MangoCore extends JavaPlugin {

    @Getter
    private static MangoCore instance;

    public static ConfigurationManager configManager;

    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic
        registerCommand(new CommandTest());

        configManager = new ConfigurationManager(this);
        configManager.loadConfig("config");
        configManager.loadConfig("message");

        YamlConfiguration config = getConfig("config");
        if (config != null) {
            YamlUtils.saveObjectToConfig(config, "test", "test");
            saveConfig("config");
        }
    }

    /**
     * 获取配置文件对象
     * @param config 配置文件名
     * @return 配置文件对象
     */
    public YamlConfiguration getConfig(String config) {
        if (configManager.getConfig(config).isPresent()) {
            return configManager.getConfig(config).get();
        } else {
            return null;
        }
    }

    /**
     * 保存配置文件
     * @param config
     */
    public void saveConfig(String config) {
        configManager.saveConfig(config);
    }

    /**
     * 注册指令
     * @param obj 指令类
     */
    public void registerCommand(Object obj) {
        CommandRegister registrar = new CommandRegister(this);
        registrar.registerCommand(obj);
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
