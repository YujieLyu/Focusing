package com.example.jessie.focusing_demo;

import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

/**
 * @author : Yujie Lyu
 * @date : 12-12-2018
 * @time : 18:17
 * 表示获取到的应用
 */
//
class AppLockInfo {

    private Drawable appImg;
    private String appName;

    public AppLockInfo(Drawable appImg,String appName){
        this.appImg=appImg;
        this.appName=appName;
    }


    public AppLockInfo(){

    }

    public Drawable getAppImg(){
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


    //    private long id;
//    private String packageName;

//    private boolean isLocked;//是否已加锁
////    todo:推荐加锁的应用
//    private ApplicationInfo appInfo;
////    todo:区分系统应用和用户应用；
//    private String topTitle;
////    todo:private boolean isSetUnlocked;

//    public AppLockInfo(){
//
//    }
//
//    public AppLockInfo(String packageName,boolean isLocked){
//        this.packageName=packageName;
//        this.isLocked=isLocked;
//
//    }
//
//    protected AppLockInfo(Parcel in) {
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
//    public static final Creator<AppLockInfo> CREATOR = new Creator<AppLockInfo>() {
//        @Override
//        public AppLockInfo createFromParcel(Parcel in) {
//            return new AppLockInfo(in);
//        }
//
//        @Override
//        public AppLockInfo[] newArray(int size) {
//            return new AppLockInfo[size];
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
