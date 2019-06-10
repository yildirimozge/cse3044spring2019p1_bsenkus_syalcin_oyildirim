package com.buraksergenozge.coursediary.Tools;

import android.content.res.Resources;

import com.buraksergenozge.coursediary.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class StringManager {
    private StringManager() {}

    public static String getTimeRepresentation(long milliseconds, Resources resources) { // Returns a string in format of .. week, .. days
        String repr = "";
        long weeks = milliseconds / 604800000;
        milliseconds = milliseconds % 604800000;
        long days = milliseconds / 86400000;
        milliseconds = milliseconds % 86400000;
        long hours = milliseconds / 3600000;
        milliseconds = milliseconds % 3600000;
        long minutes = milliseconds / 60000;
        if (weeks > 0) {
            if (weeks == 1)
                repr += weeks + " " + resources.getString(R.string.week);
            else
                repr += weeks + " " + resources.getString(R.string.weeks);
        }
        if (days > 0) {
            if (repr.length() > 0)
                repr += ", ";
            if (days == 1)
                repr += days + " " + resources.getString(R.string.day);
            else
                repr += days + " " + resources.getString(R.string.days);
        }
        if (hours > 0 && !repr.contains(",")) {
            if (repr.length() > 0)
                repr += ", ";
            if (hours == 1)
                repr += hours + " " + resources.getString(R.string.hour);
            else
                repr += hours + " " + resources.getString(R.string.hours);
        }
        if (minutes > 0 && !repr.contains(",")) {
            if (repr.length() > 0)
                repr += ", ";
            if (minutes == 1)
                repr += minutes + " " + resources.getString(R.string.min);
            else
                repr += minutes + " " + resources.getString(R.string.mins);
        }
        if (weeks == 0 && days == 0 && hours == 0 && minutes == 0)
            return resources.getString(R.string.over);
        else
            return repr;
    }

    public static String getDateString(Calendar calendar, String pattern) {
        String repr;
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        dateFormat.setTimeZone(calendar.getTimeZone());
        repr = dateFormat.format(calendar.getTime());
        return repr;
    }
}
