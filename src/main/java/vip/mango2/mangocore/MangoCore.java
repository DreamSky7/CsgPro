package vip.mango2.mangocore;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import vip.mango2.mangocore.Annotation.impl.Command.CommandRegister;
import vip.mango2.mangocore.Entity.DataBaseConfig;
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
        // 自动
        System.out.println("Yaml测试 | 自动:" + fileManager.getFile("config.yml").get("version"));
        System.out.println("Json测试 | 自动:" + fileManager.getFile("option.json").get("version"));
        // int
        System.out.println("Yaml测试 | int:" + fileManager.getFile("config.yml").getInt("demoInt"));
        System.out.println("Json测试 | int:" + fileManager.getFile("option.json").getInt("demoInt"));

        // long
        System.out.println("Yaml测试 | long:" + fileManager.getFile("config.yml").getLong("demoLong"));
        System.out.println("Json测试 | long:" + fileManager.getFile("option.json").getLong("demoLong"));
        // float
        System.out.println("Yaml测试 | float:" + fileManager.getFile("config.yml").getFloat("demoFloat"));
        System.out.println("Json测试 | float:" + fileManager.getFile("option.json").getFloat("demoFloat"));
        // double
        System.out.println("Yaml测试 | double:" + fileManager.getFile("config.yml").getDouble("demoDouble"));
        System.out.println("Json测试 | double:" + fileManager.getFile("option.json").getDouble("demoDouble"));
        // char
        System.out.println("Yaml测试 | char:" + fileManager.getFile("config.yml").getChar("demoChar"));
        System.out.println("Json测试 | char:" + fileManager.getFile("option.json").getChar("demoChar"));
        // string
        System.out.println("Yaml测试 | string:" + fileManager.getFile("config.yml").getString("demoString"));
        System.out.println("Json测试 | string:" + fileManager.getFile("option.json").getString("demoString"));
        // boolean
        System.out.println("Yaml测试 | boolean:" + fileManager.getFile("config.yml").getBoolean("demoBoolean"));
        System.out.println("Json测试 | boolean:" + fileManager.getFile("option.json").getBoolean("demoBoolean"));
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
