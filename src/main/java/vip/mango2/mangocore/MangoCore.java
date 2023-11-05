package vip.mango2.mangocore;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import vip.mango2.mangocore.Annotation.impl.Command.CommandRegister;
import vip.mango2.mangocore.Entity.Configuration.MangoJsonConfig;
import vip.mango2.mangocore.Manager.MangoWorkspace;
import vip.mango2.mangocore.Utils.MessageUtils;

public final class MangoCore extends JavaPlugin {

    @Getter
    private static MangoCore instance;

    private static String PREFIX = "&dMango &f| &dCore ";

    private MangoWorkspace fileManager;

    @Override
    public void onEnable() {
        instance = this;
        fileManager = new MangoWorkspace(this);

        MangoJsonConfig mangoJsonConfig = fileManager.loadFile(getDataFolder() + "/option.json", MangoJsonConfig.class);
        System.out.println("获取的文件对象：" + mangoJsonConfig);

        MessageUtils.consoleMessage(PREFIX + "&7plugin enable &a[ SUCCESS ]");
    }

    public static void registerCommand(Object obj) {
        CommandRegister registrar = new CommandRegister(instance);
        registrar.registerCommand(obj);
    }

    @Override
    public void onDisable() {
        MessageUtils.consoleMessage(PREFIX + "&7plugin disable &a[ SUCCESS ]");
    }
}
