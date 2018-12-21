package com.example.jessie.focusing_demo;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.IntentService;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;

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

    private boolean lockState;
    private ActivityManager activityManager;

    public LockService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) { checkData(); }
    private void checkData() {

        //获取栈顶app的包名
        String packageName = getLauncherTopApp(LockService.this, activityManager);

//        //判断包名打开解锁页面
//        if (lockState && !inWhiteList(packageName) && !TextUtils.isEmpty(packageName)) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this)
//                    .setMessage("过会再来")
//                    .setPositiveButton("好的", this);
//            builder.create().show();
//            if (!ignore) {
//                continue;
//            }
//        }
    }
    public String getLauncherTopApp(Context context, ActivityManager activityManager) {

            //5.0以后需要用这方法
            UsageStatsManager sUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            long endTime = System.currentTimeMillis();
            long beginTime = endTime - 10000;
            String result = "";
            UsageEvents.Event event = new UsageEvents.Event();
            UsageEvents usageEvents = sUsageStatsManager.queryEvents(beginTime, endTime);
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

    /**
     * 白名单
     */
    private boolean inWhiteList(String packageName) {
        return packageName.equals(AppConstants.APP_PACKAGE_NAME)
                || packageName.equals("com.android.settings")
                ||packageName.equals("com.google.android.apps.nexuslauncher");
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
