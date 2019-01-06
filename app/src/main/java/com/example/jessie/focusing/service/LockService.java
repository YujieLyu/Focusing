package com.example.jessie.focusing.service;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.example.jessie.focusing.AppConstants;
import com.example.jessie.focusing.database.AppInfoManager;
import com.example.jessie.focusing.view.LockScreenActivity;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void onCreate() {
        super.onCreate();
        appInfoManager = new AppInfoManager(this);
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

    }

    public LockService() {
        super("LockService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        while (true){
            checkData();
            try {
                Thread.sleep(1000);//todo:时间设置更改
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void checkData() {

        //获取栈顶app的包名
        String packageName = getLauncherTopApp(LockService.this, activityManager);
        boolean lockStatus = appInfoManager.checkIsLocked(packageName);

        //判断包名打开解锁页面
        if (lockStatus) {
            lockScreen(packageName);
//            AlertDialog.Builder builder = new AlertDialog.Builder(this)
//                    .setMessage("过会再来")
//                    .setPositiveButton("好的", this);
//            builder.create().show();
        }
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

    private void lockScreen(String packageName) {
//        LockApplication.getInstance().clearAllActivity();
        Intent intent = new Intent(this, LockScreenActivity.class);
        intent.putExtra(AppConstants.PRESS_BACK,AppConstants.BACK_TO_FINISH);
        intent.putExtra(AppConstants.LOCK_PACKAGE_NAME, packageName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//todo:不懂这个操作
        startActivity(intent);
    }

    /**
     * 白名单
     */
    private boolean inWhiteList(String packageName) {
        return packageName.equals(AppConstants.APP_PACKAGE_NAME)
                || packageName.equals("com.android.settings")
                || packageName.equals("com.google.android.apps.nexuslauncher");
    }

    /**
     * 获得属于桌面的应用的应用包名称
     */
    private List<String> getHomes() {
        List<String> names = new ArrayList<>();
        PackageManager packageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo ri : resolveInfo) {
            names.add(ri.activityInfo.packageName);
        }
        return names;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}