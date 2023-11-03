package vip.mango2.mangocore.Utils;

import java.util.Random;

public class RandomUtils {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int STRING_LENGTH = 8;
    private static final Random RANDOM = new Random();

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
            s = RANDOM.nextInt(length) + max;
        } else if (min < max) {
            length = max - min;
            s = RANDOM.nextInt(length) + min;
        } else {
            return min;
        }
        return s;
    }

    public static String generateRandomString() {
        StringBuilder stringBuilder = new StringBuilder(STRING_LENGTH);
        for (int i = 0; i < STRING_LENGTH; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            stringBuilder.append(CHARACTERS.charAt(randomIndex));
        }
        return stringBuilder.toString();
    }
}
