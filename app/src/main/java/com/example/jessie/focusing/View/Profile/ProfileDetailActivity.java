package com.example.jessie.focusing.View.Profile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.jessie.focusing.Controller.Adapter.ViewPagerAdapter;
import com.example.jessie.focusing.Model.Profile;
import com.example.jessie.focusing.R;

/**
 * @author : Yujie Lyu
 * @date : 26-01-2019
 * @time : 15:08
 */
public class ProfileDetailActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private ProfAppListFragment appListFragment;
    private ProfScheduleFragment calendarFragment;
    private MenuItem menuItem;
    private int profileId;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.nav_cal_prof:
                            viewPager.setCurrentItem(0);
                            return true;
                        case R.id.nav_app_list_prof:
                            viewPager.setCurrentItem(1);
                            return true;

                    }
                    return false;
                }
            };

    public void setViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        calendarFragment = new ProfScheduleFragment();
        appListFragment = new ProfAppListFragment();
        Bundle args=new Bundle();
        args.putInt("ProfileId",profileId);
        appListFragment.setArguments(args);
        adapter.addFragment(calendarFragment);
        adapter.addFragment(appListFragment);
        viewPager.setAdapter(adapter);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileId=getIntent().getIntExtra("ProfileId",-1);
        setContentView(R.layout.activity_profile_detail);
        viewPager = findViewById(R.id.viewpager_prof);
        //todo:final的意义是？
        final BottomNavigationView navigationView = findViewById(R.id.bottom_nav_prof);
        navigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    navigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = navigationView.getMenu().getItem(position);
                menuItem.setChecked(true);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setViewPager(viewPager);
        setStatusTransparent();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        profileId=getIntent().getIntExtra("Profile",-1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(ProfileDetailActivity.this,ProfileList_Activity.class);
        startActivity(intent);
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
}
