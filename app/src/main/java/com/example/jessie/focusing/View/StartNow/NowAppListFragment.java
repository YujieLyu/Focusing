package com.example.jessie.focusing.View.StartNow;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.jessie.focusing.Controller.Adapter.AppListAdapter;
import com.example.jessie.focusing.Model.AppInfo;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Utils.ScanAppsTool;

import java.util.List;

/**
 * @author : Yujie Lyu
 * @date : 24-01-2019
 * @time : 08:25
 */
public class NowAppListFragment extends Fragment {

    private ListView lv_appList;
    private AppListAdapter appListAdapter;
    private RelativeLayout applist;
//    public Handler handler = new Handler();


    @Override
    public void onStop() {
        super.onStop();
        appListAdapter.saveSettings();
    }

    private void initData() {
        final List<AppInfo> appInfos=ScanAppsTool.scanAppsList(getActivity().getPackageManager());
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
    public NowAppListFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view =inflater.inflate(R.layout.fragment_startnow_applist,container,false);
        applist=view.findViewById(R.id.fg_applist);
        applist.setBackgroundResource(R.drawable.galaxy2);
        lv_appList = view.findViewById(R.id.lv_app_list);
        appListAdapter = new AppListAdapter(this.getContext());
        lv_appList.setAdapter(appListAdapter);
        initData();
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
}
