package com.example.jessie.focusing.Model;

import android.icu.text.UnicodeSetSpanner;

import com.github.mikephil.charting.formatter.IFillFormatter;

import org.litepal.LitePal;
import org.litepal.annotation.Column;

import java.io.FileOutputStream;
import java.text.Format;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author : Yujie Lyu
 * @date : 03-02-2019
 * @time : 12:44
 */
public class FocusTimeManager {

    public synchronized void saveOrUpdateTime(Calendar today, long timeSummary) {
        FocusTime focusTimeLast = LitePal.findLast(FocusTime.class);
        int todayYear=today.get(Calendar.YEAR);
        int todayMonth= today.get(Calendar.MONTH);
        int todayDay=today.get(Calendar.DAY_OF_MONTH);
        if (focusTimeLast == null) {
           FocusTime focusTime= createFocusTime(timeSummary,todayYear,todayMonth,todayDay);
            focusTime.save();
        }
            if (focusTimeLast.getYear()==todayYear&&focusTimeLast.getMonth() == todayMonth
                    &&focusTimeLast.getDay()==todayDay) {
                long originTime=focusTimeLast.getTime();
                focusTimeLast.setTime(timeSummary + originTime);
                focusTimeLast.update(focusTimeLast.getId());
            } else {
                FocusTime focusTime= createFocusTime(timeSummary,todayYear,todayMonth,todayDay);
                focusTime.save();
            }

    }
    public synchronized void deleteItem(int id){
        LitePal.delete(FocusTime.class,id);
    }

    private FocusTime createFocusTime(long timeSummary,int year,int month,int day){
        FocusTime focusTime = new FocusTime();
        focusTime.setTime(timeSummary);
        focusTime.setYear(year);
        focusTime.setMonth(month);
        focusTime.setDay(day);
        return focusTime;
    }
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
