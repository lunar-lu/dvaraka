package org.lunarlu.dvaraka.web.common;

import org.apache.commons.lang3.StringUtils;
import org.lunarlu.commons.Matchers;

/**
 * @author jiaoteng
 */
public class ValidationUtils {

    private ValidationUtils() {
    }

    public static void checkApplicationPath(String applicationPath) {
        checkPath(applicationPath, "applicationPath");
    }

    public static void checkHttpPath(String path) {
        checkPath(path, "path");
    }

    private static void checkPath(String path, String name) {
        Matchers.of(path)
                .match("/").non()
                .match(StringUtils::isBlank).thenThrow(new InvalidPathException(path, name + " cannot be blank"))
                .match(str -> !str.startsWith("/")).thenThrow(new InvalidPathException(path, name + " must startWith /"))
                .match(str -> str.endsWith("/")).thenThrow(new InvalidPathException(path, name + " cannot endWith /"));

    }
}
