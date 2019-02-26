package com.example.jessie.focusing.View.StartNow;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.jessie.focusing.Controller.Adapter.ViewPagerAdapter;
import com.example.jessie.focusing.Model.AppInfoManager;
import com.example.jessie.focusing.Model.Profile;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Utils.StatusBarUtil;
import com.example.jessie.focusing.View.Shared.AppInfoListFragment;

import static com.example.jessie.focusing.Utils.AppConstants.PROFILE_ID;

/**
 * @author : Yujie Lyu
 * @date : 19-01-2019
 * @time : 00:54
 */
public class FocusNowActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MenuItem menuItem;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.nav_app_list_now:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.nav_timer_now:
                    viewPager.setCurrentItem(1);
                    return true;
            }
            return false;
        }
    };

    private void setViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Bundle args = new Bundle();
        args.putInt(PROFILE_ID, Profile.START_NOW_PROFILE_ID);
        NowClockFragment clockFragment = new NowClockFragment();
        clockFragment.setArguments(args);
        AppInfoListFragment nowAppListFragment = new AppInfoListFragment();
        nowAppListFragment.setArguments(args);
        adapter.addFragment(nowAppListFragment);
        adapter.addFragment(clockFragment);
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
        AppInfoManager.reset(Profile.START_NOW_PROFILE_ID);
        setViewPager(viewPager);
        StatusBarUtil.setStatusTransparent(this);
        StatusBarUtil.setDarkStatusIcon(this, true);
    }
}
