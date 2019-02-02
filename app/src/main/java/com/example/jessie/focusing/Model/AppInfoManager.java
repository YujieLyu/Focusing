package com.example.jessie.focusing.Model;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.example.jessie.focusing.Model.AppInfo;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    /**
     * delete by profId

     * @return
     */

    public synchronized void deleteByProfId(int profId){
        int rows = LitePal.deleteAll(AppInfo.class, "profid=?", String.valueOf(profId));
        Log.i("AppInfoManager", "Rows effected: " + rows);
//        List<AppInfo> appInfosDatabase = LitePal.findAll(AppInfo.class);
//        List<AppInfo> temp = new ArrayList<>();
//        for (AppInfo appInfo : appInfosDatabase) {
//            if (appInfo.getProfId()==profId){
//                temp.add(appInfo);
//            }
//
//        }
//        delete(temp);
    }
    public synchronized List<AppInfo> syncData(List<AppInfo> appInfos) {
        //for clear DB
//        LitePal.deleteAll(AppInfo.class);
        List<AppInfo> appInfosDatabase = LitePal.findAll(AppInfo.class);
        List<String> packageName = new ArrayList<>();
        List<AppInfo> tempD = new ArrayList<>();

        for (AppInfo appInfo : appInfos) {
            packageName.add(appInfo.getPackageName());
        }

        for (AppInfo appInfodb : appInfosDatabase) {
            if (!packageName.contains(appInfodb.getPackageName())) {
                tempD.add(appInfodb);
            }

        }
        delete(tempD);


        for (int i = 0; i < appInfos.size(); i++) {
            AppInfo info = appInfos.get(i);
            for (AppInfo infoDb : appInfosDatabase) {
                if (info.equals(infoDb)) {
                    info.setId(infoDb.getId());
                    //todo:设置Profid
                    info.setLocked(infoDb.isLocked());
                }
            }
        }
//按字母排序
        Collections.sort(appInfos, AppInfo.nameComparator);
        return appInfos;

    }



    public synchronized List<AppInfo> getData(String packageName) {
        List<AppInfo> synonymAppInfos = new ArrayList<>();
        List<AppInfo> appInfosDatabase = LitePal.findAll(AppInfo.class);
        for (AppInfo appInfo : appInfosDatabase) {
            if (appInfo.getPackageName().equals(packageName)) {
                synonymAppInfos.add(appInfo);
            }

        }
        return synonymAppInfos;
    }


    public synchronized List<AppInfo> setDatabase(List<AppInfo> appInfos) {
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
     *
     * @param appInfos
     */
    public void updateInfos(List<AppInfo> appInfos) {


        for (AppInfo appInfo : appInfos) {
            //Need to use saveOrUpdate, or it will save all data repetitively

            appInfo.saveOrUpdate("id=? AND profid=?", String.valueOf(appInfo.getId()), String.valueOf(appInfo.getProfId()));

        }
    }

    public void reset(int profId){
        ContentValues cv=new ContentValues();
        cv.put("islocked",0);
        LitePal.updateAll(AppInfo.class,cv,"profid=?",String.valueOf(profId));
    }

    public boolean checkIsLocked(int id) {
        AppInfo appInfo = LitePal.where("id=?", String.valueOf(id)).findFirst(AppInfo.class);
        if (appInfo == null) {
            return false;
        }
        return appInfo.isLocked();
    }

}


