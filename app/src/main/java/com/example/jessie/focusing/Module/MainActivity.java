package com.example.jessie.focusing.Module;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.jessie.focusing.Interface.TimeCallBack;
import com.example.jessie.focusing.R;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TimeCallBack {

//    private List<String> titles;
    private TextView tv_time1, tv_time2;
    private TimePickerFragment timePicker1, timePicker2;
    private Calendar time1,time2;
    private String displayCurrTime;
    private String countTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_time1 = findViewById(R.id.tx_time1);

        tv_time2 = findViewById(R.id.tx_time2);

        //First time picker
        tv_time1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo:设置点击事件
                //实例化对象
                timePicker1 = new TimePickerFragment();
                timePicker1.show(getSupportFragmentManager(), "time_picker");

            }
        });
        //Second time picker
        tv_time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker2 = new TimePickerFragment();
                timePicker2.show(getSupportFragmentManager(), "time_picker");
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        //两个按钮
        Button btnStart = findViewById(R.id.btn_start);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countTime();
                Intent intent = new Intent(MainActivity.this, Countdown_Activity.class);
                intent.putExtra("endTime", time2.getTimeInMillis());
                intent.putExtra("startTime", time1.getTimeInMillis());
                intent.putExtra("countTime", countTime);//todo:lockScreen删除则此条没用
                startActivity(intent);
            }
        });
        Button btnLockApp = findViewById(R.id.btn_lock_app);
        btnLockApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Lock_App_Activity.class);
                startActivity(intent);
            }
        });

        //浮动邮件标志，可自定义为新建标志
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //侧边栏出现的布局
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //侧边栏--个人中心
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initView();
    }

    public void initData() {

    }

    public void initView() {

//        long time=System.currentTimeMillis();
        final Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(time);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        displayCurrTime = String.format("%02d:%02d", hour, minute);
        tv_time1.setText(displayCurrTime);//todo:显示当前时间
        tv_time2.setText(displayCurrTime);

    }

    @Override
    public void getTime(TimePickerFragment tp, Calendar chosenCalendar) {
        //data即为fragment调用该函数传回的日期时间
        int chosenHour = chosenCalendar.get(Calendar.HOUR_OF_DAY);
        int chosenMin = chosenCalendar.get(Calendar.MINUTE);
        String displayTime =String.format("%02d:%02d",chosenHour,chosenMin);
        if (tp.equals(timePicker1)) {
            tv_time1.setText(displayTime);
            time1 = chosenCalendar;
        } else {
            tv_time2.setText(displayTime);
            time2 = chosenCalendar;
        }
    }

    public void countTime() {
        //todo:判断过了0点的时间计算；是否做成只选第二个时间？
        //todo:need to optimize the calculate,do it later
        int hours;
        int mins;
        String[] curr = displayCurrTime.split(":");
        int currHour = Integer.parseInt(curr[0]);
        int currMin = Integer.parseInt(curr[1]);
        if (time2 == null) {
            //todo:处理time2时间未选择的问题.是否弹窗提示用户选择正确时间
            time2=Calendar.getInstance();
            time2.setTimeInMillis(System.currentTimeMillis());
            countTime = "00:00";
        } else if (time1 == null) {
            time1=Calendar.getInstance();
            time1.setTimeInMillis(System.currentTimeMillis());
            hours=time2.get(Calendar.HOUR_OF_DAY)-currHour;
            mins=time2.get(Calendar.MINUTE)-currMin;
//            String[] parts2 = time2.split(":");
//            hours = Integer.parseInt(parts2[0]) - currHour;
//            mins = Integer.parseInt(parts2[1]) - currMin;
            countTime = hours + ":" + mins;
        } else {
            hours=time2.get(Calendar.HOUR_OF_DAY)-time1.get(Calendar.HOUR_OF_DAY);
            mins=time2.get(Calendar.MINUTE)-time1.get(Calendar.MINUTE);
//            String[] parts1 = time1.split(":");
//            String[] parts2 = time2.split(":");
//            hours = Integer.parseInt(parts2[0]) - Integer.parseInt(parts1[0]);
//            mins = Integer.parseInt(parts2[1]) - Integer.parseInt(parts1[1]);
            if (hours < 0 && mins >= 0) {
                countTime = (24 + hours) + ":" + mins;
            } else if (hours > 0 && mins < 0) {
                countTime = (hours - 1) + ":" + (60 + mins);

            } else if (hours < 0 && mins < 0) {
                countTime = (24 + hours) + ":" + (60 + mins);
            }
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
