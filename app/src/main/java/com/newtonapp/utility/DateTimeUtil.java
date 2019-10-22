package com.newtonapp.utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtil {

    public static String DATE_TIME_PATTERN_1 = "dd/MM/yyyy HH:mm:ss";
    public static String DATE_TIME_PATTERN_2 = "dd/MM/yyyy";

    public static String getCurrentDate(String pattern){
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        return simpleDateFormat.format(currentDate);
    }
}
