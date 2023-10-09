package vip.mango2.mangocore.Test;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import vip.mango2.mangocore.Annotation.Command;
import vip.mango2.mangocore.Annotation.TabComplate;
import vip.mango2.mangocore.Utils.MessageUtils;

import java.util.ArrayList;
import java.util.List;

public class CommandTest {

    @Command(name = "hello", description = "Say hello!", usage = "/hello")
    public boolean helloCommand(CommandSender sender, String[] args) {
        sender.sendMessage("Hello from the annotated command!");
        return true;
    }

    @Command(name = "testalias", alias = {"ta", "tst"}, description = "Test alias command", usage = "/testalias")
    public boolean testAliasCommand(CommandSender sender, String[] args) {
        sender.sendMessage("This command has aliases!");
        return true;
    }

    @Command(name = "test", description = "A test command", usage = "/test")
    public boolean onTestCommand(CommandSender sender, String[] args) {
        MessageUtils.senderMessage(sender, "&a测试指令");
        return true;
    }

    @TabComplate(command = "test")
    public List<String> onTestTabComplete(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("option1");
            completions.add("option2");
        }
        return completions;
    }
}
