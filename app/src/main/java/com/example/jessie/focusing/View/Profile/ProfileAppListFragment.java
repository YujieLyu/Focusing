package com.example.jessie.focusing.View.Profile;

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
 * @time : 12:35
 */
public class ProfileAppListFragment extends Fragment {

    private ListView lv_appList;
    private AppListAdapter appListAdapter;
    private RelativeLayout p1_applist;

//    public Handler handler = new Handler();


    @Override
    public void onStop() {
        super.onStop();
        appListAdapter.saveSettings();
    }

    private void initData(int profileId) {
        final List<AppInfo> appInfos = ScanAppsTool.scanAppsList(getActivity().getPackageManager());
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

    public ProfileAppListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_startnow_applist, container, false);
        p1_applist = view.findViewById(R.id.fg_applist);
        p1_applist.setBackgroundResource(R.drawable.n8);
        lv_appList = view.findViewById(R.id.lv_app_list);
        appListAdapter = new AppListAdapter(this.getContext());
        lv_appList.setAdapter(appListAdapter);
        Bundle args = getArguments();
        int profileId = -1;
        if (args != null) {
            profileId = args.getInt("Profile");
        }
        initData(profileId);
        return view;

    }

    public void onBackPressed() {
        getFragmentManager().beginTransaction()
                .replace(R.id.fg_applist, new ProfileScheduleFragment())
                .commit();

    }
}
