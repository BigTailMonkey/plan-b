package com.btm.planb.diffobject.generate.util;

import java.util.Objects;

public final class StringUtils {

    public static String convertInitialUpper(String word) {
        if (Objects.isNull(word)) {
            return null;
        }
        if (word.isEmpty()) {
            return "";
        }
        char[] chars = word.toCharArray();
        chars[0] -= 32;
        return new String(chars);
    }

}
