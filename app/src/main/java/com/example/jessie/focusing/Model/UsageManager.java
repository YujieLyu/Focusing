package com.example.jessie.focusing.Model;

import android.content.Context;

import com.example.jessie.focusing.Utils.TimeHelper;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Yujie Lyu
 * @date : 06-02-2019
 * @time : 00:26
 */
public class UsageManager {
    private static final int OPEN_TIMES_INCREMENT = 1;
    private Context context;
    private int todayYear = TimeHelper.getCurrYear();
    private int todayMonth = TimeHelper.getCurrMonth();
    private int todayDay = TimeHelper.getCurrDay();

    public UsageManager(Context context) {
        this.context = context;
    }

    private boolean atToday(AppUsage appUsage) {
        return appUsage.getYear() == todayYear && appUsage.getMonth() == todayMonth && appUsage.getDay() == todayDay;
    }

    public synchronized void saveOrUpdateData(long timeSummary, String packageName,boolean isLocked) {
        if (packageName == null || packageName.isEmpty()) {
            return;
        }
        List<AppUsage> appUsagesDB = LitePal.findAll(AppUsage.class);
        AppUsage appChecked = null;
        for (AppUsage appUsage : appUsagesDB) {
            if (appUsage.getPackageName().equals(packageName) && atToday(appUsage)&&appUsage.isLocked()==isLocked) {
                appChecked = appUsage;
            }
        }
        if (appChecked != null) {
            appChecked.addOpenTimes(OPEN_TIMES_INCREMENT);
            appChecked.addUsedTime(timeSummary);
        } else {
            appChecked = createAppUsage(todayYear, todayMonth, todayDay, packageName);
            appChecked.setOpenOutFocus(OPEN_TIMES_INCREMENT);
            appChecked.setUsedOutFocus(timeSummary);
        }
        appChecked.save();
    }

    public static void deleteAll(int day) {
        LitePal.deleteAll(AppUsage.class, "day=?", String.valueOf(day));
    }

    /**
     * 如果数据库中没有符合条件的数据，新建一条
     *
     * @param year
     * @param month
     * @param day
     * @param packageName
     * @return
     */
    private AppUsage createAppUsage(int year, int month, int day, String packageName) {
        AppUsage appUsage = new AppUsage();
        appUsage.setYear(year);
        appUsage.setMonth(month);
        appUsage.setDay(day);
        appUsage.setPackageName(packageName);
        return appUsage;
    }

    /**
     * Delete
     */
    public synchronized void delete(List<AppUsage> appUsages) {
        for (AppUsage appUsage : appUsages) {
            appUsage.delete();
        }
    }

    /**
     * 同步系统安装APP和数据库存储，增删和同步,用于更新数据库中存储的APP是否被卸载或添加
     *
     * @param appInfos
     * @return
     */
    public synchronized List<AppUsage> syncData(List<AppInfo> appInfos) {
        List<AppUsage> appUsageDB = LitePal.findAll(AppUsage.class);
        List<String> packageName = new ArrayList<>();
        List<AppUsage> temp = new ArrayList<>();

        for (AppInfo appInfo : appInfos) {
            packageName.add(appInfo.getPackageName());
        }

        for (AppUsage appUsage : appUsageDB) {
            if (!packageName.contains(appUsage.getPackageName())) {
//                temp.add(appUsage);
                appUsage.delete();
            }
        }
//        delete(temp);

        return appUsageDB;


    }


    public synchronized List<AppUsage> getData(String packageName) {
        List<AppUsage> synonymUsageInfos = new ArrayList<>();
        List<AppUsage> usageInfosDatabase = LitePal.findAll(AppUsage.class);
        for (AppUsage appUsage : usageInfosDatabase) {
            if (appUsage.getPackageName().equals(packageName)) {
                synonymUsageInfos.add(appUsage);
            }

        }
        return synonymUsageInfos;
    }

}
