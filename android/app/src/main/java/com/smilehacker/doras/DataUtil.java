package com.smilehacker.doras;

import com.google.code.regexp.Pattern;

/**
 * Created by zhouquan on 16/8/20.
 */
public class DataUtil {
    private static final Pattern DOUBLE_PATTERN = Pattern.compile(
            "[\\x00-\\x20]*[+-]?(NaN|Infinity|((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)" +
                    "([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|" +
                    "(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))" +
                    "[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*");
    private static final Pattern INT_PATTERN = Pattern.compile("\\d+");

    public static boolean isInt(String val) {
        return INT_PATTERN.matcher(val).matches();
    }


    public static boolean isFloat(String s) {
        return DOUBLE_PATTERN.matcher(s).matches();
    }
}
