package com.example.jessie.focusing.Model;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.jessie.focusing.Utils.TimeHelper;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static android.content.Context.USAGE_STATS_SERVICE;
import static com.example.jessie.focusing.Utils.TimeHelper.getCurrDay;
import static com.example.jessie.focusing.Utils.TimeHelper.getCurrMonth;
import static com.example.jessie.focusing.Utils.TimeHelper.getCurrYear;
import static com.example.jessie.focusing.Utils.TimeHelper.getMidnightTime;

/**
 * @author : Yujie Lyu
 * @date : 06-02-2019
 * @time : 00:26
 */
public class UsageManager {
    private static final int OPEN_TIMES_INCREMENT = 1;
    private static final String TAG = UsageManager.class.getSimpleName();
    private final Context context;
    private final PackageManager packageManager;
    private int todayYear = getCurrYear();
    private int todayMonth = getCurrMonth();
    private int todayDay = getCurrDay();

    public UsageManager(Context context) {
        this.context = context;
        this.packageManager = context.getPackageManager();
    }

    public static void deleteAll(int day) {
        LitePal.deleteAll(AppUsage.class, "day=?", String.valueOf(day));
    }

    /**
     * Save open times for specific app
     *
     * @param packageName
     * @param isLocked
     */
    public synchronized void saveOpenTimes(String packageName, boolean isLocked) {
        if (TextUtils.isEmpty(packageName)) {
            return;
        }
        String msg = String.format("Save Open times for %s, is locked: %s", packageName, isLocked);
        Log.i(TAG, msg);
        AppUsage appChecked = AppUsage.findAppAtToday(packageName);
        if (appChecked != null) {
            appChecked.addOpenTimes(OPEN_TIMES_INCREMENT, isLocked);
        } else {
            appChecked = new AppUsage(packageName, todayYear, todayMonth, todayDay);
            appChecked.setOpenTimes(OPEN_TIMES_INCREMENT, isLocked);
        }
        appChecked.save();
    }

    /**
     * Save used time for specific app
     *
     * @param packageName
     * @param usedTime
     * @param isLocked
     */
    public synchronized void saveUsedTime(String packageName, long usedTime, boolean isLocked) {
        if (TextUtils.isEmpty(packageName)) {
            return;
        }
        String msg = String.format(
                Locale.getDefault(),
                "Save Used time for: %s, is locked: %s, used time:%.2f",
                packageName, isLocked, usedTime / 1000 / 60D
        );
        Log.i(TAG, msg);
        AppUsage appChecked = AppUsage.findAppAtToday(packageName);
        if (appChecked != null) {
            appChecked.setUsedTime(usedTime, isLocked);
        } else {
            appChecked = new AppUsage(packageName, todayYear, todayMonth, todayDay);
            appChecked.setUsedTime(usedTime, isLocked);
        }
        appChecked.save();
    }

    /**
     * Fetch the most used apps in specific days.
     *
     * @param days days before today.
     * @return
     */
    public synchronized List<AppUsage> getMostUsedAppsInDays(int days) {
        List<AppUsage> appUsages = LitePal.findAll(AppUsage.class);
        Log.i(TAG, "Find all app usages from database: " + appUsages.size());
        Calendar start = Calendar.getInstance();
        start.add(Calendar.DATE, days);
        Calendar end = Calendar.getInstance();
        end.add(Calendar.DATE, 1);
        List<AppUsage> res = new ArrayList<>();
        Set<String> pkNames = new HashSet<>();
        for (AppUsage appUsage : appUsages) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, appUsage.getYear());
            calendar.set(Calendar.MONTH, appUsage.getMonth());
            calendar.set(Calendar.DAY_OF_MONTH, appUsage.getDay());
            boolean after = calendar.after(start);
            boolean before = calendar.before(end);
            if (after && before) {
                try {
                    PackageInfo info = packageManager.getPackageInfo(appUsage.getPackageName(), 0);
                    appUsage.setAppName(info.applicationInfo.loadLabel(packageManager).toString());
                    appUsage.setAppImg(info.applicationInfo.loadIcon(packageManager));
                } catch (PackageManager.NameNotFoundException e) {
                    continue;
                }
                if (pkNames.add(appUsage.getPackageName())) {
                    res.add(appUsage);
                }
            }
        }
        Log.i(TAG, "Get most used apps size: " + res.size());
        res.sort(AppUsage::compareTo);
        return res;
    }

    /**
     * Fetch app usage info in specific day
     *
     * @param packageName
     * @param numOfDay    the day before today, 0 for today, 1 for yesterday...
     * @return
     */
    public synchronized AppUsage getAppUsageOfDay(String packageName, int numOfDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1 * numOfDay);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return AppUsage.findByDate(packageName, year, month, day);
    }

    private List<UsageStats> getUsageStats(int numOfDay) {
        long[] timePair = getMidnightTime(numOfDay);
        long startTime = timePair[0];
        long endTime = timePair[1];
        String msg = String.format("Fetch usage stats from %s to %s",
                TimeHelper.toString(startTime, "MM/dd HH:mm:ss"),
                TimeHelper.toString(endTime, "MM/dd HH:mm:ss")
        );
        Log.i(TAG, msg);
        UsageStatsManager manager = (UsageStatsManager) context.getSystemService(USAGE_STATS_SERVICE);
        List<UsageStats> stats = manager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);
//        List<UsageStats> res = new ArrayList<>();
//        for (UsageStats stat : stats) {
//            long begin = stat.getFirstTimeStamp();
//            long end = stat.getLastTimeStamp();
//            if (begin > startTime && end < endTime) {
//                res.add(stat);
//            }
//        } // NOTE: Android builtin bug
        return stats;
    }

    public long getUsedTime(String packageName, int numOfDay) {
        List<UsageStats> statsMap = getUsageStats(numOfDay);
        long usedTime = 0;
        for (UsageStats stat : statsMap) {
            if (stat.getPackageName().equals(packageName)) {
                String startTime = TimeHelper.toString(stat.getFirstTimeStamp(), "MM/dd HH:mm:ss");
                String endTime = TimeHelper.toString(stat.getLastTimeStamp(), "MM/dd HH:mm:ss");
                Log.i(TAG, String.format("Fetched usage stat: %s ~ %s, on numOfDay = %s",
                        startTime, endTime, numOfDay));
                usedTime += stat.getTotalTimeInForeground();
            }
        }
        return usedTime;
    }
}
