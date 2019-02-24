package com.example.jessie.focusing.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author : Yujie Lyu
 * @date : 02-02-2019
 * @time : 11:47
 */
public class TimeHelper {
    public static final long HOUR_IN_MILLIS = 60 * 60 * 1000;
    public static final long DAY_IN_MILLIS = 24 * HOUR_IN_MILLIS;
    private static final String DATE_PATTERN = "HH:mm";
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_PATTERN, Locale.getDefault());

    /**
     * Convert specific {@link Date} to {@link String}
     * Time pattern: HH:mm
     *
     * @param date
     * @return
     */
    public static String toString(Date date) {
        return dateFormat.format(date);
    }

    public static String toString(long millis) {
        return toString(new Date(millis));
    }

    public static String toString(long millis, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern, Locale.getDefault());
        return df.format(new Date(millis));
    }

    /**
     * Convert Date String to {@link Date}
     *
     * @param dateStr pattern: HH:mm
     * @return
     * @throws ParseException
     */
    public static Date toDate(String dateStr) throws ParseException {
        return dateFormat.parse(dateStr);
    }

    /**
     * Check if the specific time is between the start and end time.
     *
     * @param start
     * @param end
     * @param time
     * @return
     */
    public static boolean betweenRange(long start, long end, long time) {
        return time - start >= 0 && end - time > 0;
    }

    public static long toMillis(int hour, int min) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        return calendar.getTimeInMillis();
    }

    public static int getCurrYear() {
        Calendar curr = Calendar.getInstance();
        return curr.get(Calendar.YEAR);
    }

    public static int getCurrMonth() {
        Calendar curr = Calendar.getInstance();
        return curr.get(Calendar.MONTH);
    }

    public static int getCurrDay() {
        Calendar curr = Calendar.getInstance();
        return curr.get(Calendar.DAY_OF_MONTH);
    }

    @Deprecated
    public static String showInterval(Calendar timeStart, Calendar timeEnd) {
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

    /**
     * Get the date displayed in "day of month" format
     *
     * @param numOfDay 0 for today, 1 for yesterday
     * @return
     */
    public static String getDayOfMonth(int numOfDay) {
        long millis = System.currentTimeMillis() + -1 * numOfDay * DAY_IN_MILLIS;
        Date date = new Date(millis);
        return new SimpleDateFormat("dd", Locale.getDefault()).format(date);
    }

    /**
     * Get the date displayed in "day of week" format
     *
     * @param numOfDay 0 for today, 1 for yesterday
     * @return
     */
    public static String getDayOfWeek(int numOfDay) {
        long millis = System.currentTimeMillis() + -1 * numOfDay * DAY_IN_MILLIS;
        Date date = new Date(millis);
        return new SimpleDateFormat("EEE", Locale.getDefault()).format(date);
    }

    /**
     * Put the year/month/day of the specific day into an array
     *
     * @param numOfDay 0 for today, 1 for yesterday
     * @return
     */
    public static int[] getYearMonthDay(int numOfDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1 * numOfDay);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return new int[]{year, month, day};
    }

    /**
     * For convenience use of {@link #getTotalTimeInMillis(List)}
     *
     * @param periods
     * @return
     * @see #getTotalTimeInMillis(List)
     */
    public static long getTotalTimeInMillis(Long[]... periods) {
        return getTotalTimeInMillis(Arrays.asList(periods));
    }

    /**
     * Returns the total time from multiple periods.
     * <p>
     * the basic idea is: <br>
     * total time = latest end time - earliest start time - idle time.
     * </p>
     * <p>
     * the periods are allowed to overlap.
     * </p>
     *
     * @param periods the list of period pairs, each pair: [startTime, endTime]
     * @return the total time in millis.
     */
    public static long getTotalTimeInMillis(List<Long[]> periods) {
        if (periods == null || periods.isEmpty()) {
            return 0;
        }
        // sort the list by "start time"
        periods.sort((o1, o2) -> {
            if (o1.length < 1 || o2.length < 1) {
                return -1;
            }
            return Long.compare(o1[0], o2[0]);
        });
        long start = periods.get(0)[0];
        long end = periods.get(0)[0];
        long idle = 0;
        for (Long[] period : periods) {
            if (period.length != 2 || period[1] < period[0]) {
                throw new IllegalArgumentException("Invalid periods");
            }
            long next_start = period[0];
            long next_end = period[1];
            long diff = next_start - end;
            idle += diff < 0 ? 0 : diff;
            end = Math.max(end, next_end);
        }
        // reassign "end" if end is earlier than present.
        end = Math.min(System.currentTimeMillis(), end);
        long totalTime = end - start - idle;
        return totalTime < 0 ? 0 : totalTime;
    }
}
