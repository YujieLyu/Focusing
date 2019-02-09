package com.example.jessie.focusing.Model;

import org.litepal.LitePal;
import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import static com.example.jessie.focusing.Utils.TimeHelper.getCurrDay;
import static com.example.jessie.focusing.Utils.TimeHelper.getCurrMonth;
import static com.example.jessie.focusing.Utils.TimeHelper.getCurrYear;

/**
 * @author : Yujie Lyu
 * @date : 03-02-2019
 * @time : 12:38
 */
public class FocusTime extends LitePalSupport {
    @Column
    private int id;
    @Column
    private long time;
    @Column
    private int year;
    @Column
    private int month;
    @Column
    private int day;

    public FocusTime(long time, int year, int month, int day) {
        this.time = time;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public static FocusTime findInToday() {
        return findByDate(getCurrYear(), getCurrMonth(), getCurrDay());
    }

    public static FocusTime findByDate(int year, int month, int day) {
        String where = String.format("year = %s and month = %s and day = %s", year, month, day);
        return LitePal.where(where).findLast(FocusTime.class);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void addTime(long time) {
        this.time += time;
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
}
