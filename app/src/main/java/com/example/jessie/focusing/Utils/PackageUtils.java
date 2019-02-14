package com.example.jessie.focusing.Utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.example.jessie.focusing.Model.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Yujie Lyu
 */
public class PackageUtils {
    private static final String TAG = PackageUtils.class.getSimpleName();
    private final PackageManager packageManager;
    private List<AppInfo> installedApps = null;

    public PackageUtils(PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    public String getAppName(String packageName) {
        try {
            PackageInfo info = packageManager.getPackageInfo(packageName, 0);
            return info.applicationInfo.loadLabel(packageManager).toString();
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public Drawable getAppIcon(String packageName) {
        try {
            PackageInfo info = packageManager.getPackageInfo(packageName, 0);
            return info.applicationInfo.loadIcon(packageManager);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public List<AppInfo> getInstalledApps() {
        if (installedApps == null) {
            scanAppsList();
        }
        return installedApps;
    }


    public void scanAppsList() {
        installedApps = new ArrayList<>();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        for (int i = 0; i < packageInfos.size(); i++) {
            PackageInfo packageInfo = packageInfos.get(i);
            //todo:保留一定的系统应用:电话闹钟一类的
            // filter system apps
            if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0
                    || packageInfo.packageName.equals(AppConstants.APP_PACKAGE_NAME)) {
                continue;
            }
            AppInfo appInfo = new AppInfo();
            appInfo.setAppName(packageInfo.applicationInfo.loadLabel(packageManager).toString());
            appInfo.setAppImg(packageInfo.applicationInfo.loadIcon(packageManager));
            appInfo.setPackageName(packageInfo.applicationInfo.packageName);
            installedApps.add(appInfo);
        }
    }
}
