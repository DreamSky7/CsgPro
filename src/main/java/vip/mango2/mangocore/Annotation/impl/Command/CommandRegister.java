package vip.mango2.mangocore.Annotation.impl.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;
import vip.mango2.mangocore.Annotation.Command;
import vip.mango2.mangocore.Annotation.TabComplate;
import vip.mango2.mangocore.Utils.MessageUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommandRegister {

    private Plugin plugin;

    public CommandRegister(Plugin plugin) {
        this.plugin = plugin;
    }


    public void registerCommand(Object object) {
        Map<String, DynamicCommand> tempCommands = new HashMap<>();

        // Step 1: 创建DynamicCommand实例并存储在tempCommands映射中
        for (Method declaredMethod : object.getClass().getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(Command.class)) {
                Command annotation = declaredMethod.getAnnotation(Command.class);
                DynamicCommand dynamicCommand = new DynamicCommand(
                        annotation.name(),
                        object,
                        declaredMethod,
                        annotation.description(),
                        annotation.alias(),
                        annotation.permission(),
                        annotation.usage()
                );
                tempCommands.put(annotation.name(), dynamicCommand);
            }
        }

        // Step 2: 设置Tab补全方法
        for (Method declaredMethod : object.getClass().getDeclaredMethods()) {
            if (declaredMethod.isAnnotationPresent(TabComplate.class)) {
                // 存在注解
                MessageUtils.consoleMessage("存在补全注解");
                TabComplate tabCompleteAnnotation = declaredMethod.getAnnotation(TabComplate.class);
                DynamicCommand dynamicCommand = tempCommands.get(tabCompleteAnnotation.command());
                if (dynamicCommand != null) {
                    dynamicCommand.setTabComplateMethod(declaredMethod);
                } else {
                    MessageUtils.consoleMessage("&c找不到与TabComplate注解匹配的命令: " + tabCompleteAnnotation.command());
                }
            }
        }

        // Step 3: 注册命令
        for (DynamicCommand dynamicCommand : tempCommands.values()) {
            Objects.requireNonNull(getCommandMap()).register(plugin.getName(), dynamicCommand);
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
