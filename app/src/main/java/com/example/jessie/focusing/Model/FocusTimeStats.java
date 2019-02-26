package com.example.jessie.focusing.Model;

import android.util.Log;

import com.example.jessie.focusing.Utils.TimeHelper;

import org.litepal.LitePal;
import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.List;

import static com.example.jessie.focusing.Utils.TimeHelper.getCurrDay;
import static com.example.jessie.focusing.Utils.TimeHelper.getCurrMonth;
import static com.example.jessie.focusing.Utils.TimeHelper.getCurrYear;

/**
 * @author : Yujie Lyu
 * @date : 03-02-2019
 * @time : 12:38
 */
public class FocusTimeStats extends LitePalSupport {
    private static String TAG = FocusTimeStats.class.getSimpleName();
    @Column
    private int id;
    @Column
    private long startTime;
    @Column
    private long endTime;
    @Column
    private int year;
    @Column
    private int month;
    @Column
    private int day;

    public FocusTimeStats(long startTime, long endTime, int year, int month, int day) {
        Log.i(TAG, String.format(
                "Focus during: %s ~ %s",
                TimeHelper.toString(startTime),
                TimeHelper.toString(endTime))
        );
        if (startTime < 0 || endTime < startTime) {
            throw new IllegalArgumentException("Invalid start/end time");
        }
        this.startTime = startTime;
        this.endTime = endTime;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public FocusTimeStats(long startTime, long endTime) {
        this(startTime, endTime, getCurrYear(), getCurrMonth(), getCurrDay());
    }

    public static List<FocusTimeStats> findInToday() {
        return findByDate(getCurrYear(), getCurrMonth(), getCurrDay());
    }

    public static List<FocusTimeStats> findByDate(int year, int month, int day) {
        String where = String.format("year = %s and month = %s and day = %s", year, month, day);
        return LitePal.where(where).find(FocusTimeStats.class);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void addTime(long time) {
        this.startTime += time;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
