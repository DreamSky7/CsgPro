package vip.mango2.mangocore.Utils;

import lombok.NoArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import vip.mango2.mangocore.Entity.File.MangoConfiguration;
import vip.mango2.mangocore.Manager.MangoFileManager;
import vip.mango2.mangocore.MangoCore;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 消息相关的工具类
 */
@NoArgsConstructor
public class MessageUtils {

    /**
     * 发送控制台消息
     * @param message 消息
     */
    public static void consoleMessage(String message) {
        senderMessage(Bukkit.getConsoleSender(), message);
    }

    /**
     * 由命令发送者发送消息
     * @param sender 发送者
     * @param message 消息
     */
    public static void senderMessage(CommandSender sender, String message) {
        senderMessage(sender, message, false, false, null);
    }

    /**
     * 由命令发送者发送消息
     * @param sender 发送者
     * @param message 消息
     * @param isExpand 是否解析扩展
     */
    public static void senderMessage(CommandSender sender, String message, boolean isExpand) {
        senderMessage(sender, message, isExpand, false, null);
    }

    /**
     * 由命令发送者发送消息
     * @param sender 发送者
     * @param message 消息
     * @param isExpand 是否解析扩展
     * @param usePlaceholder 是否使用占位符
     */
    public static void senderMessage(CommandSender sender, String message, boolean isExpand, boolean usePlaceholder) {
        senderMessage(sender, message, isExpand, usePlaceholder, null);
    }

    /**
     * 由命令发送者发送消息
     * @param sender 发送者
     * @param message 消息
     * @param isExpand 是否解析扩展
     * @param usePlaceholder 是否使用占位符
     */
    public static void senderMessage(CommandSender sender, String message, boolean isExpand, boolean usePlaceholder, Map<String, Object> params) {

        // 颜色代码
        message = ChatColor.translateAlternateColorCodes('&', message);

        // PAPI变量
        if (usePlaceholder && sender instanceof Player) {
            message = PlaceholderAPI.setPlaceholders((Player) sender, message);
        }

        // 自定义变量
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                message = message.replace("%" + key + "%", String.valueOf(value));
            }
        }

        if (isExpand) {
            if (message.startsWith("[title]")) {
                handleTitleMessage((Player) sender, message);
                return;
            } else if (message.startsWith("[actionbar]")) {
                actionBarMessage((Player) sender, message.replace("[actionbar]", ""));
                return;
            } else if (message.startsWith("[command]")) {
                executeCommand((Player) sender, message.replace("[command]", ""));
                return;
            } else if (message.startsWith("[console]")) {
                executeConsoleCommand(message.replace("[console]", ""));
                return;
            }
        }

        sender.sendMessage(message);
    }

    /**
     * 发送Config配置文件中的消息
     * @param fileConfiguration 配置文件
     * @param key 配置文件中的key
     * @param sender 发送者
     */
    public static void senderMessageByConfig(MangoConfiguration fileConfiguration, CommandSender sender, String key) {
        senderMessageByConfig(fileConfiguration, key, sender, false, false, null);
    }

    /**
     * 发送消息
     * @param fileConfiguration 配置文件
     * @param key 配置文件中的key
     * @param sender 发送者
     * @param isExpand 是否解析使用占位符
     */
    public static void senderMessageByConfig(MangoConfiguration fileConfiguration, String key, CommandSender sender, boolean isExpand) {
        senderMessageByConfig(fileConfiguration, key, sender, isExpand, false, null);
    }

    /**
     * 发送消息
     * @param fileConfiguration 配置文件
     * @param key 配置文件中的key
     * @param sender 发送者
     * @param usePlaceholder 是否解析使用占位符
     * @param isExpand 是否解析扩展
     */
    public static void senderMessageByConfig(MangoConfiguration fileConfiguration, String key, CommandSender sender, boolean isExpand, boolean usePlaceholder) {
        senderMessageByConfig(fileConfiguration, key, sender, usePlaceholder, isExpand, null);
    }

    /**
     * 发送Config配置文件中的消息
     * @param fileConfiguration 配置文件
     * @param key 配置文件中的key
     * @param sender 发送者
     * @param usePlaceholder 是否解析使用占位符
     * @param isExpand 是否解析扩展
     */
    public static void senderMessageByConfig(MangoConfiguration fileConfiguration, String key, CommandSender sender, boolean isExpand, boolean usePlaceholder, Map<String, Object> params) {

        if (fileConfiguration.get(key) == null)
            return;

        List<String> messages = new ArrayList<>();
        if (fileConfiguration.get(key) instanceof List) {
            messages = fileConfiguration.getStringList(key);
        } else if (fileConfiguration.get(key) instanceof String) {
            messages.add(fileConfiguration.getString(key));
        }

        for (String message : messages) {
            senderMessage(sender, message, isExpand, usePlaceholder, null);
        }
    }

    /**
     * 发送Title消息（默认时间）
     * @param player 玩家
     * @param title 标题
     * @param subTitle 副标题
     */
    public static void titleMessage(Player player, String title, String subTitle) {
        titleMessage(player, title, subTitle, 10, 70, 20);
    }

    /**
     * 发送Title消息
     * @param player 玩家
     * @param title 标题
     * @param subTitle 副标题
     * @param inTime 淡入时间
     * @param time 持续时间
     * @param outTime 淡出时间
     */
    public static void titleMessage(Player player, String title, String subTitle, int inTime, int time, int outTime) {
        player.sendTitle(ChatColor.translateAlternateColorCodes('&', title),
                ChatColor.translateAlternateColorCodes('&', subTitle), inTime, time, outTime);
    }

    /**
     * 发送标题消息
     * @param player 玩家
     * @param message 消息
     */
    private static void handleTitleMessage(Player player, String message) {
        String[] splitTitle = message.replace("[title]", "").split(";");

        if (splitTitle.length == 2) {
            player.sendTitle(splitTitle[0], splitTitle[1], 10, 70, 20);
        } else if (splitTitle.length == 5) {
            try {
                int inTime = Integer.parseInt(splitTitle[2]);
                int duration = Integer.parseInt(splitTitle[3]);
                int outTime = Integer.parseInt(splitTitle[4]);
                player.sendTitle(splitTitle[0], splitTitle[1], inTime, duration, outTime);
            } catch (NumberFormatException e) {
                player.sendMessage("标题文本格式错误");
            }
        } else {
            player.sendMessage("标题文本格式错误");
        }
    }

    /**
     * 发送ActionBar消息
     * @param player 玩家
     * @param message 消息
     */
    public static void actionBarMessage(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                new TextComponent(ChatColor.translateAlternateColorCodes('&', message)));
    }

    /**
     * 以玩家身份执行指令
     * @param player 玩家
     * @param command 指令
     */
    private static void executeCommand(Player player, String command) {
        player.setOp(true);
        Bukkit.dispatchCommand(player, command);
        player.setOp(false);
    }

    /**
     * 控制台执行指令
     * @param command 指令
     */
    private static void executeConsoleCommand(String command) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }
}
