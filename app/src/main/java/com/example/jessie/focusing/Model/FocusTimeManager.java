package com.example.jessie.focusing.Model;

import com.example.jessie.focusing.Utils.TimeHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.jessie.focusing.Utils.TimeHelper.DAY_IN_MILLIS;
import static com.example.jessie.focusing.Utils.TimeHelper.toMillis;

/**
 * @author : Yujie Lyu
 * @date : 03-02-2019
 * @time : 12:44
 */
public class FocusTimeManager {


    /**
     * Returns the used time of the specific day,
     * e.g., 0 for today, 1 for yesterday...
     *
     * @param numOfDay
     * @return
     */
    public List<FocusTimeStats> getTimeData(int numOfDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1 * numOfDay);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return FocusTimeStats.findByDate(year, month, day);
    }

    public long getTotalFocusTime(int numOfDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1 * numOfDay);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        List<Profile> profiles = Profile.findAllOnSchedule(dayOfWeek);
        List<FocusTimeStats> startNows = getTimeData(numOfDay);
        List<Long[]> periods = new ArrayList<>();
        // TODO: fix profile time stats
        for (Profile profile : profiles) {
            int startHour = profile.getStartHour();
            int startMin = profile.getStartMin();
            long start = toMillis(startHour, startMin);
            int endHour = profile.getEndHour();
            int endMin = profile.getEndMin();
            long end = toMillis(endHour, endMin);
            if (end < start) {
                end += DAY_IN_MILLIS;
            }
            periods.add(new Long[]{start, end});
        }
        for (FocusTimeStats stats : startNows) {
            periods.add(new Long[]{stats.getStartTime(), stats.getEndTime()});
        }
        return TimeHelper.getTotalTimeInMillis(periods);
    }
}
