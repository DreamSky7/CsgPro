package vip.mango2.mangocore.Utils;

public class ValidUtils {

    public static boolean isValidURL(String url) {
        String regex = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";
        return url.matches(regex);
    }
}
