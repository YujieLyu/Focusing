package com.example.jessie.focusing_demo;

import android.content.Context;
import android.content.pm.PackageManager;

import org.litepal.crud.DataSupport;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author : Yujie Lyu
 * @date : 12-12-2018
 * @time : 18:13
 */
public class AppLockInfoManager {
    private PackageManager packageManager;
    private Context context;

    public AppLockInfoManager(Context context){
        this.context=context;
        packageManager=context.getPackageManager();
    }

////    查找所有
//    public synchronized List<AppLockInfo> getAllAppLockInfos(){
//        List<AppLockInfo> appLockInfos=DataSupport.findAll(AppLockInfo.class);
//        Collections.sort(appLockInfos,appLockInfoComparator);
//        return appLockInfos;
    }


//    private Comparator appLockInfoComparator=new Comparator() {
//
//        @Override
//        public int compare(Object a1, Object a2) {
//            AppLockInfo appLockInfo1= (AppLockInfo) a1;
//            AppLockInfo appLockInfo2= (AppLockInfo) a2;
//            if(appLockInfo1.isLocked()&&!appLockInfo2.isLocked()) return -1;
//            else return 0;
//        }
//    };

//}
