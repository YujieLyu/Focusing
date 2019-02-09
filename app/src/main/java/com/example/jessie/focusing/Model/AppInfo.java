package com.example.jessie.focusing.Model;

import android.graphics.drawable.Drawable;

import org.litepal.LitePal;
import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;
import org.litepal.exceptions.LitePalSupportException;

import java.util.Comparator;


/**
 * @author : Yujie Lyu
 * @date : 12-12-2018
 * @time : 18:17
 * 表示获取到的应用
 */
//
public class AppInfo extends LitePalSupport {

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AppInfo) {
            AppInfo appInfo = (AppInfo) obj;
            boolean equalName = packageName.equals(appInfo.packageName);
            boolean equalProfId = (profId == appInfo.getProfId());
            return equalName && equalProfId;
//            if (profile==null) {
//                return equalName && appInfo.getProfile()==null;
//            }
//            return equalName && profile.equals(appInfo.getProfile());
        }
        return false;
    }

    @Column(unique = true)
    private int id;

    @Column(nullable = false)
    private String packageName;

    @Column
    private Profile profile;

    @Column
    private int profId;


    @Column(ignore = true)
    private Drawable appImg;

    @Column(ignore = true)
    private String appName;

    private boolean isLocked;//是否已加锁


    public AppInfo(Drawable appImg, String appName, Boolean isLocked) {
        this.appImg = appImg;
        this.appName = appName;
        this.isLocked = isLocked;
    }


    public AppInfo() {

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



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(int profileId) {
        Profile profile = LitePal.find(Profile.class, profileId);
        this.profile = profile;
    }

    public int getProfId() {
        return profId;
    }

    public void setProfId(int profId) {
        if (profId != -10) {
            this.profId = profId;
        } else {
            this.profId = -10;
        }

    }

    public static Comparator<AppInfo> nameComparator = new Comparator<AppInfo>() {

        @Override
        public int compare(AppInfo app1, AppInfo app2) {
            String app1First = app1.getAppName();
            String app2First = app2.getAppName();
            boolean a1 = app1.isLocked();
            boolean a2 = app2.isLocked();

            if (a1 && !a2) {
                return -1;
            } else if (!a1 && a2) {
                return 1;
            } else {

                return (app1First.compareTo(app2First));
            }
        }
    };


    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
