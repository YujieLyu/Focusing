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
        List<AppInfo> appInfosDb = LitePal.findAll(AppInfo.class);
        for (int i = 0; i < appInfos.size(); i++) {
            AppInfo info = appInfos.get(i);
            for (AppInfo infoDb : appInfosDb) {
                if (info.equals(infoDb)) {
                    info.setId(infoDb.getId());
                    info.setLocked(infoDb.isLocked());
                }
            }
        }
        return appInfos;
    }

    /**
     * 将手机应用信息插入数据库
     */
    public synchronized List<AppInfo> setDatabase(List<AppInfo> appInfos) {
//        LitePal.deleteAll(AppInfo.class);
        List<AppInfo> appInfosDatabase = LitePal.findAll(AppInfo.class);
        List<AppInfo> tempI = new ArrayList<>();
        List<AppInfo> tempD = new ArrayList<>();

        //todo:可删除
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

    public void saveInfos(List<AppInfo> appInfos) {
        for (AppInfo appInfo : appInfos) {
            appInfo.save();
        }
//        List<AppInfo> appInfosDatabase = LitePal.findAll(AppInfo.class);
//        for (AppInfo info : appInfos) {
//            for (AppInfo infoDB : appInfosDatabase) {
//                if (info.getPackageName().equals(infoDB.getPackageName())) {
//                    infoDB.setLocked(info.isLocked());
//                    infoDB.save();
//                }
//            }
////            info.save();
//        }
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

    public boolean checkIsLocked(String packageName) {
        AppInfo appInfo = LitePal.where("packageName=?", packageName).findFirst(AppInfo.class);
        if (appInfo == null) {
            return false;
        }
        return appInfo.isLocked();
    }

}


