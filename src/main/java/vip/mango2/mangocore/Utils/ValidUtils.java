package vip.mango2.mangocore.Utils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidUtils {

    /**
     * 判断字符串是否为URL地址
     * @param url URL地址
     * @return 是否为URL地址
     */
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

    /**
     * 判断字符串是否为数字
     * @param str 字符串
     * @return 是否为数字
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[0-9]+(\\.[0-9]+)?$");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断是否为自定义对象
     * @param clazz 类
     * @return 是否为自定义对象
     */
    public static boolean isCustomObject(Class<?> clazz) {
        // 检查是否为基本类型或包装类
        List<Class<?>> basicClassTypes = Arrays.asList(
                Integer.class, Double.class, Float.class,
                Long.class, Boolean.class,  String.class,
                BigDecimal.class,  Character.class
        );

        return !clazz.isPrimitive()
                && basicClassTypes.stream().noneMatch(nonCustomClass -> nonCustomClass.isAssignableFrom(clazz))
                && !Collection.class.isAssignableFrom(clazz)
                && !Map.class.isAssignableFrom(clazz);
    }
}
