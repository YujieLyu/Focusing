package com.example.jessie.focusing.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author : Yujie Lyu
 * @date : 02-02-2019
 * @time : 11:47
 */
public class TimeHelper {
    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

    /**
     * Convert specific {@link Date} to {@link String}
     *
     * @param date
     * @return
     */
    public static String toString(Date date) {
        return sdf.format(date);
    }

    /**
     * Convert Date String to {@link Date}
     *
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static Date toDate(String dateStr) throws ParseException {
        return sdf.parse(dateStr);
    }

    public static long convertTime(int hour,int min){
        long time;
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,min);
        time=calendar.getTimeInMillis();
        return time;
    }

    public static Calendar getCurrCalendar(){
        Calendar calendar=Calendar.getInstance();
        return calendar;

    }
    public static int getCurrYear(){
        Calendar curr=getCurrCalendar();
        int year=curr.get(Calendar.YEAR);
        return year;
    }
    public static int getCurrMonth(){
        Calendar curr=getCurrCalendar();
        int month=curr.get(Calendar.MONTH);//mcl: should be month
        return month;//mcl: month filed start from 0
    }
    public static int getCurrDay(){
        Calendar curr=getCurrCalendar();
        int day=curr.get(Calendar.DAY_OF_MONTH);//mcl: should be day
        return day;
    }

    public static String showInterval(Calendar timeStart, Calendar timeEnd) {
        //todo:判断过了0点的时间计算；是否做成只选第二个时间？
        //todo:need to optimize the calculate,do it later
        int hours;
        int mins;
        String countTime;
        hours = timeEnd.get(Calendar.HOUR_OF_DAY) - timeStart.get(Calendar.HOUR_OF_DAY);
        mins = timeEnd.get(Calendar.MINUTE) - timeStart.get(Calendar.MINUTE);
        if (hours == 0) {
            if (mins >= 0) {
                countTime = String.format("%02d h %02d m", hours, mins);
            } else {
                countTime = String.format("%02d h %02d m", 23, mins + 60);
            }
        } else if (hours > 0) {
            if (mins >= 0) {
                countTime = String.format("%02d h %02d m", hours, mins);
            } else {
                countTime = String.format("%02d h %02d m", hours - 1, mins + 60);
            }
        } else {
            if (mins >= 0) {
                countTime = String.format("%02d h %02d m", hours + 24, mins);
            } else {
                countTime = String.format("%02d h %02d m", hours + 23, mins + 60);
            }
        }

        return countTime;

    }
}
