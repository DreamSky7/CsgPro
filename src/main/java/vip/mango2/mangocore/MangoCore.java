package vip.mango2.mangocore;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import vip.mango2.mangocore.Annotation.impl.Command.CommandRegister;
import vip.mango2.mangocore.Test.CommandTest;

public final class MangoCore extends JavaPlugin {

    @Getter
    private static MangoCore instance;

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
        CommandRegister registrar = new CommandRegister(this);
        registrar.registerCommand(obj);
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
