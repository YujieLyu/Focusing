package com.example.jessie.focusing.Utils;

import java.util.Calendar;

/**
 * @author : Yujie Lyu
 */
public class CircleTimeUtils {
    public static final int FULL_ANGLE = 720;

    public static int toMillis(float angle) {
        return (int) ((angle / FULL_ANGLE) * (24 * 60 * 60 * 1000));
    }

    public static String toString(float angle) {
        Calendar calendar = toCalendar(angle);
        return TimeHelper.toString(calendar.getTime());
    }

    public static Calendar toCalendar(float angle) {
        int mins = toMillis(angle);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.add(Calendar.MINUTE, mins);
        return calendar;
    }

    public static String diffTime(float startDegree, float endDegree) {
        int start = toMillis(startDegree);
        int end = toMillis(endDegree);
        return null;
    }
}
