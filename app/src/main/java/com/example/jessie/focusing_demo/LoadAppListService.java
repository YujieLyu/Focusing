package com.example.jessie.focusing_demo;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.util.Log;

import java.util.List;

public class LoadAppListService extends IntentService {
    private PackageManager packageManager;
    public LoadAppListService() {
        super("");
    }

    @Override
    public void onCreate(){
        Log.i("step","on Create");
        super.onCreate();
        packageManager =getPackageManager();

    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //每次都获取手机上的所有应用
        Intent intent1=new Intent(Intent.ACTION_MAIN,null);
        intent1.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfos= packageManager.queryIntentActivities(intent1,0);
        //非第一次，对比数据

    }
}
