package com.example.jessie.focusing.Model;

import android.graphics.drawable.Drawable;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

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
    private boolean isLocked;
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


//    public AppUsage(Context context, String packageName) {
//        this.packageName = packageName;
//        PackageManager packageManager = context.getPackageManager();
//        try {
//            PackageInfo info = packageManager.getPackageInfo(packageName, 0);
//            appName = info.applicationInfo.loadLabel(packageManager).toString();
//            appImg = info.applicationInfo.loadIcon(packageManager);
//            usedOutFocus = 0;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

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

    public void setUsedOutFocus(long usedOutFocus) {
        this.usedOutFocus = usedOutFocus;
    }

    public void addUsedTime(long usedTime) {
        this.usedOutFocus += usedTime;
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

    public void setOpenOutFocus(int openOutFocus) {
        this.openOutFocus = openOutFocus;
    }

    public void addOpenTimes(int openTimes) {
        this.openOutFocus += openTimes;
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

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public long getUsedInFocus() {
        return usedInFocus;
    }

    public void setUsedInFocus(long usedInFocus) {
        this.usedInFocus = usedInFocus;
    }

    public int getOpenInFocus() {
        return openInFocus;
    }

    public void setOpenInFocus(int openInFocus) {
        this.openInFocus = openInFocus;
    }
}
