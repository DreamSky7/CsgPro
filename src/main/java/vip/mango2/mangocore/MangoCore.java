package vip.mango2.mangocore;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import vip.mango2.mangocore.Annotation.impl.Command.CommandRegister;
import vip.mango2.mangocore.Enum.FileType;
import vip.mango2.mangocore.Manager.MangoFileManager;
import vip.mango2.mangocore.Utils.MessageUtils;

public final class MangoCore extends JavaPlugin {

    @Getter
    private static MangoCore instance;

    private static String PREFIX = "&dMango &f| &dCore ";

    private MangoFileManager fileManager;

    @Override
    public void onEnable() {
        instance = this;
        fileManager = new MangoFileManager(this);

        fileManager.loadFile("config.yml", FileType.YAML);
        fileManager.loadFile("option.json", FileType.JSON);
        System.out.println(fileManager.getFiles());
        System.out.println("获取的对象是:" + fileManager.getFile("config.yml"));
        System.out.println("获取的Yaml版本是:" + fileManager.getFile("config.yml").getInt("ceshi"));
        System.out.println("获取的Json版本是:" + fileManager.getFile("option.json").get("version"));
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
