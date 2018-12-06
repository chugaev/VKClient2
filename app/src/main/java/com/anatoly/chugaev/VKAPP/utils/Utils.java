package com.anatoly.chugaev.VKAPP.utils;

import java.util.Calendar;

public class Utils {
    public static String getFormatTime(long time) {
        Calendar mydate = Calendar.getInstance();
        mydate.setTimeInMillis(time * 1000);
        String hours = String.valueOf(mydate.get(Calendar.HOUR_OF_DAY));
        String minutes = String.valueOf(mydate.get(Calendar.MINUTE));
        if (hours.length() == 1) {
            hours = "0" + hours;
        }
        if (minutes.length() == 1) {
            minutes = "0" + minutes;
        }
        return hours + ":" + minutes;
    }
}
