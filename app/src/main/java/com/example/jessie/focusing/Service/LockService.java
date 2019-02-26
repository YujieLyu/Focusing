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
import com.example.jessie.focusing.Model.FocusTimeStats;
import com.example.jessie.focusing.Model.Profile;
import com.example.jessie.focusing.Model.UsageManager;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Utils.AppConstants;
import com.example.jessie.focusing.Utils.TimeHelper;
import com.example.jessie.focusing.View.Countdown.CountdownActivity;
import com.example.jessie.focusing.View.Finish.FinishActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public static final String TO_UPDATE = "to_update";
    public static final int DEF_INTERVAL = 100; //ms
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
    private Set<Profile> startedProfiles;
    private boolean toUpdateProfiles = false;

    public LockService() {
        super("LockService");
        SERVICE_ID = "LockService".hashCode();
    }

    public static void start(Context context, int interval) {
        Intent intent = new Intent(context, LockService.class);
        intent.putExtra(INTERVAL, interval);
        start(context, intent);
    }

    public static void updateProfiles(Context context, boolean toUpdate) {
        Intent intent = new Intent(context, LockService.class);
        intent.putExtra(INTERVAL, DEF_INTERVAL);
        intent.putExtra(TO_UPDATE, toUpdate);
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
        Log.i(TAG, "Stop start now.");
        AppInfoManager.reset(Profile.START_NOW_PROFILE_ID);
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
        updateProfiles();
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
            toUpdateProfiles = intent.getBooleanExtra(TO_UPDATE, false);
        }
        Log.i(TAG, "Interval is: " + interval);
        Log.i(TAG, "Start Time is: " +
                (startNowStartTime < 0 ? startNowStartTime : TimeHelper.toString(startNowStartTime)));
        Log.i(TAG, "End Time is: " +
                (startNowEndTime < 0 ? startNowEndTime : TimeHelper.toString(startNowEndTime)));
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "on Destroy...");
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "onHandleIntent, Interval is: " + interval);
        while (interval > 0) {
            checkToLock();
            if (toUpdateProfiles) {
                updateProfiles();
                toUpdateProfiles = false;
            }
            checkIfFinished();
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateProfiles() {
        startedProfiles = Profile.findAllStarted();
        Log.i(TAG, "Started Profiles updated - size:" + startedProfiles.size());
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
    private void checkToLock() {
        String packageName = getLauncherTopApp(LockService.this);
        if (LAUNCHER_PACKAGE_NAME.equals(packageName)) {
            recordScreenTime();
        }
        if (!inWhiteList(packageName)) {
            List<AppInfo> toLockApps = appInfoManager.getToLockApps(packageName, startedProfiles);
            boolean toLock = !toLockApps.isEmpty();
            if (toLock) {
                boolean startNow = toLockApps
                        .stream()
                        .anyMatch(a -> a.getProfId() == Profile.START_NOW_PROFILE_ID);
                long endTime;
                boolean isStartNow;
                if (startNow && startNowEndTime >= System.currentTimeMillis()) {
                    endTime = startNowEndTime;
                    isStartNow = true;
                } else {
                    endTime = appInfoManager.getLatestEndTime(toLockApps, startedProfiles);
                    isStartNow = false;
                }
                if (endTime > System.currentTimeMillis()) {
                    lockApp(packageName, endTime, isStartNow);
                } else toLock = false;
            }
            recordOpenTimes(packageName, toLock);
        }
    }

    /**
     * Checks if should transfer to {@link FinishActivity}
     */
    private void checkIfFinished() {
        long now = System.currentTimeMillis();
        Collection<Profile> finishedProfs = startedProfiles.stream()
                .filter(profile -> profile.getEndTime() <= now).collect(Collectors.toSet());
        // refresh the profile set
        startedProfiles.removeIf(profile -> profile.getEndTime() <= now);
        long focusedTime = 0;
        for (Profile prof : finishedProfs) {
            focusedTime += prof.getDuration();
        }
        if (toResetStartNow()) {
            focusedTime += Math.max(startNowEndTime - startNowStartTime, 0);
            resetStartNow();
        }
        if (focusedTime > 0) {
            toFinishActivity(focusedTime);
        }
    }

    /**
     * Checks if to reset start now setting
     *
     * @return
     */
    private boolean toResetStartNow() {
        return startNowEndTime > -1 && startNowEndTime <= System.currentTimeMillis();
    }

    private void resetStartNow() {
        // NOTE: update endTime if earlier than Now.
        // i.e., start_now had finished.
        FocusTimeStats focusTimeStats = new FocusTimeStats(startNowStartTime, startNowEndTime);
        focusTimeStats.save();
        startNowStartTime = -1;
        startNowEndTime = -1;// reset start now end time
        AppInfoManager.reset(Profile.START_NOW_PROFILE_ID);
        Log.i(TAG, "Reset start now time");
    }

    /**
     * Lock specific app until end time reached
     *
     * @param packageName the app to lock.
     * @param endTime     the end time of locking.
     * @param isStartNow  specify if the app is in "Start Now" profile.
     */
    private void lockApp(String packageName, long endTime, boolean isStartNow) {
        Log.i(TAG, "Lock app: " + packageName);
        Log.i(TAG, "End Time:" + TimeHelper.toString(endTime));
        if (endTime < System.currentTimeMillis()) {
            Log.e(TAG, "End Time has been reached:" + TimeHelper.toString(endTime));
            return;
        }
        Intent intent = new Intent(this, CountdownActivity.class);
//        intent.putExtra(AppConstants.PRESS_BACK, AppConstants.BACK_TO_FINISH);
        intent.putExtra(AppConstants.LOCK_PACKAGE_NAME, packageName);
        intent.putExtra(CountdownActivity.IS_START_NOW, isStartNow);
        if (isStartNow) {
            intent.putExtra(START_TIME, startNowStartTime);
        }
        intent.putExtra(END_TIME, endTime);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * Transfers to {@link FinishActivity}
     *
     * @param focusedTime the total time displayed on screen
     */
    private void toFinishActivity(long focusedTime) {
        Intent intent = new Intent(this, FinishActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(FinishActivity.FOCUSED_TIME, focusedTime);
        startActivity(intent);
    }

    /**
     * record the used time of app to database
     */
    private void recordScreenTime() {
        if (!isEmpty(appOnTop) && appStartTime != 0) {
            usageManager.saveUsedTime(appOnTop, System.currentTimeMillis() - appStartTime, this.isLocked);
            appOnTop = null;
        }
    }

    /**
     * record the open times of app to database
     *
     * @param packageName
     * @param toLock
     */
    private void recordOpenTimes(String packageName, boolean toLock) {
        if (!packageName.equals(appOnTop)) {
            usageManager.saveOpenTimes(packageName, toLock);
            appOnTop = packageName;
            appStartTime = System.currentTimeMillis();
            this.isLocked = toLock;
        }
    }

    private void createNotificationManager() {
        String NOTIFICATION_CHANNEL_ID = PACKAGE_NAME;
        NotificationChannel chan = new NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                TAG,
                NotificationManager.IMPORTANCE_MIN);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_SECRET);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private Notification notifyFinish(String title, String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, PACKAGE_NAME);
        builder.setOngoing(false)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setSmallIcon(R.drawable.ic_stat_name);
        if (!isEmpty(content)) {
            builder.setContentText(content);
        }
        return builder.build();
    }

    private Notification createPermanentNotification(String content) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, PACKAGE_NAME);
        builder.setOngoing(true)
                .setContentTitle("Focusing is running")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setShowWhen(false);
        if (!isEmpty(content)) {
            builder.setContentText(content);
        }
        return builder.build();
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        createNotificationManager();
        Notification notification = createPermanentNotification(null);
        startForeground(SERVICE_ID, notification);
    }

    /**
     * Check if the app is in white list.
     * If so, would not lock or record it.
     *
     * @param packageName
     * @return
     */
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
        long beginTime = (endTime - 1000);
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
}
