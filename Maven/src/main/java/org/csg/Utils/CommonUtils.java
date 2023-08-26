package org.csg.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.csg.Fwmain;

import java.util.Random;
import java.util.regex.Pattern;

public class CommonUtils {

    public CommonUtils() { }


    public static Random random = new Random();

    /**
     * 判断字符串是否为数字
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[0-9]+(\\.[0-9]+)?$");
        return pattern.matcher(str).matches();
    }


    /**
     * 随机取件的整数
     * @param min 最小值
     * @param max 最大值
     * @return 随机的值
     */
    public static int Random(int min, int max) {
        int s;
        int length;
        if (min > max) {
            length = min - max;
            s = random.nextInt(length) + max;
        } else if (min < max) {
            length = max - min;
            s = random.nextInt(length) + min;
        } else {
            return min;
        }
        return s;
    }


    public static void ConsoleCommand(String command) {
        Bukkit.dispatchCommand(Fwmain.getInstance().getServer().getConsoleSender(), command);
    }

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int STRING_LENGTH = 8;
    private static final Random RANDOM = new Random();

    public static String generateRandomString() {
        StringBuilder stringBuilder = new StringBuilder(STRING_LENGTH);
        for (int i = 0; i < STRING_LENGTH; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            stringBuilder.append(CHARACTERS.charAt(randomIndex));
        }
        return stringBuilder.toString();
    }
}
