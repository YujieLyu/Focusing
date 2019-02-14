package com.example.jessie.focusing.Service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.jessie.focusing.Model.AppInfo;
import com.example.jessie.focusing.Model.AppInfoManager;
import com.example.jessie.focusing.Model.UsageManager;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Utils.AppConstants;
import com.example.jessie.focusing.View.CountDown.CountdownActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;

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
    public static final String INTERVAL = "interval";
    private static final String TAG = LockService.class.getSimpleName();
    public static long START_NOW_END_TIME = -1;
    private final int SERVICE_ID;
    private String PACKAGE_NAME;
    private AppInfoManager appInfoManager;
    private UsageManager usageManager;
    private String appOnTop = null;
    private long appStartTime = 0;
    private boolean isLocked = false;
    private int interval = -1;

    public LockService() {
        super("LockService");
        SERVICE_ID = "LockService".hashCode();
    }

    public static void start(Context context, int interval) {
        Intent intent = new Intent(context, LockService.class);
        intent.putExtra(INTERVAL, interval);
        start(context, intent);
    }

    public static void start(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent intent = new Intent(this, RebootBroadcastReceiver.class);
        intent.setAction(RebootBroadcastReceiver.REBOOT_ACTION);
        sendBroadcast(intent);
        Log.i(TAG, "on Task Removed...");
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appInfoManager = new AppInfoManager();
        usageManager = new UsageManager(this);
        setIntentRedelivery(true);
        PACKAGE_NAME = getPackageName();
        Log.i(TAG, "onCreate");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        Log.i(TAG, "on Start...");
        if (intent != null) {
            interval = intent.getIntExtra(INTERVAL, -1);
        }
        Log.i(TAG, "Interval is: " + interval);
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        Intent intent = new Intent(this, RebootBroadcastReceiver.class);
        intent.setAction(RebootBroadcastReceiver.REBOOT_ACTION);
        sendBroadcast(intent);
        Log.i(TAG, "on Destroy...");
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        interval = 100;
        Log.i(TAG, "Interval is: " + interval);
        while (interval > 0) {
//            interval = intent.getIntExtra(INTERVAL, -1);
            checkData();
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = PACKAGE_NAME;
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, TAG, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_SECRET);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setSmallIcon(R.drawable.ic_stat_name)
                .build();
        startForeground(SERVICE_ID, notification);
    }

    /**
     * 此处存在一个判断是否有profile开启，先执行Prof还是先执行custom
     */
    private void checkData() {
        String packageName = getLauncherTopApp(LockService.this);
        if (LAUNCHER_PACKAGE_NAME.equals(packageName)) {
            recordUsedTime();
        }
        if (!inWhiteList(packageName)) {
            List<AppInfo> toLockApps = appInfoManager.getToLockApps(packageName);
            boolean toLock = !toLockApps.isEmpty();
            recordAppUsage(packageName, toLock);
            if (toLock) {
                long endTime = appInfoManager.getLatestEndTime(toLockApps);
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

        Intent intent = new Intent(this, CountdownActivity.class);
//        intent.putExtra(AppConstants.PRESS_BACK, AppConstants.BACK_TO_FINISH);
        intent.putExtra(AppConstants.LOCK_PACKAGE_NAME, packageName);
//        intent.putExtra(START_TIME, start);
        intent.putExtra(END_TIME, endTime);//todo:类似的数据传递的要写成常量吧
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//todo:不懂这个操作
        startActivity(intent);
    }
}
