package com.kailaisi.handwritemvc.utils;

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2019/12/26 23:11
 */
public final class StringUtil {
    public static String lowerFirstCase(String str) {
        char[] array = str.toCharArray();
        array[0] += 32;
        return String.valueOf(array);
    }
}
