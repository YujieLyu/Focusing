package com.example.jessie.focusing.Model;

import android.graphics.drawable.Drawable;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;


/**
 * @author : Yujie Lyu
 * @date : 12-12-2018
 * @time : 18:17
 * 表示获取到的应用
 */
//
public class AppInfo extends LitePalSupport {

    @Column(unique = true)
    private long id;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AppInfo) {
            AppInfo appInfo = (AppInfo) obj;
            return packageName.equals(appInfo.getPackageName());
        }
        return false;
    }

    @Column(nullable = false)
    private String packageName;

    @Column(defaultValue = "null")
    private String profile;

    private Drawable appImg;


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

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    //


////    todo:推荐加锁的应用
//    private ApplicationInfo appInfo;
////    todo:区分系统应用和用户应用；
//    private String topTitle;
////    todo:private boolean isSetUnlocked;

//    public AppInfo(){
//
//    }
//
//    public AppInfo(String packageName,boolean isLocked){
//        this.packageName=packageName;
//        this.isLocked=isLocked;
//
//    }
//
//    protected AppInfo(Parcel in) {
//        this.id=in.readLong();
//        this.packageName=in.readString();
//        this.appName=in.readString();
//        this.isLocked=in.readInt()!=0;
//        this.appInfo=in.readParcelable(ApplicationInfo.class.getClassLoader());
//        this.topTitle=in.readString();
//
//
//    }
//
//    public static final Creator<AppInfo> CREATOR = new Creator<AppInfo>() {
//        @Override
//        public AppInfo createFromParcel(Parcel in) {
//            return new AppInfo(in);
//        }
//
//        @Override
//        public AppInfo[] newArray(int size) {
//            return new AppInfo[size];
//        }
//    };
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeLong(this.id);
//        dest.writeString(this.packageName);
//        dest.writeString(this.appName);
//        dest.writeInt((this.isLocked?1:0));
//        dest.writeParcelable(this.appInfo,flags);
//        dest.writeString(this.topTitle);
//
//    }
//    public boolean isLocked() {
//        return isLocked;
//    }


}
