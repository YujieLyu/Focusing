package com.example.jessie.focusing.View.Shared;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jessie.focusing.Controller.Adapter.AppListAdapter;
import com.example.jessie.focusing.Model.Profile;
import com.example.jessie.focusing.R;

import static com.example.jessie.focusing.Utils.AppConstants.PROFILE_ID;

/**
 * @author : Yujie Lyu
 * @date : 26-01-2019
 * @time : 21:58
 */
public class AppInfoListFragment extends Fragment {
    private AppListAdapter appListAdapter;
    private int profileId = Profile.START_NOW_PROFILE_ID;
    private Profile profile;

    private void initData() {
        profile = Profile.findById(profileId);
        appListAdapter.syncData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prof_app_list, container, false);
        Bundle args = getArguments();
        if (args != null) {
            profileId = args.getInt(PROFILE_ID, Profile.START_NOW_PROFILE_ID);
        }
        ListView lv_appList = view.findViewById(R.id.prof_app_list);
        appListAdapter = new AppListAdapter(getContext(), profileId);
        lv_appList.setAdapter(appListAdapter);
        initData();
        if (profileId != Profile.START_NOW_PROFILE_ID && profile != null) {
            TextView tv_profName = view.findViewById(R.id.tv_title);
            tv_profName.setText(profile.getProfileName());
        }

        return view;
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
