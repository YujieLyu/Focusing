package com.example.jessie.focusing.View.Stats;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.jessie.focusing.Controller.Adapter.ViewPagerAdapter;
import com.example.jessie.focusing.R;
import com.example.jessie.focusing.Utils.StatusBarUtil;

/**
 * @author : Yujie Lyu
 * @date : 08-02-2019
 * @time : 02:40
 */
public class LogDetailActivity extends AppCompatActivity {
    public static final String KEY = "pkName";
    /**
     * new added
     */
    private ViewPager viewPager;
    private MenuItem menuItem;
    private String packageName;
    /**
     * NEW ADDED
     */
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.nav_use_time:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.nav_open_times:
                    viewPager.setCurrentItem(0);
                    return true;
            }
            return false;
        }
    };



    private void setViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Bundle args = new Bundle();
        args.putString(KEY, packageName);
        OpenTimeFragment openTimeFragment = new OpenTimeFragment();
        openTimeFragment.setArguments(args);
        UseTimeFragment useTimeFragment = new UseTimeFragment();
        useTimeFragment.setArguments(args);
        adapter.addFragment(openTimeFragment);
        adapter.addFragment(useTimeFragment);
        viewPager.setAdapter(adapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
            packageName = intent.getStringExtra(KEY);
        }
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
