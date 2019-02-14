package com.example.jessie.focusing.Model;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.jessie.focusing.Utils.TimeHelper.getCurrDay;
import static com.example.jessie.focusing.Utils.TimeHelper.getCurrMonth;
import static com.example.jessie.focusing.Utils.TimeHelper.getCurrYear;

/**
 * @author : Yujie Lyu
 * @date : 03-02-2019
 * @time : 12:44
 */
public class FocusTimeManager {

    public synchronized void saveOrUpdateTime(long timeSummary) {
        int todayYear = getCurrYear();
        int todayMonth = getCurrMonth();
        int todayDay = getCurrDay();
        FocusTime ftInToday = FocusTime.findByDate(todayYear, todayMonth, todayDay);
        if (ftInToday != null) {
            ftInToday.addTime(timeSummary);
        } else {
            ftInToday = new FocusTime(timeSummary, todayYear, todayMonth, todayDay);
        }
        ftInToday.save();
    }

    /**
     * Fetch all time data between the specific time range
     *
     * @param start
     * @param end
     * @return
     */
    public synchronized List<FocusTime> getTimeData(Calendar start, Calendar end) {
        List<FocusTime> res = new ArrayList<>();
        List<FocusTime> ftDb = LitePal.findAll(FocusTime.class);
        for (FocusTime time : ftDb) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, time.getYear());
            calendar.set(Calendar.MONTH, time.getMonth());
            calendar.set(Calendar.DAY_OF_MONTH, time.getDay());
            if (calendar.after(start) && calendar.before(end)) {
                res.add(time);
            }
        }
        return res;
    }

    /**
     * Returns the used time of the specific day,
     * e.g., 0 for today, 1 for yesterday...
     *
     * @param numOfDay
     * @return
     */
    public FocusTime getTimeData(int numOfDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1 * numOfDay);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return FocusTime.findByDate(year, month, day);
    }
}
