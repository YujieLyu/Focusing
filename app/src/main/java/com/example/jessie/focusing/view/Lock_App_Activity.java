package com.example.jessie.focusing.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.jessie.focusing.R;
import com.example.jessie.focusing.adapter.AppListAdapter;
import com.example.jessie.focusing.utils.ScanAppsTool;
import com.example.jessie.focusing.model.AppInfo;

import java.util.List;

/**
 * @author : Yujie Lyu
 * @date : 21-12-2018
 * @time : 14:39
 */
public class Lock_App_Activity extends AppCompatActivity {

    private ListView lv_appList;
    private AppListAdapter appListAdapter;
//    public Handler handler = new Handler();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_app_main);
        lv_appList = findViewById(R.id.lv_app_list);
        appListAdapter = new AppListAdapter(this);
        lv_appList.setAdapter(appListAdapter);
        initData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        appListAdapter.saveInfos();
    }

    private void initData() {
        final List<AppInfo> appInfos=ScanAppsTool.scanAppsList(Lock_App_Activity.this.getPackageManager());
        appListAdapter.setData(appInfos);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final List<AppInfo> appInfos=ScanAppsTool.scanAppsList(Lock_App_Activity.this.getPackageManager());
//                appListAdapter.setData(appInfos);
//
//            }
//        }).start();

    }
}
