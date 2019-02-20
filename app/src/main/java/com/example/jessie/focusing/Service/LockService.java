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
import com.example.jessie.focusing.Utils.TimeHelper;
import com.example.jessie.focusing.View.Countdown.CountdownActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;

import static android.text.TextUtils.isEmpty;
import static com.example.jessie.focusing.Utils.AppConstants.END_TIME;
import static com.example.jessie.focusing.Utils.AppConstants.LAUNCHER_PACKAGE_NAME;
import static com.example.jessie.focusing.Utils.AppConstants.START_TIME;

/**
 * @author : Yujie Lyu
 * @date : 12-12-2018
 * @time : 23:42
 */
public class LockService extends IntentService {

    public static final String INTERVAL = "interval";
    public static final int DEF_INTERVAL = 100;//ms
    private static final String TAG = LockService.class.getSimpleName();
    private final int SERVICE_ID;
    private String PACKAGE_NAME;
    private AppInfoManager appInfoManager;
    private UsageManager usageManager;
    private String appOnTop = null;
    private long appStartTime = 0;
    private boolean isLocked = false;
    private int interval = -1;
    private long startNowStartTime = -1, startNowEndTime = -1;

    public LockService() {
        super("LockService");
        SERVICE_ID = "LockService".hashCode();
    }

    public static void start(Context context, int interval) {
        Intent intent = new Intent(context, LockService.class);
        intent.putExtra(INTERVAL, interval);
        start(context, intent);
    }

    /**
     * Used for Start Now
     *
     * @param context
     * @param startTime the start time of start now
     * @param endTime   the end time of start now
     */
    public static void startNow(Context context, long startTime, long endTime) {
        Intent intent = new Intent(context, LockService.class);
        intent.putExtra(INTERVAL, DEF_INTERVAL);
        intent.putExtra(START_TIME, startTime);
        intent.putExtra(END_TIME, endTime);
        start(context, intent);
    }

    public static void start(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    public static void stopStartNow(Context context) {
        startNow(context, -1, -1);
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
        Log.i(TAG, "on Start, id: " + startId);
        if (intent != null) {
            // Keep the original value if did not specified.
            interval = intent.getIntExtra(INTERVAL, interval);
            startNowStartTime = intent.getLongExtra(START_TIME, startNowStartTime);
            startNowEndTime = intent.getLongExtra(END_TIME, startNowEndTime);
        }
        Log.i(TAG, "Interval is: " + interval);
        Log.i(TAG, "Start Time is: " + TimeHelper.toString(startNowStartTime));
        Log.i(TAG, "End Time is: " + TimeHelper.toString(startNowEndTime));
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
        Log.i(TAG, "onHandleIntent, Interval is: " + interval);
        while (interval > 0) {
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
     * This method is used to check whether the current operating app needs to be locked
     * or not by checking database through its packageName.
     * <p>
     * Focusing's package name, launcher, and the settings package name  are stored
     * in the whitelist
     * <p>
     * It also used to record app's screen time and open times when the app on top changes.
     */
    private void checkData() {
        String packageName = getLauncherTopApp(LockService.this);
        if (LAUNCHER_PACKAGE_NAME.equals(packageName)) {
            recordScreenTime();
        }
        if (!inWhiteList(packageName)) {
            List<AppInfo> toLockApps = appInfoManager.getToLockApps(packageName);
            boolean toLock = !toLockApps.isEmpty();
            recordOpenTimes(packageName, toLock);
            if (toLock) {
                // NOTE: update endTime if earlier than Now.
                // i.e., start_now did not boot or had finished.
                long endTime = startNowEndTime;
                if (endTime < System.currentTimeMillis()) {
                    startNowEndTime = -1;// reset start now end time
                    endTime = appInfoManager.getLatestEndTime(toLockApps);
                }
                lockScreen(packageName, endTime);
            }
        }
    }

    /**
     * Save the used time of app to database
     */
    private void recordScreenTime() {
        if (!isEmpty(appOnTop) && appStartTime != 0) {
            usageManager.saveUsedTime(appOnTop, System.currentTimeMillis() - appStartTime, this.isLocked);
            appOnTop = null;
        }
    }

    /**
     * Save the open times of app to database
     *
     * @param packageName
     * @param toLock
     */
    private void recordOpenTimes(String packageName, boolean toLock) {
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
                // NOTE: to add new package name
        ));
        return whiteList.contains(packageName);
    }

    /**
     * This method is used to fetch the package name of current operating app
     *
     * @param context
     * @return
     */
    public String getLauncherTopApp(Context context) {

        UsageStatsManager usageStatsManager =
                (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        long endTime = System.currentTimeMillis();
        long beginTime = (long) (endTime - interval * 1.2);
        String result = "";
        UsageEvents.Event event = new UsageEvents.Event();
        UsageEvents usageEvents = usageStatsManager.queryEvents(beginTime, endTime);
        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event);
            if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                result = event.getPackageName();
            }
        }
        return result;
    }

    private void lockScreen(String packageName, long endTime) {

        Intent intent = new Intent(this, CountdownActivity.class);
//        intent.putExtra(AppConstants.PRESS_BACK, AppConstants.BACK_TO_FINISH);
        intent.putExtra(AppConstants.LOCK_PACKAGE_NAME, packageName);
//        intent.putExtra(START_TIME, start);
        intent.putExtra(END_TIME, endTime);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
