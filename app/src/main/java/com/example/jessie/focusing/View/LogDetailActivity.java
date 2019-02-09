package com.example.jessie.focusing.View;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.jessie.focusing.Controller.Adapter.ViewPagerAdapter;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Utils.StatusBarUtil;
import com.example.jessie.focusing.View.StartNow.NowAppListFragment;
import com.example.jessie.focusing.View.StartNow.NowClockFragment;

/**
 * @author : Yujie Lyu
 * @date : 08-02-2019
 * @time : 02:40
 */
public class LogDetailActivity extends AppCompatActivity {
    /**
     * new added
     */
    private ViewPager viewPager;
    private OpenTimeFragment openTimeFragment;
    private UseTimeFragment useTimeFragment;
    private MenuItem menuItem;

    /**
     * NEW ADDED
     */
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.nav_use_time:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.nav_open_times:
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
        openTimeFragment = new OpenTimeFragment();
        useTimeFragment = new UseTimeFragment();
        adapter.addFragment(openTimeFragment);
        adapter.addFragment(useTimeFragment);
        viewPager.setAdapter(adapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logdetail);
        viewPager = findViewById(R.id.viewpager_logdetail);
        final BottomNavigationView navigationView = findViewById(R.id.bottom_nav_logdetail);
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
        StatusBarUtil.setDarkStatusIcon(this,true);
    }
}
