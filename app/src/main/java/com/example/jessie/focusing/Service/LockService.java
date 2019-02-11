package com.example.jessie.focusing.Service;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.jessie.focusing.Model.AppInfo;
import com.example.jessie.focusing.Model.AppInfoManager;
import com.example.jessie.focusing.Model.UsageManager;
import com.example.jessie.focusing.Utils.AppConstants;
import com.example.jessie.focusing.View.CountDown.Countdown_Activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.text.TextUtils.isEmpty;
import static com.example.jessie.focusing.Utils.AppConstants.LAUNCHER_PACKAGE_NAME;

/**
 * @author : Yujie Lyu
 * @date : 12-12-2018
 * @time : 23:42
 */
public class LockService extends IntentService {
    public static final String START_TIME = "start_time";
    public static final String END_TIME = "end_time";
    private static final String TAG = LockService.class.getSimpleName();
    public static long START_NOW_END_TIME = -1;
    private String PACKAGE_NAME;
    private AppInfoManager appInfoManager;
    private UsageManager usageManager;
    private String appOnTop = null;
    private long appStartTime = 0;
    private boolean isLocked = false;


    public LockService() {
        super("LockService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appInfoManager = new AppInfoManager();
        usageManager = new UsageManager(this);
        /**
         * Creates an IntentService.  Invoked by your subclass's constructor.
         *
         * @param name Used to name the worker thread, important only for debugging.
         */
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        PACKAGE_NAME = getPackageName();
        Log.i(TAG, "this package name is: " + PACKAGE_NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        long startTime = intent.getLongExtra(START_TIME, 0);
//        if (endTime <= 0) {
//            Log.e(TAG, endTime + "");
//        }
        while (true) {

            checkData(intent);
            try {
                Thread.sleep(100);//todo:时间设置更改.考虑冲突，延迟等问题
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 此处存在一个判断是否有profile开启，先执行Prof还是先执行custom
     */
    private void checkData(Intent intent) {
//        endTime = intent.getLongExtra(END_TIME, 0);
//        if (endTime <= 0) {
//            Log.e(TAG, endTime + "");
//        }
        String packageName = getLauncherTopApp(LockService.this);
        if (LAUNCHER_PACKAGE_NAME.equals(packageName)) {
            recordUsedTime();
        }
        if (!inWhiteList(packageName)) {
            List<AppInfo> toLockApps = appInfoManager.getToLockApps(packageName);
            boolean toLock = !toLockApps.isEmpty();
            recordAppUsage(packageName, toLock);
            if (toLock) {
                long endTime = appInfoManager.getLongestEndTime(toLockApps);
//                if (!LockService.StartNow) {
//                    endTime = appInfoManager.getLongestEndTime(toLockApps);
//                }

                lockScreen(packageName, endTime);
            }
        }
    }

    private void recordUsedTime() {
        if (!isEmpty(appOnTop) && appStartTime != 0) {
            usageManager.saveUsedTime(appOnTop, System.currentTimeMillis() - appStartTime, this.isLocked);
            appOnTop = null;
        }
    }

    private void recordAppUsage(String packageName, boolean toLock) {
        if (!packageName.equals(appOnTop)) {
            appOnTop = packageName;
            appStartTime = System.currentTimeMillis();
            this.isLocked = toLock;
            usageManager.saveOpenTimes(packageName, toLock);
        }
    }

    private boolean inWhiteList(String packageName) {
        List<String> whiteList = new ArrayList<>(Arrays.asList(
                PACKAGE_NAME,
                LAUNCHER_PACKAGE_NAME,
                "com.android.settings"
                //TODO: to add new package name
        ));
        return whiteList.contains(packageName);
    }


    public String getLauncherTopApp(Context context) {

        //5.0以后需要用这方法
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        long endTime = System.currentTimeMillis();
        long beginTime = endTime - 10000;
        String result = null;
        UsageEvents.Event event = new UsageEvents.Event();
        UsageEvents usageEvents = usageStatsManager.queryEvents(beginTime, endTime);
        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event);
            if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                result = event.getPackageName();
            }
        }
        if (!isEmpty(result)) {
            return result;
        }

        return "";
    }

    private void lockScreen(String packageName, long endTime) {

        Intent intent = new Intent(this, Countdown_Activity.class);
//        intent.putExtra(AppConstants.PRESS_BACK, AppConstants.BACK_TO_FINISH);
        intent.putExtra(AppConstants.LOCK_PACKAGE_NAME, packageName);
//        intent.putExtra(START_TIME, start);
        intent.putExtra(END_TIME, endTime);//todo:类似的数据传递的要写成常量吧
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//todo:不懂这个操作
        startActivity(intent);
    }
}
