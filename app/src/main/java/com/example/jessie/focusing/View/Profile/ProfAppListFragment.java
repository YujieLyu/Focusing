package com.example.jessie.focusing.View.Profile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jessie.focusing.Controller.Adapter.AppListAdapter;
import com.example.jessie.focusing.Model.AppInfo;
import com.example.jessie.focusing.Model.Profile;
import com.example.jessie.focusing.Model.ProfileManager;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Utils.ScanAppsTool;

import java.util.List;

/**
 * @author : Yujie Lyu
 * @date : 26-01-2019
 * @time : 21:58
 */
public class ProfAppListFragment extends Fragment {
    private TextView tv_profName;
    private ListView lv_appList;
    private AppListAdapter appListAdapter;
    private RelativeLayout applist;
    private int profileId;
    private Profile profile;
    private ProfileManager profileManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_prof_app_list,container,false);
        applist=view.findViewById(R.id.prof_app_list_layout);
        applist.setBackgroundResource(R.drawable.ocean7);
        tv_profName = view.findViewById(R.id.set_prof_name);
//        tv_profName.setText(profile.getProfileName());
        lv_appList = view.findViewById(R.id.prof_app_list);
        appListAdapter = new AppListAdapter(this.getContext());
        lv_appList.setAdapter(appListAdapter);
        profileId=getArguments().getInt("ProfileId",-1);
        profileManager = new ProfileManager(getContext());
        initData();
        tv_profName.setText(profile.getProfileName());
        return view;
    }
    private void initData() {
        profile = profileManager.syncProfileDetail(profileId);
        final List<AppInfo> appInfos=ScanAppsTool.scanAppsList(getActivity().getPackageManager());
        appListAdapter.setData(appInfos,profileId);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final List<AppInfo> appInfos=ScanAppsTool.scanAppsList(LockApp_Activity.this.getPackageManager());
//                appListAdapter.setData(appInfos);
//
//            }
//        }).start();

    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
    @Override
    public void onStop() {
        super.onStop();
        appListAdapter.saveSettings();
    }

}
