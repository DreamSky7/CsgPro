package vip.mango2.mangocore;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import vip.mango2.mangocore.Annotation.impl.Command.CommandRegister;
import vip.mango2.mangocore.Manager.ConfigurationManager;
import vip.mango2.mangocore.Test.CommandTest;

public final class MangoCore extends JavaPlugin {

    @Getter
    private static MangoCore instance;

    public static ConfigurationManager configManager;

    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic
        registerCommand(new CommandTest());

        configManager.loadConfig("config");
    }

    /**
     * 注册指令
     * @param obj 指令类
     */
    public static void registerCommand(Object obj) {
        configManager = new ConfigurationManager(instance);
        CommandRegister registrar = new CommandRegister(instance);
        registrar.registerCommand(obj);

    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
