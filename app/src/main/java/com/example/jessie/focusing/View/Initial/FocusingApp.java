package com.example.jessie.focusing.View.Initial;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import com.example.jessie.focusing.Service.LockService;
import com.example.jessie.focusing.Service.RebootBroadcastReceiver;

import org.litepal.LitePal;

/**
 * @author : Yujie Lyu
 */
public class FocusingApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
        //        LitePal.deleteDatabase("appDB");//todo: be careful
        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        RebootBroadcastReceiver receiver = new RebootBroadcastReceiver();
        registerReceiver(receiver, filter);
        LockService.start(this, 100);
        ComponentName cpt = new ComponentName(this, RebootBroadcastReceiver.class);
        PackageManager pm = getPackageManager();

        pm.setComponentEnabledSetting(cpt,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }
}
