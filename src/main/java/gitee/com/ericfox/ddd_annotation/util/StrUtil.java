package gitee.com.ericfox.ddd_annotation.util;

import java.util.regex.Pattern;

public class StrUtil {
    public static String lowerCamelToUpperSnake(String lowerCamel) {
        if (lowerCamel == null) {
            return null;
        }
        if (Pattern.matches("^\\s*$", lowerCamel)) {
            return lowerCamel;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < lowerCamel.length(); i++) {
            char c = lowerCamel.charAt(i);
            if (Character.isUpperCase(c)) {
                stringBuilder.append("_");
            }
            stringBuilder.append(Character.toUpperCase(c));
        }
        return stringBuilder.toString();
    }
}
