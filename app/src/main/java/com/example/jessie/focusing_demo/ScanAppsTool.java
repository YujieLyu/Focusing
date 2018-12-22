package com.example.jessie.focusing_demo;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Yujie Lyu
 * @date : 21-12-2018
 * @time : 16:58
 */
public class ScanAppsTool {

    static final String TAG="ScanAppsTool";


    public static List<AppLockInfo> scanAppsList(PackageManager packageManager) {

        List<AppLockInfo> mAppInfos = new ArrayList<AppLockInfo>();
        try {
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo packageInfo = packageInfos.get(i);
                //todo:保留一定的系统应用
                // 过滤掉系统app

            if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
                continue;
            }

                AppLockInfo appLockInfo = new AppLockInfo();
                appLockInfo.setAppName(packageInfo.applicationInfo.loadLabel(packageManager).toString());
                appLockInfo.setAppImg(packageInfo.applicationInfo.loadIcon(packageManager));
                mAppInfos.add(appLockInfo);

            }

        } catch (Exception e) {
            Log.e(TAG, "====================获取应用包信息失败");
        }
        return mAppInfos;
    }

}
