package com.example.jessie.focusing.Service;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.example.jessie.focusing.Model.AppInfo;
import com.example.jessie.focusing.Model.Profile;
import com.example.jessie.focusing.Model.ProfileManager;
import com.example.jessie.focusing.Model.UsageManager;
import com.example.jessie.focusing.Utils.AppConstants;
import com.example.jessie.focusing.Model.AppInfoManager;
import com.example.jessie.focusing.Utils.TimeHelper;
import com.example.jessie.focusing.View.CountDown.Countdown_Activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.jessie.focusing.Utils.AppConstants.ONE_DAY;

/**
 * @author : Yujie Lyu
 * @date : 12-12-2018
 * @time : 23:42
 */
public class LockService extends IntentService implements DialogInterface.OnClickListener {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    private ActivityManager activityManager;
    private AppInfoManager appInfoManager;
    private ProfileManager profileManager;
    private UsageManager usageManager;
    private long endTime;
    private long startTime;
    public static boolean StartNow = false;

    @Override
    public void onCreate() {
        super.onCreate();
        appInfoManager = new AppInfoManager(this);
        profileManager = new ProfileManager(this);
        usageManager = new UsageManager(this);
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

    }

    public LockService() {
        super("LockService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        while (true) {

            checkData(intent);
            try {
                Thread.sleep(100);//todo:时间设置更改.考虑冲突，延迟等问题
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private String appOnTop = null;

    /**
     * 此处存在一个判断是否有profile开启，先执行Prof还是先执行custom
     *
     * @param intent
     */
    private void checkData(Intent intent) {
        startTime = intent.getLongExtra("startTime", 0);
        endTime = intent.getLongExtra("endTime", 0);//todo:start now countdown开始后 过来的lock请求
        long currTime = System.currentTimeMillis();


//        获取栈顶app的包名
        String packageName = getLauncherTopApp(LockService.this, activityManager);

        if (!packageName.equals("com.example.jessie.focusing") && !packageName.equals("com.google.android.apps.nexuslauncher")) {
//            usageManager.deleteAll(9);
            if (!packageName.equals(appOnTop)) {
                appOnTop = packageName;
                boolean isLocked=appInfoManager.checkIsLocked(packageName);
                usageManager.saveOrUpdateData(0, packageName,isLocked);
            }
            //todo:待优化！！！
            List<AppInfo> appInfos = appInfoManager.getData(packageName);
            List<AppInfo> temp = new ArrayList<>();
            for (AppInfo appInfo : appInfos) {
                if (appInfo.isLocked()) {
                    temp.add(appInfo);
                }
            }

            Calendar calendar = Calendar.getInstance();
            int today = calendar.get(Calendar.DAY_OF_WEEK);
            for (AppInfo appInfo : temp) {
                int profId = appInfo.getProfId();
                if (profId == -10) {
                    if (LockService.StartNow) {
                        lockScreen(packageName, endTime);
                    }
                    return;
                } else {
                    Profile p = profileManager.getProfile(profId);
                    boolean onSchedule = profileManager.checkProfOnSchedule(p, today);
                    long profStart = TimeHelper.convertTime(p.getStartHour(), p.getStartMin());
                    long profEnd = TimeHelper.convertTime(p.getEndHour(), p.getEndMin());
                    boolean inTimeSlot = compareTime(profStart, profEnd, currTime);
                    if (onSchedule) {
                        if (inTimeSlot) {
                            lockScreen(packageName, profEnd);
                            return;
                        }
                    }
                }


            }
        }
    }

    private boolean compareTime(long start, long end, long curr) {
//        if (end < curr) {
//            end = end + ONE_DAY;
//        }
        return curr - start >= 0 && end - curr > 0;
    }


    public String getLauncherTopApp(Context context, ActivityManager activityManager) {

        //5.0以后需要用这方法
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        long endTime = System.currentTimeMillis();
        long beginTime = endTime - 10000;
        String result = "";
        UsageEvents.Event event = new UsageEvents.Event();
        UsageEvents usageEvents = usageStatsManager.queryEvents(beginTime, endTime);
        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event);
            if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                result = event.getPackageName();
            }
        }
        if (!android.text.TextUtils.isEmpty(result)) {
            return result;
        }

        return "";
    }

    private void lockScreen(String packageName, long endTime) {

        Intent intent = new Intent(this, Countdown_Activity.class);
//        intent.putExtra(AppConstants.PRESS_BACK, AppConstants.BACK_TO_FINISH);
        intent.putExtra(AppConstants.LOCK_PACKAGE_NAME, packageName);
//        intent.putExtra("startTime", start);
        intent.putExtra("endTime", endTime);//todo:类似的数据传递的要写成常量吧
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//todo:不懂这个操作
        startActivity(intent);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}
