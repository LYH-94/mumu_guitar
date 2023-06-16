package util;

public class StringUtils {
    //判斷字串是否為 null 或 ""。
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
}
