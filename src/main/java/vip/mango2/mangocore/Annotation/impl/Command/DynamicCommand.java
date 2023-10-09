package vip.mango2.mangocore.Annotation.impl.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import vip.mango2.mangocore.Annotation.TabComplate;
import vip.mango2.mangocore.Utils.MessageUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class DynamicCommand extends Command implements TabCompleter {

    private final Object instance;

    private final Method method;

    private Method tabComplateMethod;

    public DynamicCommand(String name, Object instance, Method method, String description, String[] aliases, String permission, String usage) {
        super(name, description, usage, Arrays.asList(aliases));
        this.instance = instance;
        this.method = method;
        this.setPermission(permission);
    }

    public void setTabComplateMethod(Method tabComplateMethod) {
        this.tabComplateMethod = tabComplateMethod;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        try {
            return (boolean) method.invoke(instance, sender, args);
        } catch (Exception e) {
            MessageUtils.consoleMessage("&c执行指令时出现错误: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        System.out.println("触发补全");
        if (tabComplateMethod != null) {
            try {
                List<String> ceshi =  (List<String>) tabComplateMethod.invoke(instance, sender, args);
                System.out.println(ceshi);
                return (List<String>) tabComplateMethod.invoke(instance, sender, args);
            } catch (Exception e) {
                // 你的错误处理代码
                MessageUtils.consoleMessage("&c代码补全参数异常: " + e.getMessage());
            }
        }
        return null;
    }


}
