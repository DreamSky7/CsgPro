package vip.mango2.mangocore;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import vip.mango2.mangocore.Annotation.impl.Command.CommandRegister;
import vip.mango2.mangocore.Entity.Configuration.MangoJsonConfig;
import vip.mango2.mangocore.Entity.Configuration.MangoYamlConfig;
import vip.mango2.mangocore.Entity.DataBaseConfig;
import vip.mango2.mangocore.Entity.test.TClass;
import vip.mango2.mangocore.Entity.test.TStudent;
import vip.mango2.mangocore.Manager.MangoWorkspace;
import vip.mango2.mangocore.Utils.MessageUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class MangoCore extends JavaPlugin {

    @Getter
    private static MangoCore instance;

    private static String PREFIX = "&dMango &f| &dCore ";

    private MangoWorkspace fileManager;

    @Override
    public void onEnable() {
        instance = this;
        fileManager = new MangoWorkspace(this);



        MangoJsonConfig mangoJsonConfig = fileManager.loadFile("demo.json", MangoJsonConfig.class,"http://192.168.3.117:8080/option.json");
        //MangoJsonConfig mangoJsonConfig = fileManager.loadFile("option.json", MangoJsonConfig.class);
        System.out.println("Config version: "+ mangoJsonConfig.getString("version"));
        fileManager.saveFile("demo.json");

        // 对象测试
        System.out.println("Json测试 | 对象DataBaseConfig:" + mangoJsonConfig.get("demoDataBaseConfig", DataBaseConfig.class));
        System.out.println("Json测试 | 嵌套对象TClass:" + mangoJsonConfig.get("classes", TClass.class));
        System.out.println("Json测试 | 学生对象列表:" + mangoJsonConfig.get("stuList", List.class));
        System.out.println("Json测试 | 班级对象列表:" + mangoJsonConfig.get("classesList", ArrayList.class));
        System.out.println("Json测试 | 学生对象列表:" + mangoJsonConfig.getList("stuList", TStudent.class));
        System.out.println("Json测试 | 班级对象列表:" + mangoJsonConfig.getList("classesList", TClass.class));
        System.out.println("Json测试 | 学生Map集合:" + mangoJsonConfig.getMap("mapStu", TStudent.class));
        System.out.println("Json测试 | 班级Map集合:" + mangoJsonConfig.getMap("mapClass", TClass.class));
        System.out.println("Yaml测试 | 班级MapList集合:" + mangoJsonConfig.getMapList("mapListClass", TClass.class));


        MangoYamlConfig mangoYamlConfig = fileManager.loadFile("config.yml", MangoYamlConfig.class);
        System.out.println("Yaml测试 | 对象DataBaseConfig:" + mangoYamlConfig.get("demoDataBaseConfig", DataBaseConfig.class));
        System.out.println("Yaml测试 | 嵌套对象TClass:" + mangoYamlConfig.get("classes", TClass.class));
        System.out.println("Yaml测试 | 学生对象列表:" + mangoYamlConfig.getList("stuList", TStudent.class));
        System.out.println("Yaml测试 | 班级对象列表:" + mangoYamlConfig.getList("classesList", TClass.class));
        System.out.println("Yaml测试 | 学生Map集合:" + mangoYamlConfig.getMap("mapStu", TStudent.class));
        System.out.println("Yaml测试 | 班级Map集合:" + mangoYamlConfig.getMap("mapClass", TClass.class));
        System.out.println("Yaml测试 | 班级MapList集合:" + mangoYamlConfig.getMapList("mapListClass", TClass.class));
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
