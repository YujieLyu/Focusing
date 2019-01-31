package com.example.jessie.focusing.View.StartNow;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.jessie.focusing.Controller.Adapter.ViewPagerAdapter;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Utils.StatusBarUtil;

/**
 * @author : Yujie Lyu
 * @date : 19-01-2019
 * @time : 00:54
 */
public class StartNow_Activity extends AppCompatActivity {

    /**
     * new added
     */
    private ViewPager viewPager;
    private NowClockFragment clockFragment;
    private NowAppListFragment nowAppListFragment;
    private MenuItem menuItem;

    /**
     * NEW ADDED
     */
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.nav_timer_now:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.nav_app_list_now:
                    viewPager.setCurrentItem(1);
                    return true;
            }
            return false;
        }
    };


    /**
     * new added todo:
     */
    private void setViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        clockFragment = new NowClockFragment();
        nowAppListFragment = new NowAppListFragment();
        adapter.addFragment(clockFragment);
        adapter.addFragment(nowAppListFragment);
        viewPager.setAdapter(adapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startnow);
        viewPager = findViewById(R.id.viewpager_now);
        final BottomNavigationView navigationView = findViewById(R.id.bottom_nav_now);
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

        StatusBarUtil.setStatusTransparent(this);
    }


}
