package vip.mango2.mangocore;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import vip.mango2.mangocore.Annotation.impl.Command.CommandRegister;
import vip.mango2.mangocore.Entity.DataBaseConfig;
import vip.mango2.mangocore.Entity.test.TClass;
import vip.mango2.mangocore.Entity.test.TStudent;
import vip.mango2.mangocore.Enum.FileType;
import vip.mango2.mangocore.Manager.MangoFileManager;
import vip.mango2.mangocore.Utils.MessageUtils;

import java.util.ArrayList;
import java.util.List;

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
        // 对象测试
        System.out.println("Json测试 | 对象DataBaseConfig:" + fileManager.getFile("option.json").get("demoDataBaseConfig", DataBaseConfig.class));
        System.out.println("Json测试 | 嵌套对象TClass:" + fileManager.getFile("option.json").get("classes", TClass.class));
        System.out.println("Json测试 | 学生对象列表:" + fileManager.getFile("option.json").get("stuList", List.class));
        System.out.println("Json测试 | 班级对象列表:" + fileManager.getFile("option.json").get("classesList", ArrayList.class));
        System.out.println("Yaml测试 | 对象DataBaseConfig:" + fileManager.getFile("config.yml").get("demoDataBaseConfig", DataBaseConfig.class));
        System.out.println("Yaml测试 | 嵌套对象TClass:" + fileManager.getFile("config.yml").get("classes", TClass.class));
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
