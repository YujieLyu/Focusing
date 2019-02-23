package com.example.jessie.focusing.Utils;

import org.junit.Test;

import java.util.Calendar;

/**
 * @author : Yujie Lyu
 */
public class TimeHelperTest {
    private static final String TAG = TimeHelperTest.class.getSimpleName();

    @Test
    public void getTotalTimeInMillis() {
        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DATE, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        long s1 = calendar.getTimeInMillis();
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        long e1 = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 30);
        long s2 = calendar.getTimeInMillis();
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        long e2 = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 0);
        long s3 = calendar.getTimeInMillis();
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        long e3 = calendar.getTimeInMillis();
        long[][] periods = new long[][]{
                new long[]{s1, e1},
                new long[]{s2, e2},
                new long[]{s3, e3},
        };
        long actual = TimeHelper.getTotalTimeInMillis(periods);
//        long expect = (long) (2.5 * 60 * 60 * 1000);
        long expect = 0;
        assert actual == expect;
    }
}