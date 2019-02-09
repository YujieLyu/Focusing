package com.example.jessie.focusing.View.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.jessie.focusing.Controller.Adapter.ProfileListAdapter;
import com.example.jessie.focusing.Model.Profile;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Utils.StatusBarUtil;

/**
 * @author : Yujie Lyu
 * @date : 19-01-2019
 * @time : 01:02
 */
public class ProfileList_Activity extends AppCompatActivity
        implements AddNewProfileDialog.OnFragmentInteractionListener {
    private ListView lv_profile;
    private ImageButton btn_add;
    private RelativeLayout profileList;
    private ProfileListAdapter profileListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_list);
        profileList = findViewById(R.id.prof_list);
        profileList.setBackgroundResource(R.drawable.island);
        btn_add = findViewById(R.id.ibtn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddProfileDialog(null);
            }


        });
        lv_profile = findViewById(R.id.lv_prof_list);
        profileListAdapter = new ProfileListAdapter(this);
        initData();
        lv_profile.setAdapter(profileListAdapter);
        StatusBarUtil.setStatusTransparent(this);
        StatusBarUtil.setDarkStatusIcon(this, true);


    }

    private void showAddProfileDialog(Profile profile) {
        AddNewProfileDialog dialog = AddNewProfileDialog.newInstance(profile);
        FragmentManager manager = getSupportFragmentManager();
        dialog.show(manager, "AddProfile");

    }

    private void initData() {

        profileListAdapter.setData();

    }


    @Override
    public void onFragmentInteraction(Profile profile) {
        profileListAdapter.addProfile(profile);
        Intent intent = new Intent(ProfileList_Activity.this, ProfileDetailActivity.class);
        intent.putExtra("ProfileId", profile.getId());
        startActivity(intent);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        initData();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        profileListAdapter.saveSettings();
    }
}
