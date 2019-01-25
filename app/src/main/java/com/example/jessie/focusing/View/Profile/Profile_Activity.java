package com.example.jessie.focusing.View.Profile;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.jessie.focusing.Controller.Adapter.ProfileListAdapter;
import com.example.jessie.focusing.R;

/**
 * @author : Yujie Lyu
 * @date : 19-01-2019
 * @time : 01:02
 */
public class Profile_Activity extends AppCompatActivity {
//    private ViewPager viewPager;
    private ProfileAppListFragment listFragment;
    private ListView lv_profile;
    private ProfileListAdapter profileListAdapter;
    private RelativeLayout profileList;
//    private ProfileAppListFragment listFragment;
    private int profileId=1;



//    private void setupViewPager(ViewPager viewPager){
//        ViewPagerAdapter adapter=new ViewPagerAdapter(getSupportFragmentManager());
//        listFragment =new ProfileAppListFragment();
//        displayFragment=new ProfileDisplayFragment();
//        adapter.addFragment(listFragment);
//        adapter.addFragment(displayFragment);
//        viewPager.setAdapter(adapter);
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profileList=findViewById(R.id.prof_container);
        profileList.setBackgroundResource(R.drawable.n8);
        lv_profile=findViewById(R.id.lv_prof_list);
        profileListAdapter=new ProfileListAdapter(this);

        lv_profile.setAdapter(profileListAdapter);
        initData();
//        viewPager = findViewById(R.id.prof_viewpager);
        listFragment=new ProfileAppListFragment();
//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.prof_container,displayFragment)
//                .commit();
//        setupViewPager(viewPager);
        setStatusTransparent();

    }

    private void initData(){

//        Profile initProfile=new Profile("Meeting");
        profileListAdapter.setData(null);

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

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId+1;
    }
}
