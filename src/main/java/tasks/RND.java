package tasks;

import java.util.Random;

public final class RND {
    private static final Random RANDOM = new Random();
    private static final String ALPHABET = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static double getRandomDouble(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(new Random().nextInt(10));
        }
        int dotPoint = length - 1 - new Random().nextInt(2);
        sb.insert(dotPoint, ".");
        return Double.parseDouble(sb.toString());
    }

    public static String getRandomString(int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = RANDOM.nextInt(62);
            sb.append(ALPHABET.charAt(number));
        }
        return sb.toString();
    }
}