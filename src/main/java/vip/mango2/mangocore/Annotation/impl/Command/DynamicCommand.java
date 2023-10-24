package vip.mango2.mangocore.Annotation.impl.Command;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.TabExecutor;
import vip.mango2.mangocore.Utils.MessageUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class DynamicCommand extends Command {

    private final Object instance;

    private final Method method;

    private Method tabCompleteMethod;

    public DynamicCommand(String name, Object instance, Method method, String description, String[] aliases, String permission, String usage,Method tabCompleteMethod) {
        super(name, description, usage, Arrays.asList(aliases));
        this.instance = instance;
        this.method = method;
        this.setPermission(permission);
        this.tabCompleteMethod = tabCompleteMethod;
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        try {
            return (boolean) method.invoke(instance, sender, args);
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtils.consoleMessage("执行指令出现了异常");
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (tabCompleteMethod != null) {
            try {
                return (List<String>) tabCompleteMethod.invoke(instance, sender, args);
            } catch (Exception e) {
                MessageUtils.consoleMessage("&c执行TabComplete时出现错误: " + e.getMessage());
                return null;
            }
        }
        return super.tabComplete(sender, alias, args);
    }
}
