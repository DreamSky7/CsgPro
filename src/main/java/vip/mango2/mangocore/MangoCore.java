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
    }

    /**
     * 注册指令
     * @param obj 指令类
     */
    public void registerCommand(Object obj) {
        configManager = new ConfigurationManager(this);
        CommandRegister registrar = new CommandRegister(this);
        registrar.registerCommand(obj);

    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
