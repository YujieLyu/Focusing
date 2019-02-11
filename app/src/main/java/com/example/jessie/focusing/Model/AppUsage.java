package com.example.jessie.focusing.Model;

import android.graphics.drawable.Drawable;

import org.litepal.LitePal;
import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Calendar;

/**
 * @author : Yujie Lyu
 * @date : 06-02-2019
 * @time : 00:04
 */
public class AppUsage extends LitePalSupport implements Comparable<AppUsage> {

    @Column(unique = true)
    private int id;
    @Column(nullable = false)
    private String packageName;
    @Column
    private long usedOutFocus;
    @Column
    private long usedInFocus;
    @Column
    private int openOutFocus;
    @Column
    private int openInFocus;
    @Column
    private int year;
    @Column
    private int month;
    @Column
    private int day;
    @Column(ignore = true)
    private Drawable appImg;
    @Column(ignore = true)
    private String appName;

    public AppUsage(String packageName, int year, int month, int day) {
        this.packageName = packageName;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public static AppUsage findByDate(String packageName, int year, int month, int day) {
        String where = String.format("packagename = \"%s\" and year = %s and month = %s and day = %s", packageName, year, month, day);
        return LitePal.where(where).findLast(AppUsage.class);
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Drawable getAppImg() {
        return appImg;
    }

    public void setAppImg(Drawable appImg) {
        this.appImg = appImg;
    }

    public long getUsedOutFocus() {
        return usedOutFocus;
    }

    public void setUsedTime(long usedTime, boolean isLocked) {
        if (isLocked) {
            this.usedInFocus = usedTime;
        } else {
            this.usedOutFocus = usedTime;
        }
    }

    public void addUsedTime(long usedTime, boolean isLocked) {
        if (isLocked) {
            this.usedInFocus += usedTime;
        } else {
            this.usedOutFocus += usedTime;
        }
    }

    @Override
    public int compareTo(AppUsage o) {
        if (usedOutFocus == o.usedOutFocus) {
            return 0;
        } else
            return usedOutFocus > o.usedOutFocus ? -1 : 1;
    }

    public String getPackageName() {
        return packageName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOpenOutFocus() {
        return openOutFocus;
    }

    public void setOpenTimes(int openTimes, boolean isLocked) {
        if (isLocked) {
            this.openInFocus = openTimes;
        } else {
            this.openOutFocus = openTimes;
        }
    }

    public void addOpenTimes(int openTimes, boolean isLocked) {
        if (isLocked) {
            this.openInFocus += openTimes;
        } else {
            this.openOutFocus += openTimes;
        }
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public long getUsedInFocus() {
        return usedInFocus;
    }

    public int getOpenInFocus() {
        return openInFocus;
    }

}
