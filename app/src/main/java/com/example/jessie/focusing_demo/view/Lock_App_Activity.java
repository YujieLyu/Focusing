package com.example.jessie.focusing_demo.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jessie.focusing_demo.R;
import com.example.jessie.focusing_demo.adapter.AppListAdapter;
import com.example.jessie.focusing_demo.utils.ScanAppsTool;
import com.example.jessie.focusing_demo.model.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Yujie Lyu
 * @date : 21-12-2018
 * @time : 14:39
 */
public class Lock_App_Activity extends AppCompatActivity {

    private ListView lv_app_list;
    private AppListAdapter appListAdapter;
    public Handler handler = new Handler();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_app_main);
        lv_app_list = findViewById(R.id.lv_app_list);
        appListAdapter = new AppListAdapter(this);
        lv_app_list.setAdapter(appListAdapter);
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
