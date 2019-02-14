package com.example.jessie.focusing.Service;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static android.content.Intent.ACTION_BOOT_COMPLETED;
import static android.content.Intent.ACTION_SCREEN_OFF;
import static android.content.Intent.ACTION_SCREEN_ON;

/**
 * @author : Yujie Lyu
 */
public class RebootBroadcastReceiver extends BroadcastReceiver {
    public static final String REBOOT_ACTION = "action";
    private static final String TAG = RebootBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context, "action is: " + intent.getAction(), Toast.LENGTH_LONG).show();
        Log.i(TAG, "action is: " + intent.getAction());
        String action = intent.getAction();
        if (action == null) {
            return;
        }
        switch (action) {
            case ACTION_BOOT_COMPLETED:
            case REBOOT_ACTION:
                LockService.start(context, 100);
                break;
            case ACTION_SCREEN_ON:
                LockService.start(context, 100);
                break;
            case Intent.ACTION_TIME_TICK:
                if (!isMyServiceRunning(LockService.class, context)) {
                    LockService.start(context, 100);
                }
                break;
            case ACTION_SCREEN_OFF:
                LockService.start(context, 1000 * 60);
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
