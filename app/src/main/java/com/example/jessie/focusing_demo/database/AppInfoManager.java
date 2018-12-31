package com.example.jessie.focusing_demo.database;

import android.content.Context;
import android.content.pm.PackageManager;

import com.example.jessie.focusing_demo.model.AppInfo;

import org.litepal.LitePal;
import org.litepal.exceptions.DataSupportException;

import java.util.ArrayList;
import java.util.List;


/**
 * @author : Yujie Lyu
 * @date : 12-12-2018
 * @time : 18:13
 */
public class AppInfoManager {
    private PackageManager packageManager;
    private Context context;

    public AppInfoManager(Context context) {
        this.context = context;
        packageManager = context.getPackageManager();
    }

    /**
     * 将手机应用信息插入数据库
     */
    public synchronized List<AppInfo> insertAppLockInfo(List<AppInfo> appInfos) {
        //        List<AppInfo> appInfosToShow=new ArrayList<>();
        List<AppInfo> appInfosDatabase=LitePal.findAll(AppInfo.class);
//        for (AppInfo infoDB : appInfosDatabase) {
//            if(appInfos.contains(infoDB)){
//                appInfosToShow.add(infoDB);
//            }else {
//                LitePal.delete(AppInfo.class,infoDB.getId());
//            }
//
//        }

        //todo:可删除
        for (AppInfo appInfo : appInfos) {
            if (!appInfosDatabase.contains(appInfo)) {
                appInfosDatabase.add(appInfo);
                appInfo.save();
            }
        }

        for (AppInfo appInfoDB : appInfosDatabase) {
            if (!appInfos.contains(appInfoDB)){
                LitePal.delete(AppInfo.class,appInfoDB.getId());
            }

        }
        for (AppInfo appInfo : appInfos) {
            for (AppInfo infoDB : appInfosDatabase) {
                if (appInfo.getPackageName().equals(infoDB.getPackageName())){
                    appInfo.setId(infoDB.getId());
                    appInfo.setLocked(infoDB.isLocked());
                }
            }

        }
        return appInfos;
    }

    public void saveInfos(List<AppInfo> appInfos) {
        for (AppInfo info : appInfos) {
            info.save();
        }
    }

    /**
     * 更改数据库app状态为锁定
     */
    public void lockApp(String packageName) {
        List<AppInfo> appInfos = LitePal.where("packageName = ?", packageName).find(AppInfo.class);
        for (AppInfo info : appInfos) {
            info.setLocked(true);
            info.saveAsync();
        }
    }

    public boolean checkIsLocked(String packageName){
        AppInfo appInfo=LitePal.where("packageName=?",packageName).findFirst(AppInfo.class);
        if(appInfo==null){
            return false;
        }
        return appInfo.isLocked();
    }

}


