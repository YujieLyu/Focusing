package com.example.jessie.focusing_demo.utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.example.jessie.focusing_demo.model.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Yujie Lyu
 * @date : 21-12-2018
 * @time : 16:58
 */
public class ScanAppsTool {

    static final String TAG="ScanAppsTool";


    public static List<AppInfo> scanAppsList(PackageManager packageManager) {

        List<AppInfo> appInfos = new ArrayList<AppInfo>();
        try {
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo packageInfo = packageInfos.get(i);
                //todo:保留一定的系统应用:电话闹钟一类的
                // 过滤掉系统app

            if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0) {
                continue;
            }
//            if (!packageInfo.packageName.equals(AppConstants.APP_PACKAGE_NAME) && !packageInfo.packageName.equals("com.android.settings")
//                        && !packageInfo.packageName.equals("com.google.android.googlequicksearchbox"))) {
//                    continue;
//            }

                AppInfo appInfo = new AppInfo();
                appInfo.setAppName(packageInfo.applicationInfo.loadLabel(packageManager).toString());
                appInfo.setAppImg(packageInfo.applicationInfo.loadIcon(packageManager));
                appInfo.setPackageName(packageInfo.applicationInfo.packageName);
                appInfos.add(appInfo);

            }

        } catch (Exception e) {
            Log.e(TAG, "====================获取应用包信息失败");
        }
        return appInfos;
    }

}
