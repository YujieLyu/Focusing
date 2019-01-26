package com.example.jessie.focusing.Model;

import android.content.Context;
import android.content.pm.PackageManager;

import com.example.jessie.focusing.Model.AppInfo;

import org.litepal.LitePal;

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
    private Profile profile;//todo:等待在profileList转到ProfAppList传值

    public AppInfoManager(Context context) {
        this.context = context;
        packageManager = context.getPackageManager();
    }

    /**
     * Insert
     */
    public synchronized void insert(List<AppInfo> appInfos) {
        for (AppInfo appInfo : appInfos) {
            appInfo.save();

        }
    }

    /**
     * Delete
     */
    public synchronized void delete(List<AppInfo> appInfos) {
        for (AppInfo appInfo : appInfos) {
            appInfo.delete();
        }
    }

    public synchronized List<AppInfo> syncData(List<AppInfo> appInfos) {
        //for clear DB
//        LitePal.deleteAll(AppInfo.class);
        List<AppInfo> appInfosDB = LitePal.findAll(AppInfo.class);

        List<AppInfo> tempD = new ArrayList<>();

        for (AppInfo appinfodb : appInfosDB) {
            if (!appInfos.contains(appinfodb)) {
                tempD.add(appinfodb);
            }
        }
        delete(tempD);

        for (int i = 0; i < appInfos.size(); i++) {
            AppInfo info = appInfos.get(i);
            for (AppInfo infoDb : appInfosDB) {
                if (info.equals(infoDb)) {
                    info.setId(infoDb.getId());
                    //todo:设置Profid
                    info.setProfile(profile);
                    info.setLocked(infoDb.isLocked());
                }
            }
        }
        return appInfos;
    }


    public synchronized List<AppInfo> setDatabase(List<AppInfo> appInfos) {
//        LitePal.deleteAll(AppInfo.class);
        List<AppInfo> appInfosDatabase = LitePal.findAll(AppInfo.class);
        List<AppInfo> tempI = new ArrayList<>();
        List<AppInfo> tempD = new ArrayList<>();

        //
        for (AppInfo appInfo : appInfos) {
            if (!appInfosDatabase.contains(appInfo)) {
                tempI.add(appInfo);
            }
        }
        insert(tempI);

        for (AppInfo appInfoDB : appInfosDatabase) {
            if (!appInfos.contains(appInfoDB)) {
                tempD.add(appInfoDB);
            }
        }
        delete(tempD);

        for (AppInfo appInfo : appInfos) {
            for (AppInfo infoDB : appInfosDatabase) {
                if (appInfo.getPackageName().equals(infoDB.getPackageName())) {
                    appInfo.setLocked(infoDB.isLocked());
                }
            }

        }
        return appInfos;
    }

    /**
     * update app status settings
     * @param appInfos
     */
    public void updateInfos(List<AppInfo> appInfos) {
        for (AppInfo appInfo : appInfos) {
            //Need to use saveOrUpdate, or it will save all data repetitively
            appInfo.saveOrUpdate("id=?", String.valueOf(appInfo.getId()));
        }
    }

//    /**
//     * 更改数据库app状态为锁定
//     */
//    public void lockApp(String packageName) {
//        List<AppInfo> appInfos = LitePal.where("packageName = ?", packageName).find(AppInfo.class);
//        for (AppInfo info : appInfos) {
//            info.setLocked(true);
//            info.saveAsync();
//        }
//    }

    public boolean checkIsLocked(String packageName) {
        AppInfo appInfo = LitePal.where("packageName=?", packageName).findFirst(AppInfo.class);
        if (appInfo == null) {
            return false;
        }
        return appInfo.isLocked();
    }

}


