package com.example.jessie.focusing.Service;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.jessie.focusing.Model.FocusTimeStats;
import com.example.jessie.focusing.Model.Profile;

import java.util.Calendar;
import java.util.List;

import static android.content.Intent.ACTION_BOOT_COMPLETED;
import static android.content.Intent.ACTION_SCREEN_OFF;
import static android.content.Intent.ACTION_SCREEN_ON;
import static com.example.jessie.focusing.Utils.TimeHelper.DAY_IN_MILLIS;

/**
 * @author : Yujie Lyu
 */
public class RebootBroadcastReceiver extends BroadcastReceiver {
    public static final String REBOOT_ACTION = "ACTION_TO_REBOOT";
    private static final String TAG = RebootBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "action is: " + intent.getAction());
        String action = intent.getAction();
        if (action == null) {
            return;
        }
        switch (action) {
            case ACTION_BOOT_COMPLETED:
            case REBOOT_ACTION:
            case ACTION_SCREEN_ON:
                LockService.start(context, 100);
                break;
            case Intent.ACTION_TIME_TICK:
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int min = calendar.get(Calendar.MINUTE);
                if (hour == 0 && min == 0) {
                    calendar.add(Calendar.DATE, -1);
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                    List<Profile> profiles = Profile.findAllOnSchedule(dayOfWeek);
                    for (Profile profile : profiles) {
                        long start = profile.getStartTime();
                        long end = profile.getEndTime();
                        if (end < start) {
                            end += DAY_IN_MILLIS;
                        }
                        FocusTimeStats stats = new FocusTimeStats(start, end, year, month, day);
                        stats.save();
                    }
                    Log.i(TAG, "Save Profile to FocusTimeStats");
                }
                if (!isMyServiceRunning(LockService.class, context)) {
                    LockService.start(context, 100);
                }
                break;
            case ACTION_SCREEN_OFF:
                LockService.start(context, -1);
                break;
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i(TAG, "isMyServiceRunning? " + true);
                return true;
            }
        }
        Log.i(TAG, "isMyServiceRunning? " + false);
        return false;
    }
}
