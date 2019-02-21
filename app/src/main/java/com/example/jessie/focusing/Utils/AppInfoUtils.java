package com.example.jessie.focusing.Utils;

import android.content.Context;
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
public class AppInfoUtils {
    private static final String TAG = AppInfoUtils.class.getSimpleName();
    private final Context context;
    private final PackageManager packageManager;
    private List<AppInfo> installedApps = null;

    public AppInfoUtils(Context context) {
        this.context = context;
        this.packageManager = context.getPackageManager();
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
            // filter system apps
            if ((ApplicationInfo.FLAG_SYSTEM & packageInfo.applicationInfo.flags) != 0
                    || packageInfo.packageName.equals(context.getPackageName())) {
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
