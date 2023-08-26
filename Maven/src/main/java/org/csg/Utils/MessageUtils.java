package org.csg.Utils;

import org.bukkit.ChatColor;
import org.csg.Fwmain;

public class MessageUtils {

    /**
     * 控制台输出常规信息
     * @param info
     */
    public static void ConsoleInfoMsg(String info) {
        Fwmain.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&a[ INFO ]&r " + info));
    }

    /**
     * 控制台输出警告信息
     * @param info
     */
    public static void ConsoleWarnMsg(String info) {
        Fwmain.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&e[ WARN ]&r " + info));
    }

    /**
     * 控制台输出错误信息
     * @param info
     */
    public static void ConsoleErrorMsg(String info) {
        Fwmain.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&c[ ERROR ]&r " + info));
    }

    /**
     * 控制台输出调试信息
     * @param str
     */
    public static void ConsoleDebugMsg(String str){
        if(Fwmain.getInstance().isDebug()){
            Fwmain.getInstance().getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&e[ DEBUG ]&r " + str));
        }
    }
}
