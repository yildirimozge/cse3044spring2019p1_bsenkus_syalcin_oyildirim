package com.buraksergenozge.coursediary;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexChecker {
    private static Pattern pattern;
    private static Matcher matcher;
    public static String datePattern = "^\\s*([0-2]?[0-9]|3[01])\\-(0?[1-9]|1[0-2])\\-[0-9]{4}\\s*$";
    public static String clockPattern = "^\\s*([01]?[0-9]|2[0-3]):[0-5][0-9]\\s*$";
    public static String numberPattern = "^\\s*[0-9]*\\s*$";

    public static boolean check(String input, String checkPattern) {
        pattern = Pattern.compile(checkPattern);
        matcher = pattern.matcher(input);
        return matcher.matches();
    }
}
