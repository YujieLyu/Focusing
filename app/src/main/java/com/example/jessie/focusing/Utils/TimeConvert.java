package com.example.jessie.focusing.Utils;

import java.util.Calendar;

/**
 * @author : Yujie Lyu
 * @date : 30-01-2019
 * @time : 15:14
 */
public class TimeConvert {
    private volatile static TimeConvert instance;
    private int hour;
    private int min;
    private long time;

    public static TimeConvert getInstance() {
        if (null == instance) {
            synchronized (TimeConvert.class) {
                if (null == instance) {
                    instance = new TimeConvert();
                }
            }
        }
        return instance;
    }

    public long convertTime(int hour,int min){
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,min);
        time=calendar.getTimeInMillis();
        return time;
    }


}
