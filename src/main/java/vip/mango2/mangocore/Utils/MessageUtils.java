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
import vip.mango2.mangocore.MangoCore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    public static void senderMessage(CommandSender sender, String message) {
        senderMessage(sender, message, false, false);
    }

    public static void senderMessage(CommandSender sender, String message, boolean usePlaceholder) {
        senderMessage(sender, message, usePlaceholder, false);
    }

    /**
     * 由命令发送者发送消息
     * @param sender 发送者
     * @param message 消息
     * @param isExpand 是否解析扩展
     * @param usePlaceholder 是否使用占位符
     */
    public static void senderMessage(CommandSender sender, String message, boolean usePlaceholder, boolean isExpand) {

        message = ChatColor.translateAlternateColorCodes('&', message);

        Player senderPlayer = null;
        if (sender instanceof Player) {
            senderPlayer = (Player) sender;
            if (usePlaceholder) {
                message = PlaceholderAPI.setPlaceholders((Player) sender, message);
            }
        }

        if (isExpand) {
            String customValid = ValidUtils.getCustomValid("\\[.*?\\].*", message);
            if (customValid != null && senderPlayer != null) {
                    switch (customValid) {
                        case "[title]":
                            handleTitleMessage(senderPlayer, message);
                            return;
                        case "[actionbar]":
                            actionBarMessage(senderPlayer, message.replace("[actionbar]", ""));
                            return;
                        case "[command]":
                            senderPlayer.setOp(true);
                            Bukkit.dispatchCommand(senderPlayer, message.replace("[command]", ""));
                            senderPlayer.setOp(false);
                            return;
                        case "[console]":
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), message.replace("[console]", ""));
                            return;
                    }
            }
            senderMessage(sender, message, false, false);
        }
    }

    /**
     * 发送标题消息
     * @param player 玩家
     * @param message 消息
     */
    private static void handleTitleMessage(Player player, String message) {
        String[] splitTitle = message.replace("[title]", "").split(";");

        if (splitTitle.length == 2) {
            titleMessage(player, splitTitle[0], splitTitle[1]);
        } else if (splitTitle.length == 5) {
            titleMessage(player, splitTitle[0], splitTitle[1],
                    Integer.parseInt(splitTitle[2]), Integer.parseInt(splitTitle[3]), Integer.parseInt(splitTitle[4]));
        } else {
            senderMessage(player, "&cTitle消息格式错误", false, false);
        }
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
     * 发送Title消息（默认时间）
     * @param player 玩家
     * @param title 标题
     * @param subTitle 副标题
     */
    public static void titleMessage(Player player, String title, String subTitle) {
        titleMessage(player, title, subTitle, 10, 70, 20);
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
     * 发送自定义消息
     * @param messages 消息列表
     * @param sender 发送者
     * @param params 参数
     * @param isExpand 是否解析变量
     */
    public static void senderMessageByList(List<String> messages, CommandSender sender, Map<String, Object> params, boolean isExpand) {
        // 循环列表消息并解析对应的变量
        formatterMessageParams(sender, params, isExpand, messages);
    }

    /**
     * 发送ActionBar消息
     * @param fileConfiguration 配置文件
     * @param key 配置文件中的key
     * @param sender 发送者
     */
    public static void senderMessageByConfig(FileConfiguration fileConfiguration, String key, CommandSender sender, Map<String, Object> params, boolean isExpand) {

        // 判断配置文件是否存在该Key
        if (fileConfiguration.get(key) == null)
            return;

        // 兼容多条消息
        List<String> messages = new ArrayList<>();

        if (fileConfiguration.get(key) instanceof List) {
            // 判断是否为List
            messages = fileConfiguration.getStringList(key);
        } else if (fileConfiguration.get(key) instanceof String) {
            // 判断是否为String
            messages.add(fileConfiguration.getString(key));
        }

        // 循环列表消息并解析对应的变量
        formatterMessageParams(sender, params, isExpand, messages);

    }

    private static void formatterMessageParams(CommandSender sender, Map<String, Object> params, boolean isExpand, List<String> messages) {
        for (String message : messages) {

            // 正则表达式匹配需要的变量
            Pattern pattern = Pattern.compile("%(.*?)%");
            Matcher matcher = pattern.matcher(message);

            List<String> expandedMessages = new ArrayList<>();
            expandedMessages.add(message);

            // 过滤变量并替换
            if (params != null) {
                while (matcher.find()) {
                    String variable  = matcher.group(1).replace("%", "");
                    if (params.containsKey(variable)) {
                        Object value = params.get(variable);
                        if (value instanceof List) {
                            List<?> valueList = (List<?>) value;
                            List<String> newExpandedMessages = new ArrayList<>();
                            for (String expandedMsg : expandedMessages) {
                                for (Object item : valueList) {
                                    newExpandedMessages.add(expandedMsg.replace("%" + variable + "%", item.toString()));
                                }
                            }
                            expandedMessages = newExpandedMessages;
                        } else {
                            for (int i = 0; i < expandedMessages.size(); i++) {
                                expandedMessages.set(i, expandedMessages.get(i).replace("%" + variable + "%", value.toString()));
                            }
                        }
                    }
                }
            }


            for (String expandedMessage : expandedMessages) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (expandedMessage.startsWith("[title]")) { // 发送Title消息
                        String[] splitTitle = expandedMessage.replace("[title]", "").split(";");
                        if (splitTitle.length == 2) {
                            titleMessage(player, splitTitle[0], splitTitle[1]);
                        } else if (splitTitle.length == 5) {
                            titleMessage(player, splitTitle[0], splitTitle[1],
                                    Integer.parseInt(splitTitle[2]), Integer.parseInt(splitTitle[3]), Integer.parseInt(splitTitle[4]));
                        } else {
                            senderMessage(sender, "&cTitle消息格式错误");
                        }
                        return;
                    } else if (expandedMessage.startsWith("[actionbar]")) { // 发送ActionBar消息
                        actionBarMessage(player, expandedMessage.replace("[actionbar]", ""));
                        return;
                    }
                    if (isExpand) {
                        if (expandedMessage.startsWith("[command]")) { // 执行指令
                            player.setOp(true);
                            Bukkit.dispatchCommand(player, expandedMessage.replace("[command]", ""));
                            player.setOp(false);
                            return;
                        } else if (expandedMessage.startsWith("[console]")) { // 控制台
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), expandedMessage.replace("[command]", ""));
                            return;
                        }
                    }
                }
                senderMessage(sender, expandedMessage);
            }
        }
    }
}
