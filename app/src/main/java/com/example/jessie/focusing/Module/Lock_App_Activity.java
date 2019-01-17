package com.example.jessie.focusing.Module;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Adapter.AppListAdapter;
import com.example.jessie.focusing.utils.ScanAppsTool;
import com.example.jessie.focusing.Model.AppInfo;
import com.example.jessie.focusing.utils.SearchFilter;

import java.util.List;

import static org.litepal.LitePalApplication.getContext;

/**
 * @author : Yujie Lyu
 * @date : 21-12-2018
 * @time : 14:39
 */
public class Lock_App_Activity extends AppCompatActivity implements android.support.v7.widget.SearchView.OnQueryTextListener {

    private ListView lv_appList, lv_searchedlist;
    private AppListAdapter appListAdapter;
    private android.support.v7.widget.SearchView searchView;
    private SearchFilter searchFilter;
    private List<AppInfo> appInfos;
    AppListAdapter searchAdapter;

    public Lock_App_Activity() {
    }
//    public Handler handler = new Handler();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_app_main);
        searchView = findViewById(R.id.search_bar);
        searchView = findViewById(R.id.search_bar);
        searchView.setOnQueryTextListener(this);
        lv_appList = findViewById(R.id.lv_app_list);
//        lv_searchedlist=findViewById(R.id.lv_app_list);
//        searchAdapter=new AppListAdapter(this);
        appListAdapter = new AppListAdapter(this);
        lv_appList.setAdapter(appListAdapter);
//        lv_searchedlist.setAdapter(searchAdapter);

        lv_appList.setTextFilterEnabled(true);
        initData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        appListAdapter.saveInfos();
    }


    private void initData() {
        appInfos = ScanAppsTool.scanAppsList(Lock_App_Activity.this.getPackageManager());
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

    public List<AppInfo> getData() {
        return appInfos;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        //TODO:Yujie for search implementation
        Toast.makeText(this, "search for " + query, Toast.LENGTH_SHORT).show();
        return true;

    }

    @Override
    public boolean onQueryTextChange(String query) {
//        Intent intent=new Intent(Lock_App_Activity.this,SearchFilter.class);
//        intent.putExtra("appList", (Parcelable) appInfoList);
        appListAdapter.getFilter().filter(query);
        return true;
    }
}
