package com.example.jessie.focusing.Model;

import android.graphics.drawable.Drawable;

import org.litepal.LitePal;
import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.List;


/**
 * @author : Yujie Lyu
 * @date : 12-12-2018
 * @time : 18:17
 * The installed apps information
 */
public class AppInfo extends LitePalSupport implements Comparable<AppInfo> {

    @Column(unique = true)
    private int id;

    @Column(nullable = false)
    private String packageName;

    @Column
    private int profId;

    @Column(ignore = true)
    private Drawable appImg;

    @Column(ignore = true)
    private String appName;

    private boolean isLocked;

    public static List<AppInfo> findAllByPackageName(String packageName) {
        return LitePal.where("packagename = ?", packageName).find(AppInfo.class);
    }

    public static List<AppInfo> findAllByProfileId(int profId) {
        return LitePal.where("profid = ?", profId + "").find(AppInfo.class);
    }

    public static List<AppInfo> findAllLockApps(String packageName) {
        String where = String.format("packagename = \"%s\" AND islocked = 1", packageName);
        return LitePal.where(where).find(AppInfo.class);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AppInfo) {
            AppInfo appInfo = (AppInfo) obj;
            boolean equalName = packageName.equals(appInfo.packageName);
            boolean equalProfId = profId == appInfo.profId;
            return equalName && equalProfId;
        }
        return false;
    }

    public Drawable getAppImg() {
        return appImg;
    }

    public void setAppImg(Drawable appImg) {
        this.appImg = appImg;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProfId() {
        return profId;
    }

    public void setProfId(int profId) {
        this.profId = profId;
    }

    @Override
    public int compareTo(AppInfo o) {
        if (o == null) {
            return 1;
        }
        if (isLocked && !o.isLocked) {
            return -1;
        } else if (!isLocked && o.isLocked) {
            return 1;
        } else {
            return appName.compareTo(o.appName);
        }
    }

    public boolean saveOrUpdate() {
        return saveOrUpdate("id=? AND profid=?", id + "", profId + "");
    }
}
