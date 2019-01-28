package com.example.jessie.focusing.View.Profile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.jessie.focusing.Controller.Adapter.ProfileListAdapter;
import com.example.jessie.focusing.Model.Profile;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.View.Main_Activity;

/**
 * @author : Yujie Lyu
 * @date : 19-01-2019
 * @time : 01:02
 */
public class ProfileList_Activity extends AppCompatActivity
        implements  AddNewProfileDialog.OnFragmentInteractionListener{
    private ListView lv_profile;
    private ImageButton btn_add;
    private RelativeLayout profileList;
    private ProfileListAdapter profileListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_list);
        profileList = findViewById(R.id.prof_list);
        profileList.setBackgroundResource(R.drawable.n8);
        btn_add =findViewById(R.id.ibtn_add);
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

        setStatusTransparent();

    }

    private void showAddProfileDialog(Profile profile) {
        AddNewProfileDialog dialog=AddNewProfileDialog.newInstance(profile);
        FragmentManager manager=getSupportFragmentManager();
        dialog.show(manager,"AddProfile");

    }

    private void initData() {

        profileListAdapter.setData();

    }

    protected void setStatusTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 5.0+ 实现
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 4.4 实现
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    @Override
    public void onFragmentInteraction(Profile profile) {
        profileListAdapter.addProfile(profile);
        Intent intent=new Intent(ProfileList_Activity.this,ProfileDetailActivity.class);
        intent.putExtra("ProfileId",profile.getId());
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
    //    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent=new Intent(ProfileList_Activity.this,Main_Activity.class);
//        startActivity(intent);
//    }
}
