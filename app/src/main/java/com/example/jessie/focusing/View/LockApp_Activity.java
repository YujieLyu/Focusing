package com.example.jessie.focusing.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Adapter.AppListAdapter;
import com.example.jessie.focusing.Utils.ScanAppsTool;
import com.example.jessie.focusing.Model.AppInfo;

import java.util.List;

/**
 * @author : Yujie Lyu
 * @date : 21-12-2018
 * @time : 14:39
 */
public class LockApp_Activity extends AppCompatActivity {

    private ListView lv_appList;
    private AppListAdapter appListAdapter;
//    public Handler handler = new Handler();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_applist_main);
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
        final List<AppInfo> appInfos=ScanAppsTool.scanAppsList(LockApp_Activity.this.getPackageManager());
        appListAdapter.setData(appInfos);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final List<AppInfo> appInfos=ScanAppsTool.scanAppsList(LockApp_Activity.this.getPackageManager());
//                appListAdapter.setData(appInfos);
//
//            }
//        }).start();

    }
}
