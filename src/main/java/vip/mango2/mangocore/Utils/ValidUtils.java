package vip.mango2.mangocore.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidUtils {

    public static boolean isValidURL(String url) {
        String regex = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";
        return url.matches(regex);
    }

    /**
     * 自定义正则表达式验证
     * @param pattern 正则表达式
     * @param input 输入
     * @return 是否匹配
     */
    public static boolean customValid(String pattern, String input) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(input);
        return m.find();
    }

    /**
     * 获取正则表达式匹配的字符串
     * @param pattern 正则表达式
     * @param input 输入
     * @return 匹配的字符串
     */
    public static String getCustomValid(String pattern, String input) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(input);
        if (m.find()) {
            return m.group();
        }
        return null;
    }
}
