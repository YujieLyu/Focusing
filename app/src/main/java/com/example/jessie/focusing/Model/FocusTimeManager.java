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

    @Deprecated
    public synchronized List<FocusTime> getTimeDate(Calendar before, Calendar today) {
        List<FocusTime> timeData = new ArrayList<>();
        List<FocusTime> focusTimesDB = LitePal.findAll(FocusTime.class);
//        List<FocusTime> focusTimeList=LitePal.where("date>=?",String.valueOf(before));
//        long timeInterval=today.getTimeInMillis()-before.getTimeInMillis();
        for (FocusTime focusTime : focusTimesDB) {
            int year = focusTime.getYear();
            int month = focusTime.getMonth();
            int day = focusTime.getDay();
            if (year == before.get(Calendar.YEAR) && before.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
                if (month == before.get(Calendar.MONTH) &&
                        before.get(Calendar.MONTH) == today.get(Calendar.MONTH)) {
                    if (day >= before.get(Calendar.DAY_OF_MONTH) && day <= today.get(Calendar.DAY_OF_MONTH)) {
                        timeData.add(focusTime);
                    }
                } else if (month == before.get(Calendar.MONTH)
                        && day >= before.get(Calendar.DAY_OF_MONTH)) {
                    timeData.add(focusTime);
                } else if (month == today.get(Calendar.MONTH) && day <= today.get(Calendar.DAY_OF_MONTH)) {
                    timeData.add(focusTime);
                }
            } else if (year == before.get(Calendar.YEAR) && month == before.get(Calendar.MONTH)
                    && day >= before.get(Calendar.DAY_OF_MONTH)) {
                timeData.add(focusTime);
            } else if (year == today.get(Calendar.YEAR) && month == today.get(Calendar.MONTH)
                    && day <= today.get(Calendar.DAY_OF_MONTH)) {
                timeData.add(focusTime);
            }

        }
        return timeData;
    }
}
