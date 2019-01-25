package com.example.jessie.focusing.View.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.jessie.focusing.Controller.Adapter.ProfileListAdapter;
import com.example.jessie.focusing.Model.Profile;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.View.Main_Activity;

/**
 * @author : Yujie Lyu
 * @date : 24-01-2019
 * @time : 17:44
 */
public class ProfileDisplayFragment extends Fragment  {

    private ListView lv_profile;
    private ProfileListAdapter profileListAdapter;
    private RelativeLayout profileList;

    public  ProfileDisplayFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_display, container, false);
        profileList=view.findViewById(R.id.fg_display);
        profileList.setBackgroundResource(R.drawable.n8);
        lv_profile=view.findViewById(R.id.lv_prof_list);
        profileListAdapter=new ProfileListAdapter(this.getContext());
        lv_profile.setAdapter(profileListAdapter);
        initData();
        return view;

    }

    private void initData(){

        Profile initProfile=new Profile("Meeting");
        profileListAdapter.setData(initProfile);

    }

//    @Override
//    public void onStop() {
//        super.onStop();
//        profileListAdapter.syncData();
//    }

    public void onBackPressed() {
        Intent intent = new Intent(getActivity(), Main_Activity.class);
        startActivity(intent);

    }



}
