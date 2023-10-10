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

    public static ConfigurationManager configurationManager;

    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic
        registerCommand(new CommandTest());

        configurationManager = new ConfigurationManager(this);
        configurationManager.loadConfig("config");

        if (configurationManager.getConfig("config").isPresent()) {
            YamlConfiguration config = configurationManager.getConfig("config").get();
            // 基本对象测试
            YamlUtils.saveObjectToConfig(config, new TStudent("张三",14, false), "基本对象测试");

            // 基本List测试
            YamlUtils.saveObjectToConfig(config, Arrays.asList("测试", 1, true), "基本List测试");

            // 基本List对象
            List<TStudent> studentList1 = new ArrayList<>();
            studentList1.add(new TStudent("杨杰", 67, true));
            studentList1.add(new TStudent("陈晨", 45, false));
            studentList1.add(new TStudent("张磊", 23, true));
            YamlUtils.saveObjectToConfig(config, studentList1, "基本List对象");

            // 基本嵌套对象
            TClass tClass1 = new TClass();
            tClass1.setGrade(1);
            tClass1.setName("一班");
            tClass1.setStudents(studentList1);
            YamlUtils.saveObjectToConfig(config, tClass1, "嵌套对象");

            // 基本嵌套List
            List<TStudent> studentList2 = new ArrayList<>();
            studentList2.add(new TStudent("孟乐", 78, false));
            studentList2.add(new TStudent("韩梅", 91, true));
            studentList2.add(new TStudent("陈晨", 12, false));

            TClass tClass2 = new TClass();
            tClass2.setGrade(1);
            tClass2.setName("二班");
            tClass2.setStudents(studentList2);

            List<TClass> tClasses = new ArrayList<>();
            tClasses.add(tClass1);
            tClasses.add(tClass2);
            YamlUtils.saveObjectToConfig(config, tClasses, "List嵌套对象");

            configurationManager.saveConfig("config");


        }

        configurationManager.loadConfig("message");
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
