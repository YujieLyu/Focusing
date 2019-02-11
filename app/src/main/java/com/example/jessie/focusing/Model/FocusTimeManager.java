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

    public synchronized List<FocusTime> getTimeData(Calendar before, Calendar today) {
        List<FocusTime> res = new ArrayList<>();
        List<FocusTime> ftDb = LitePal.findAll(FocusTime.class);
        for (FocusTime time : ftDb) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, time.getYear());
            calendar.set(Calendar.MONTH, time.getMonth());
            calendar.set(Calendar.DAY_OF_MONTH, time.getDay());
            if (calendar.after(before) && calendar.before(today)) {
                res.add(time);
            }
        }
        return res;
    }

    public FocusTime getTimeData(int numOfDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1 * numOfDay);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return FocusTime.findByDate(year, month, day);
    }
}
