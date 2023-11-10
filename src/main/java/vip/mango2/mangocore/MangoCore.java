package vip.mango2.mangocore;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import vip.mango2.mangocore.Annotation.impl.Command.CommandRegister;
import vip.mango2.mangocore.Entity.Configuration.MangoJsonConfig;
import vip.mango2.mangocore.Entity.Configuration.MangoYamlConfig;
import vip.mango2.mangocore.Entity.DataBaseConfig;
import vip.mango2.mangocore.Entity.File.MangoDirectory;
import vip.mango2.mangocore.Entity.test.TClass;
import vip.mango2.mangocore.Entity.test.TStudent;
import vip.mango2.mangocore.Manager.MangoWorkspace;
import vip.mango2.mangocore.Utils.MessageUtils;

import java.io.File;
import java.io.IOException;
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
        fileManager = MangoWorkspace.getWorkspace(this);

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
