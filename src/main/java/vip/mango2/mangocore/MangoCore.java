package vip.mango2.mangocore;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import vip.mango2.mangocore.Annotation.impl.Command.CommandRegister;
import vip.mango2.mangocore.Entity.Configuration.MangoJsonConfig;
import vip.mango2.mangocore.Entity.Configuration.MangoYamlConfig;
import vip.mango2.mangocore.Manager.MangoWorkspace;
import vip.mango2.mangocore.Utils.MessageUtils;

import java.net.URL;

public final class MangoCore extends JavaPlugin {

    @Getter
    private static MangoCore instance;

    private static String PREFIX = "&dMango &f| &dCore ";

    private MangoWorkspace fileManager;

    @Override
    public void onEnable() {
        instance = this;
        fileManager = new MangoWorkspace(this);

        MangoJsonConfig mangoJsonConfig = fileManager.loadFile("option.json", MangoJsonConfig.class);
        System.out.println("Config version: "+mangoJsonConfig.getString("version"));
        fileManager.saveFile("option.json");

//        fileManager.loadFile("strange.yml", MangoYamlConfig.class, new URL(
//                "https://portrait.gitee.com/MangoRabbit/mango-core/blob/45aabfc0c642ec3ec8cc7169a9ecd8b4612e6974/src/main/java/vip/mango2/mangocore/Entity/Configuration/MangoJsonConfig.java"
//        ));
//        fileManager.saveFile("strange.yml");
//
//        space.loadResource(new URL("jar:///config.json"), MangoJsonConfig.class);

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
