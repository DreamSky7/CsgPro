package vip.mango2.mangocore.Test;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import vip.mango2.mangocore.Annotation.MangoCommand;
import vip.mango2.mangocore.Annotation.MangoTabComplate;
import vip.mango2.mangocore.MangoCore;
import vip.mango2.mangocore.Utils.MessageUtils;
import vip.mango2.mangocore.Utils.ValidUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandTest {

    @MangoCommand(name = "yaml", description = "管理已存在的配置文件", usage = "/yaml")
    public boolean helloCommand(CommandSender sender, String[] args) {
        if (args.length == 0) {
            helpMessage(sender);
            return false;
        }
        switch (args[0]) {
            case "get":
                if (args.length != 3) {
                    MessageUtils.senderMessage(sender, "&c/yaml get [配置文件] [键值]");
                    return false;
                }
                if (!MangoCore.configurationManager.configs.containsKey(args[1])) {
                    MessageUtils.senderMessage(sender, "&c配置文件不存在");
                    return false;
                }
                MessageUtils.senderMessage(sender, "&8> &f" + args[2] + ": " + MangoCore.configurationManager.configs.get(args[1]).get(args[2]));
                break;
            case "load":
                if (args.length == 2) {
                    if (MangoCore.configurationManager.configs.containsKey(args[1])) {
                        MessageUtils.senderMessage(sender, "&c配置文件已存在");
                        return false;
                    }
                    MangoCore.configurationManager.loadConfig(args[1]);
                } else if (args.length == 3) {
                    MangoCore.configurationManager.loadConfigFromURL(args[1], args[2]);
                } else {
                    MessageUtils.senderMessage(sender, "&c/yaml load [配置文件] <URL>");
                }
                break;
            case "reload":
                if (args.length != 2) {
                    MessageUtils.senderMessage(sender, "&c/yaml reload [配置文件]");
                }
                MangoCore.configurationManager.reloadConfig(args[1]);
                MessageUtils.senderMessage(sender, "&a重载配置文件成功");
                break;
            case "reloadAll":
                MangoCore.configurationManager.reloadAllConfig();
                MessageUtils.senderMessage(sender, "&a重载所有配置文件成功");
                break;
            default:
                helpMessage(sender);
                break;
        }
        return true;
    }

    @MangoTabComplate(command = "yaml")
    public List<String> yamlCmdTabComplater(CommandSender sender, String[] args) {
        List<String> collection = new ArrayList<>();
        if (args.length == 1) {
            collection = Arrays.asList("get", "reload", "reloadAll");
        }
        if (args.length == 2) {
            if ("reload".equals(args[0])) {
                collection.addAll(MangoCore.configurationManager.configs.keySet());
            }
        }
        return collection;
    }

    public void helpMessage(CommandSender sender) {
        MessageUtils.senderMessage(sender, "&dMangoCore &7- &f帮助信息");
        MessageUtils.senderMessage(sender, "&8");
        MessageUtils.senderMessage(sender, "&7/yaml get [配置文件] [键值] &7- &f获取指定配置文件中的值");
        MessageUtils.senderMessage(sender, "&7/yaml load [配置文件] <URL> &7- &f加载指定配置文件（支持URL）");
        MessageUtils.senderMessage(sender, "&7/yaml reload [配置文件] &7- &f重载指定配置文件");
        MessageUtils.senderMessage(sender, "&7/yaml reloadAll &7- &f重载所有配置文件");
        MessageUtils.senderMessage(sender, "&8");
    }
}
