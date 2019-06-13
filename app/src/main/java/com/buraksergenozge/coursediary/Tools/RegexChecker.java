package com.buraksergenozge.coursediary.Tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexChecker {
    public static final String datePattern = "^\\s*([0-2]?[0-9]|3[01])\\-(0?[1-9]|1[0-2])\\-[12][0-9]{3}\\s*$";
    public static final String clockPattern = "^\\s*([01]?[0-9]|2[0-3]):[0-5][0-9]\\s*$";
    public static String numberPattern = "^\\s*[0-9]*\\s*$";
    public static final String floatPattern = "^\\s*[0-9]+(.[0-9]+|[0-9]*)\\s*$";

    public static boolean check(String input, String checkPattern) {
        Pattern pattern = Pattern.compile(checkPattern);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
}