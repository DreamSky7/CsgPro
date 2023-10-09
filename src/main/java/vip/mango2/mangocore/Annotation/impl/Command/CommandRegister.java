package vip.mango2.mangocore.Annotation.impl.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;
import vip.mango2.mangocore.Annotation.MangoCommand;
import vip.mango2.mangocore.Annotation.MangoTabComplate;
import vip.mango2.mangocore.MangoCore;
import vip.mango2.mangocore.Utils.MessageUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class CommandRegister {

    private final Plugin plugin;

    public CommandRegister(Plugin plugin) {
        this.plugin = plugin;
    }

    public void registerCommand(Object object) {
        for (Method declaredMethod : object.getClass().getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(MangoCommand.class)) {
                MangoCommand commandAnnotation = declaredMethod.getAnnotation(MangoCommand.class);

                // 使用流来检查是否存在MangoTabComplete注解
                Optional<Method> tabCompleteMethod = Arrays.stream(object.getClass().getDeclaredMethods())
                        .filter(method -> method.isAnnotationPresent(MangoTabComplate.class))
                        .filter(method -> {
                            MangoTabComplate tabComplateAnnotation = method.getAnnotation(MangoTabComplate.class);
                            return tabComplateAnnotation.command().equals(commandAnnotation.name());
                        })
                        .findFirst();

                DynamicCommand dynamicCommand = new DynamicCommand(
                        commandAnnotation.name(),
                        object,
                        declaredMethod,
                        commandAnnotation.description(),
                        commandAnnotation.alias(),
                        commandAnnotation.permission(),
                        commandAnnotation.usage(),
                        tabCompleteMethod.orElse(null)
                );

                // 注册指令
                Objects.requireNonNull(getCommandMap()).register(plugin.getName(), dynamicCommand);
                if (tabCompleteMethod.isPresent()) {
                    MessageUtils.consoleMessage("&d[MangoCore] &7动态注册指令: &a" + commandAnnotation.name() + " &7| &a" + "存在补全");
                } else {
                    MessageUtils.consoleMessage("&d[MangoCore] &7动态注册指令: &a" + commandAnnotation.name());
                }

            }
        }
    }

    private CommandMap getCommandMap() {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            return (CommandMap) commandMapField.get(Bukkit.getServer());
        } catch (Exception e) {
            MessageUtils.consoleMessage("&c无法获取CommandMap");
            return null;
        }
    }
}
