package com.kaihuang.bintutu.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TimeUtils
 */
public class TimeUtils {

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat DATE_FORMAT_MONTH_DAY = new SimpleDateFormat("MM月dd日");
    public static final SimpleDateFormat DATE_FORMAT_YEAR_MONTH_DAY = new SimpleDateFormat("yyyy年MM月dd日");
    public static final SimpleDateFormat DATE_FORMAT_YEAR_MONTH= new SimpleDateFormat("yyyy年MM月");
    public static final SimpleDateFormat DATE_FORMAT_YEAR_MONTH_DAY_SECOND = new SimpleDateFormat("yyyy年MM月dd日  HH时mm分");
    /**
     * long time to string
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    public static Date stringToDate(SimpleDateFormat simpleDateFormat ,String dateStr) {

        Date date = null;
        try {
            date = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    public static String dateToString(SimpleDateFormat simpleDateFormat,Date date){
        return simpleDateFormat.format(date);
    }


    public static long dateToLong(String datestr) throws ParseException {

        Date date = DEFAULT_DATE_FORMAT.parse(datestr);
        long ts = date.getTime();
        return ts;
    }
}
